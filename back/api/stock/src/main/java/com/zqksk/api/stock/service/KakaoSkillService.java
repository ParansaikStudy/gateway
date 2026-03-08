package com.zqksk.api.stock.service;

import com.zqksk.api.stock.storage.Top100AnalysisStore;
import com.zqksk.api.stock.storage.Top500DomesticAnalysisItem;
import com.zqksk.api.stock.storage.Top500OverseasAnalysisItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 카카오 스킬: userRequest.utterance(종목명 또는 6자리 코드) →
 * 국내·해외 탑500에 있으면 미리 저장된 분석만 즉시 리턴, 없으면 "없는 종목"만 리턴. (5초 이내 응답)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoSkillService {

    private static final String MSG_NOT_IN_TOP500 = "탑500에 포함된 종목만 조회 가능합니다. (없는 종목입니다.)";

    /** 탑500 파일이 없을 때 보조 종목명→코드 (국내) */
    private static final Map<String, String> FALLBACK_NAME_TO_CODE = Map.ofEntries(
        Map.entry("삼성전자", "005930"), Map.entry("삼성", "005930"),
        Map.entry("sk하이닉스", "000660"), Map.entry("하이닉스", "000660"),
        Map.entry("네이버", "035420"), Map.entry("카카오", "035720"),
        Map.entry("현대차", "005380"), Map.entry("현대자동차", "005380"),
        Map.entry("기아", "000270"), Map.entry("lg에너지솔루션", "373220"), Map.entry("lg에너지", "373220"),
        Map.entry("셀트리온", "068270"), Map.entry("삼성바이오로직스", "207940"), Map.entry("삼성바이오", "207940"),
        Map.entry("삼성sdi", "006400"), Map.entry("kb금융", "105560")
    );

    /** 해외 종목명/별칭 → "거래소코드,심볼" (예: NAS,NVDA). 탑100 목록 없을 때 보조 검색용 */
    private static final Map<String, String> FALLBACK_OVERSEAS_NAME_TO_EXCD_SYMBOL = Map.ofEntries(
        Map.entry("엔비디아", "NAS,NVDA"), Map.entry("nvidia", "NAS,NVDA"), Map.entry("nvda", "NAS,NVDA"),
        Map.entry("애플", "NAS,AAPL"), Map.entry("apple", "NAS,AAPL"), Map.entry("aapl", "NAS,AAPL"),
        Map.entry("마이크로소프트", "NAS,MSFT"), Map.entry("microsoft", "NAS,MSFT"), Map.entry("msft", "NAS,MSFT"),
        Map.entry("테슬라", "NAS,TSLA"), Map.entry("tesla", "NAS,TSLA"), Map.entry("tsla", "NAS,TSLA"),
        Map.entry("알파벳", "NAS,GOOGL"), Map.entry("구글", "NAS,GOOGL"), Map.entry("google", "NAS,GOOGL"), Map.entry("googl", "NAS,GOOGL"),
        Map.entry("아마존", "NAS,AMZN"), Map.entry("amazon", "NAS,AMZN"), Map.entry("amzn", "NAS,AMZN"),
        Map.entry("메타", "NAS,META"), Map.entry("meta", "NAS,META"), Map.entry("페이스북", "NAS,META"),
        Map.entry("amd", "NAS,AMD"), Map.entry("인텔", "NAS,INTC"), Map.entry("intel", "NAS,INTC"), Map.entry("intc", "NAS,INTC"),
        Map.entry("넷플릭스", "NAS,NFLX"), Map.entry("netflix", "NAS,NFLX"), Map.entry("nflx", "NAS,NFLX"),
        Map.entry("코스트코", "NAS,COST"), Map.entry("costco", "NAS,COST")
    );

    private final Top100AnalysisStore top100AnalysisStore;

    /**
     * 사용자 발화(종목명 또는 6자리 코드) → 탑500에 있으면 저장된 분석 즉시 리턴, 없으면 "없는 종목"만 리턴.
     */
    public String analyzeFromUtterance(String utterance) {
        if (utterance == null || utterance.isBlank()) {
            return "종목명 또는 6자리 종목코드를 입력해 주세요. 예: 삼성전자, 005930";
        }
        String trimmed = utterance.trim();

        Map<String, String> domesticMap = top100AnalysisStore.loadTop500DomesticAnalysisMap();
        Map<String, String> overseasMap = top100AnalysisStore.loadTop500OverseasAnalysisMap();
        List<Top500DomesticAnalysisItem> domesticItems = top100AnalysisStore.loadTop500DomesticAnalysisItems();
        List<Top500OverseasAnalysisItem> overseasItems = top100AnalysisStore.loadTop500OverseasAnalysisItems();

        // 1) 6자리 숫자 → 국내 탑500에 있으면 저장된 분석 리턴
        if (trimmed.matches("\\d{6}")) {
            String code = trimmed;
            String analysis = domesticMap.get(code);
            if (analysis != null && !analysis.isBlank()) {
                return analysis;
            }
            return MSG_NOT_IN_TOP500;
        }

        // 2) 국내 탑500 종목명으로 검색 (저장된 items에서 name 매칭)
        Optional<Top500DomesticAnalysisItem> domesticMatch = findDomesticByName(domesticItems, trimmed);
        if (domesticMatch.isPresent()) {
            String analysis = domesticMatch.get().getAnalysis();
            if (analysis != null && !analysis.isBlank()) return analysis;
        }
        String fallbackCode = resolveFallbackName(trimmed);
        if (fallbackCode != null && domesticMap.containsKey(fallbackCode)) {
            String analysis = domesticMap.get(fallbackCode);
            if (analysis != null && !analysis.isBlank()) return analysis;
        }

        // 3) 해외: 탑500에 있으면 저장된 분석 리턴
        Optional<OverseasResolved> overseas = resolveOverseasForTop500(trimmed, overseasItems);
        if (overseas.isPresent()) {
            String key = overseas.get().excd + "," + overseas.get().symbol;
            String analysis = overseasMap.get(key);
            if (analysis != null && !analysis.isBlank()) return analysis;
        }

        return MSG_NOT_IN_TOP500;
    }

    /** 해외 종목 해석 결과 (거래소코드 + 심볼) */
    private static class OverseasResolved {
        final String excd;
        final String symbol;
        OverseasResolved(String excd, String symbol) {
            this.excd = excd;
            this.symbol = symbol;
        }
    }

    private Optional<Top500DomesticAnalysisItem> findDomesticByName(List<Top500DomesticAnalysisItem> items, String utterance) {
        String normalized = utterance.replaceAll("\\s+", "").toLowerCase();
        for (Top500DomesticAnalysisItem item : items) {
            if (item.getCode() != null && item.getCode().equals(utterance.trim())) return Optional.of(item);
            if (item.getName() == null) continue;
            String nameNorm = item.getName().replaceAll("\\s+", "").toLowerCase();
            if (nameNorm.equals(normalized) || nameNorm.contains(normalized) || normalized.contains(nameNorm)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    private Optional<OverseasResolved> resolveOverseasForTop500(String utterance, List<Top500OverseasAnalysisItem> overseasItems) {
        String normalized = utterance.replaceAll("\\s+", "").toLowerCase();
        String excdSymbol = FALLBACK_OVERSEAS_NAME_TO_EXCD_SYMBOL.get(normalized);
        if (excdSymbol != null) {
            String[] parts = excdSymbol.split(",", 2);
            if (parts.length == 2 && !parts[0].isBlank() && !parts[1].isBlank()) {
                return Optional.of(new OverseasResolved(parts[0].trim(), parts[1].trim()));
            }
        }
        for (Top500OverseasAnalysisItem item : overseasItems) {
            if (item.getSymbol() != null && item.getSymbol().equalsIgnoreCase(normalized)) {
                return Optional.of(new OverseasResolved(item.getExcd(), item.getSymbol()));
            }
            if (item.getSymbol() != null && normalized.equals(item.getSymbol().replaceAll("\\s+", "").toLowerCase())) {
                return Optional.of(new OverseasResolved(item.getExcd(), item.getSymbol()));
            }
        }
        return Optional.empty();
    }

    private String resolveFallbackName(String utterance) {
        String key = utterance.replaceAll("\\s+", "").toLowerCase();
        return FALLBACK_NAME_TO_CODE.get(key);
    }
}
