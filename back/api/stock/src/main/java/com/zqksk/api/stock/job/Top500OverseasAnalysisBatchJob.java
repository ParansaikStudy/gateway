package com.zqksk.api.stock.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqksk.api.stock.client.GeminiClient;
import com.zqksk.api.stock.client.KisDailyItem;
import com.zqksk.api.stock.client.KisOverseasApiClient;
import com.zqksk.api.stock.client.KisPriceOutput;
import com.zqksk.api.stock.config.GeminiProperties;
import com.zqksk.api.stock.config.KisProperties;
import com.zqksk.api.stock.storage.Top100AnalysisStore;
import com.zqksk.api.stock.storage.Top500OverseasAnalysisItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 매일 00:10 KST 해외 탑500(또는 설정된 만큼) KIS 조회(1초 간격) 후
 * Gemini로 종목별 3문장 분석 → top500-overseas-analysis.json 1개로 저장.
 * 사용자 스킬은 이 파일만 조회해 즉시 리턴.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Top500OverseasAnalysisBatchJob {

    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int CHUNK_SIZE = 100;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final KisProperties kisProperties;
    private final GeminiProperties geminiProperties;
    private final KisOverseasApiClient kisOverseasApiClient;
    private final GeminiClient geminiClient;
    private final Top100AnalysisStore store;

    @Scheduled(cron = "${stock.gemini.top500.overseas.cron:0 10 0 * * *}", zone = "Asia/Seoul")
    public void run() {
        if (!kisProperties.isConfigured()) {
            log.warn("탑500 해외 배치 스킵: KIS API 미설정");
            return;
        }
        if (!geminiProperties.isConfigured()) {
            log.warn("탑500 해외 배치 스킵: Gemini API 미설정");
            return;
        }
        List<String> codeLines = store.loadTop500OverseasCodes();
        if (codeLines.isEmpty()) {
            codeLines = getDefaultTop100OverseasCodes();
        }
        if (codeLines.isEmpty()) {
            log.warn("탑500 해외 배치 스킵: 종목 없음. data/top500-overseas-codes.txt 또는 top100-overseas-codes.txt 확인");
            return;
        }
        int limit = Math.min(500, codeLines.size());
        log.info("탑500 해외 배치 시작: {} 개 종목 (KIS 1초 간격)", limit);

        String endDate = LocalDate.now().format(YYYYMMDD);
        List<OverseasSummary> summaries = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            String line = codeLines.get(i);
            String[] parts = line.split(",", 2);
            if (parts.length != 2) continue;
            String excd = parts[0].trim();
            String symbol = parts[1].trim();
            if (excd.isEmpty() || symbol.isEmpty()) continue;
            try {
                KisPriceOutput price = kisOverseasApiClient.inquireOverseasPrice(excd, symbol);
                List<KisDailyItem> daily = kisOverseasApiClient.inquireOverseasDailyChart(excd, symbol, endDate);
                String summary = formatOverseasSummary(excd, symbol, price, daily);
                summaries.add(new OverseasSummary(excd, symbol, summary));
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (Exception e) {
                log.warn("탑500 해외 종목 조회 실패: {} {}, error={}", excd, symbol, e.getMessage());
            }
        }
        if (summaries.isEmpty()) {
            log.warn("탑500 해외 배치: 조회된 데이터 없음");
            return;
        }

        List<Top500OverseasAnalysisItem> allItems = new ArrayList<>();
        for (int from = 0; from < summaries.size(); from += CHUNK_SIZE) {
            int to = Math.min(from + CHUNK_SIZE, summaries.size());
            List<OverseasSummary> chunk = summaries.subList(from, to);
            String prompt = buildChunkPrompt(chunk);
            try {
                String raw = geminiClient.generateContent(prompt);
                List<Top500OverseasAnalysisItem> parsed = parseOverseasAnalysisJson(raw, chunk);
                allItems.addAll(parsed);
                if (to < summaries.size()) {
                    TimeUnit.SECONDS.sleep(2);
                }
            } catch (Exception e) {
                log.error("탑500 해외 Gemini 청크 실패: {}~{}, error={}", from, to, e.getMessage());
            }
        }
        if (!allItems.isEmpty()) {
            store.saveTop500OverseasAnalysis(allItems);
            log.info("탑500 해외 배치 완료: {} 건 저장", allItems.size());
        }
    }

    private String buildChunkPrompt(List<OverseasSummary> chunk) {
        StringBuilder data = new StringBuilder();
        for (OverseasSummary s : chunk) {
            data.append(s.summary).append("\n\n");
        }
        return "다음은 해외(미국 등) 시장 인기 종목 " + chunk.size() + "개의 시세·일봉 요약입니다.\n"
            + "각 종목에 대해 **아래 3문장 형식으로만** 분석해 주세요. 다른 설명은 붙이지 마세요.\n"
            + "1) 지금 {주식명/티커}은 [상방/하방] 압력입니다.\n"
            + "2) [단기선 아래/위], [중기선 근처/위/아래]로 [조정국면/반등 가능성 등] 이러한 상황입니다.\n"
            + "3) 따라서 현재 매도하면 손실율 약 -x~-y%, 매수하면 반등 시 수익률 약 +a~+b% 가능합니다.\n\n"
            + "응답은 **반드시 아래 JSON 배열 하나만** 출력하세요. 코드 블록 없이 순수 JSON만.\n"
            + "[{\"excd\":\"NAS\",\"symbol\":\"AAPL\",\"analysis\":\"1) 지금 애플은 ... 2) ... 3) ...\"}, ...]\n\n"
            + "데이터:\n" + data;
    }

    private List<Top500OverseasAnalysisItem> parseOverseasAnalysisJson(String raw, List<OverseasSummary> fallbackChunk) {
        List<Top500OverseasAnalysisItem> list = new ArrayList<>();
        String json = raw;
        if (json != null) json = json.trim();
        if (json == null || json.isEmpty()) return list;
        int start = json.indexOf('[');
        int end = json.lastIndexOf(']');
        if (start >= 0 && end > start) {
            json = json.substring(start, end + 1);
        }
        try {
            JsonNode arr = MAPPER.readTree(json);
            if (!arr.isArray()) return list;
            for (JsonNode node : arr) {
                String excd = node.path("excd").asText(null);
                String symbol = node.path("symbol").asText(null);
                String analysis = node.path("analysis").asText(null);
                if (excd != null && symbol != null && !excd.isBlank() && !symbol.isBlank() && analysis != null && !analysis.isBlank()) {
                    list.add(new Top500OverseasAnalysisItem(excd.trim(), symbol.trim(), analysis.trim()));
                }
            }
        } catch (Exception e) {
            log.warn("탑500 해외 Gemini JSON 파싱 실패, fallback 사용: {}", e.getMessage());
            for (OverseasSummary s : fallbackChunk) {
                list.add(new Top500OverseasAnalysisItem(s.excd, s.symbol, "(분석 생성 실패)"));
            }
        }
        return list;
    }

    private static String formatOverseasSummary(String excd, String symbol, KisPriceOutput price, List<KisDailyItem> daily) {
        double cur = parseDouble(price != null ? price.getStck_prpr() : null);
        double changePct = parseDouble(price != null ? price.getPrdy_ctrt() : null);
        int size = daily != null ? daily.size() : 0;
        return String.format(Locale.US, "[%s %s] 현재가 $%.2f, 전일대비 %.2f%%, 일봉 %d일", excd, symbol, cur, changePct, size);
    }

    private static double parseDouble(String s) {
        if (s == null || s.isBlank()) return 0;
        try {
            return Double.parseDouble(s.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static final class OverseasSummary {
        final String excd;
        final String symbol;
        final String summary;
        OverseasSummary(String excd, String symbol, String summary) {
            this.excd = excd;
            this.symbol = symbol;
            this.summary = summary;
        }
    }

    private static List<String> getDefaultTop100OverseasCodes() {
        return List.of(
            "NAS,AAPL", "NAS,MSFT", "NAS,GOOGL", "NAS,AMZN", "NAS,NVDA",
            "NAS,META", "NAS,TSLA", "NAS,AVGO", "NAS,COST", "NAS,PEP",
            "NAS,NFLX", "NAS,ADBE", "NAS,CSCO", "NAS,AMD", "NAS,INTC",
            "NAS,QCOM", "NAS,TXN", "NAS,INTU", "NAS,AMAT", "NAS,SBUX",
            "NYS,BRK.B", "NYS,JPM", "NYS,V", "NYS,MA", "NYS,UNH",
            "NYS,JNJ", "NYS,PG", "NYS,HD", "NYS,DIS", "NYS,XOM",
            "NYS,CVX", "NYS,KO", "NYS,WMT", "NYS,MCD", "NYS,ABBV",
            "NYS,MRK", "NYS,PFE", "NYS,BAC", "NYS,WFC", "NYS,GS",
            "NYS,MS", "NYS,AXP", "NYS,CRM", "NYS,VZ", "NYS,T",
            "NYS,BMY", "NYS,PM", "NYS,UNP", "NYS,LMT", "NYS,HON",
            "NYS,RTX", "NYS,UPS", "NYS,LOW", "NYS,IBM", "NYS,GE",
            "NYS,AMGN", "NYS,SPGI", "NYS,DE", "NYS,CAT", "NYS,ORCL",
            "NYS,GILD", "NYS,MDT", "NYS,SCHW", "NYS,BLK", "NYS,ADI",
            "NYS,C", "NYS,MMC", "NYS,REGN", "NYS,LRCX", "NYS,PLD",
            "NYS,CI", "NYS,SO", "NYS,DUK", "NYS,BDX", "NYS,EOG",
            "NYS,MO", "NYS,SLB", "NYS,FIS", "NYS,CMCSA", "NYS,ZTS",
            "NYS,APD", "NYS,USB", "NYS,PGR", "NYS,KLAC", "NYS,TGT",
            "NYS,MCO", "NYS,CB", "NYS,SYK", "NYS,DHR", "NYS,NEE",
            "NYS,BSX", "NYS,WM", "NYS,ICE", "NYS,EQIX", "NYS,SHW"
        );
    }
}
