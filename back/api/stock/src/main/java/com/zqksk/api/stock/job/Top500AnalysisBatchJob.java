package com.zqksk.api.stock.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqksk.api.stock.client.GeminiClient;
import com.zqksk.api.stock.client.KisApiClient;
import com.zqksk.api.stock.config.GeminiProperties;
import com.zqksk.api.stock.config.KisProperties;
import com.zqksk.api.stock.dto.item.Top500DomesticAnalysisItem;
import com.zqksk.api.stock.dto.kis.KisDailyItem;
import com.zqksk.api.stock.dto.kis.KisPriceOutput;
import com.zqksk.api.stock.storage.Top100AnalysisStore;
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
 * 매일 00:00 KST 국내 탑500(또는 설정된 만큼) KIS 조회(1초 간격) 후
 * Gemini로 종목별 3문장 분석 → top500-domestic-analysis.json 1개로 저장.
 * 사용자 스킬은 이 파일만 조회해 즉시 리턴.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Top500AnalysisBatchJob {

    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int DAILY_DAYS = 30;
    /** Gemini 1회 호출당 종목 수 (출력 토큰 제한 고려) */
    private static final int CHUNK_SIZE = 100;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final KisProperties kisProperties;
    private final GeminiProperties geminiProperties;
    private final KisApiClient kisApiClient;
    private final GeminiClient geminiClient;
    private final Top100AnalysisStore store;

    @Scheduled(cron = "${stock.gemini.top500.cron:0 0 0 * * *}", zone = "Asia/Seoul")
    public void run() {
        if (!kisProperties.isConfigured()) {
            log.warn("탑500 배치 스킵: KIS API 미설정");
            return;
        }
        if (geminiProperties.isConfigured()) {
            log.warn("탑500 배치 스킵: Gemini API 미설정");
            return;
        }
        List<String> codes = store.loadTop500Codes();
        if (codes.isEmpty()) {
            codes = getDefaultTop100Codes();
        }
        if (codes.isEmpty()) {
            log.warn("탑500 배치 스킵: 종목 코드 없음. data/top500-codes.txt 또는 top100-codes.txt 확인");
            return;
        }
        int limit = Math.min(500, codes.size());
        log.info("탑500 국내 배치 시작: {} 개 종목 (KIS 1초 간격)", limit);

        String endDate = LocalDate.now().format(YYYYMMDD);
        String startDate = LocalDate.now().minusDays(DAILY_DAYS).format(YYYYMMDD);
        List<StockSummary> summaries = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            String code = codes.get(i);
            try {
                KisPriceOutput price = kisApiClient.inquirePrice(code);
                List<KisDailyItem> daily = kisApiClient.inquireDailyChart(code, startDate, endDate);
                String name = (price != null && price.getPrdt_name() != null && !price.getPrdt_name().isBlank())
                    ? price.getPrdt_name().trim() : null;
                String summary = formatStockSummary(code, name, price, daily);
                summaries.add(new StockSummary(code, name, summary));
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (Exception e) {
                log.warn("탑500 종목 조회 실패: code={}, error={}", code, e.getMessage());
            }
        }
        if (summaries.isEmpty()) {
            log.warn("탑500 배치: 조회된 데이터 없음");
            return;
        }

        List<Top500DomesticAnalysisItem> allItems = new ArrayList<>();
        for (int from = 0; from < summaries.size(); from += CHUNK_SIZE) {
            int to = Math.min(from + CHUNK_SIZE, summaries.size());
            List<StockSummary> chunk = summaries.subList(from, to);
            String prompt = buildChunkPrompt(chunk);
            try {
                String raw = geminiClient.generateContent(prompt);
                List<Top500DomesticAnalysisItem> parsed = parseDomesticAnalysisJson(raw, chunk);
                allItems.addAll(parsed);
                if (to < summaries.size()) {
                    TimeUnit.SECONDS.sleep(2);
                }
            } catch (Exception e) {
                log.error("탑500 Gemini 청크 실패: {}~{}, error={}", from, to, e.getMessage());
            }
        }
        if (!allItems.isEmpty()) {
            store.saveTop500DomesticAnalysis(allItems);
            log.info("탑500 국내 배치 완료: {} 건 저장", allItems.size());
        }
    }

    private String buildChunkPrompt(List<StockSummary> chunk) {
        StringBuilder data = new StringBuilder();
        for (StockSummary s : chunk) {
            data.append(s.summary).append("\n\n");
        }

        return "다음은 한국 시장 인기 종목 " + chunk.size() + "개의 시세·일봉 요약입니다.\n"
            + "각 종목에 대해 **아래 3문장 형식으로만** 분석해 주세요. 다른 설명은 붙이지 마세요.\n"
            + "1) 지금 {주식명}은 [상방/하방] 압력입니다.\n"
            + "2) [단기선 아래/위], [중기선 근처/위/아래]로 [조정국면/반등 가능성 등] 이러한 상황입니다.\n"
            + "3) 따라서 현재 매도하면 손실율 약 -x~-y%, 매수하면 반등 시 수익률 약 +a~+b% 가능합니다.\n\n"
            + "응답은 **반드시 아래 JSON 배열 하나만** 출력하세요. 코드 블록 없이 순수 JSON만.\n"
            + "[{\"code\":\"005930\",\"name\":\"삼성전자\",\"analysis\":\"1) 지금 삼성전자는 ... 2) ... 3) ...\"}, ...]\n\n"
            + "데이터:\n" + data;
    }

    private List<Top500DomesticAnalysisItem> parseDomesticAnalysisJson(String raw, List<StockSummary> fallbackChunk) {
        List<Top500DomesticAnalysisItem> list = new ArrayList<>();
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
                String code = node.path("code").asText(null);
                String name = node.path("name").asText(null);
                String analysis = node.path("analysis").asText(null);
                if (code != null && code.matches("\\d{6}") && analysis != null && !analysis.isBlank()) {
                    list.add(new Top500DomesticAnalysisItem(code, name, analysis.trim()));
                }
            }
        } catch (Exception e) {
            log.warn("탑500 Gemini JSON 파싱 실패, fallback 사용: {}", e.getMessage());
            for (StockSummary s : fallbackChunk) {
                list.add(new Top500DomesticAnalysisItem(s.code, s.name, "(분석 생성 실패)"));
            }
        }
        return list;
    }

    private static String formatStockSummary(String code, String name, KisPriceOutput price, List<KisDailyItem> daily) {
        double cur = parseDouble(price != null ? price.getStck_prpr() : null);
        double changePct = parseDouble(price != null ? price.getPrdy_ctrt() : null);
        String per = price != null ? price.getPer() : null;
        int size = daily != null ? daily.size() : 0;
        return String.format(Locale.US, "[종목코드 %s] %s | 현재가 %.0f원, 전일대비 %.2f%%, PER %s, 일봉 %d일",
            code, name != null ? name : "", cur, changePct, per != null && !per.isBlank() ? per : "-", size);
    }

    private static double parseDouble(String s) {
        if (s == null || s.isBlank()) return 0;
        try {
            return Double.parseDouble(s.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static final class StockSummary {
        final String code;
        final String name;
        final String summary;
        StockSummary(String code, String name, String summary) {
            this.code = code;
            this.name = name;
            this.summary = summary;
        }
    }

    private List<String> getDefaultTop100Codes() {
        return List.of(
            "005930", "000660", "035420", "051910", "006400",
            "005380", "035720", "000270", "068270", "207940",
            "005490", "012330", "066570", "003550", "017670",
            "051900", "000810", "009150", "032830", "033780",
            "247540", "034730", "009540", "034020", "000720",
            "028260", "161390", "018880", "086520", "011200",
            "096770", "003670", "009830", "251270", "011070",
            "066970", "000100", "024110", "316140", "009420",
            "034220", "008930", "004020", "003490", "017800",
            "011170", "009410", "004170", "000850", "009290",
            "009200", "011780", "008350", "001800", "004990",
            "003540", "001450", "008260", "001040", "001230",
            "008730", "001460", "007310", "002790", "001380",
            "001120", "001740", "002380", "004840", "005250",
            "005680", "005720", "005740", "005830", "005940",
            "006050", "006120", "006260", "006360", "006650",
            "006800", "006840", "007070", "007210", "007460",
            "007540", "007810", "008060", "105560", "373220"
        );
    }
}
