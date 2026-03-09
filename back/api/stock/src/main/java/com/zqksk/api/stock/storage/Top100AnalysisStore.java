package com.zqksk.api.stock.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zqksk.api.stock.define.Code;
import com.zqksk.api.stock.dto.FileDTO;
import com.zqksk.api.stock.dto.item.Top100NameCodeItem;
import com.zqksk.api.stock.dto.item.Top100OverseasNameCodeItem;
import com.zqksk.api.stock.dto.item.Top500DomesticAnalysisItem;
import com.zqksk.api.stock.dto.item.Top500OverseasAnalysisItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

/**
 * 인기 종목 탑 100(국내 + 해외)에 대한 Gemini 분석 결과를 JSON 파일로 저장/조회.
 * DB 사용 안 함. 국내: top100-analysis.json / 해외: top100-overseas-analysis.json
 */
@Slf4j
@Component
public class Top100AnalysisStore {

    HashMap<Code, FileDTO> TOP100_ANALYSIS = new HashMap<>();

    private static final String FILENAME = "top100-analysis.json";
    private static final String CODES_FILENAME = "top100-codes.txt";
    private static final String OVERSEAS_FILENAME = "top100-overseas-analysis.json";
    private static final String OVERSEAS_CODES_FILENAME = "top100-overseas-codes.txt";
    private static final String OVERSEAS_NAME_CODE_FILENAME = "top100-overseas-name-code.json";
    private static final String NAME_CODE_FILENAME = "top100-name-code.json";
    /** 탑500 국내 종목별 분석 (code → analysis) */
    private static final String TOP500_DOMESTIC_ANALYSIS_FILENAME = "top500-domestic-analysis.json";
    /** 탑500 해외 종목별 분석 (excd,symbol → analysis) */
    private static final String TOP500_OVERSEAS_ANALYSIS_FILENAME = "top500-overseas-analysis.json";
    private static final String TOP500_CODES_FILENAME = "top500-codes.txt";
    private static final String TOP500_OVERSEAS_CODES_FILENAME = "top500-overseas-codes.txt";
    private static final int TOP500_LIMIT = 500;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Path dataDir;

    public Top100AnalysisStore(@Value("${stock.gemini.data-dir:./data}") String dataDir) {
        this.dataDir = Path.of(dataDir).toAbsolutePath();
    }

    public Path getDataDir() {
        return dataDir;
    }

    /** 저장 파일 경로 */
    public Path getTop100AnalysisPath() {
        return dataDir.resolve(FILENAME);
    }

    /**
     * 인기 종목 탑 100 분석 텍스트 저장.
     * @param analysisText Gemini가 생성한 분석 전체 텍스트
     */
    public void save(String analysisText) {
        try {
            Files.createDirectories(dataDir);
            ObjectNode root = MAPPER.createObjectNode();
//            TOP100_ANALYSIS.put(NVIDIA, 21.3);
            root.put("generatedAt", Instant.now().toString());
            root.put("analysisText", analysisText != null ? analysisText : "");
            Files.writeString(getTop100AnalysisPath(), MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root));
            log.info("탑100 분석 저장 완료: {}", getTop100AnalysisPath());
        } catch (IOException e) {
            log.error("탑100 분석 저장 실패: {}", e.getMessage());
            throw new RuntimeException("탑100 분석 파일 저장 실패", e);
        }
    }

    /**
     * 저장된 탑100 분석 텍스트 조회. 없으면 empty.a
    public Optional<String> load() {
        Path path = getTop100AnalysisPath();
        if (!Files.exists(path)) {
            log.debug("탑100 분석 파일 없음: {}", path);
            return Optional.empty();
        }
        try {
            String json = Files.readString(path);
            JsonNode node = MAPPER.readTree(json);
            String text = node.path("analysisText").asText("");
            return Optional.ofNullable(text.isBlank() ? null : text);
        } catch (IOException e) {
            log.warn("탑100 분석 파일 읽기 실패: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * 탑100 종목명-코드 목록 저장 (00시 배치에서 생성). 종목명으로 검색해 100위 포함 여부·코드 확보용.
     */
    public void saveTop100NameCodeList(List<Top100NameCodeItem> items) {
        try {
            Files.createDirectories(dataDir);
            ObjectNode root = MAPPER.createObjectNode();
            root.put("generatedAt", Instant.now().toString());
            root.set("items", MAPPER.valueToTree(items != null ? items : List.of()));
            Files.writeString(getTop100NameCodePath(), MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root));
            log.info("탑100 종목명-코드 저장 완료: {} 건", items != null ? items.size() : 0);
        } catch (IOException e) {
            log.error("탑100 종목명-코드 저장 실패: {}", e.getMessage());
            throw new RuntimeException("탑100 종목명-코드 저장 실패", e);
        }
    }

    public Path getTop100NameCodePath() {
        return dataDir.resolve(NAME_CODE_FILENAME);
    }

    /**
     * 저장된 탑100 종목명-코드 목록 조회. 없으면 빈 리스트.
     */
    public List<Top100NameCodeItem> loadTop100NameCodeList() {
        Path path = getTop100NameCodePath();
        if (!Files.exists(path)) {
            log.debug("탑100 종목명-코드 파일 없음: {}", path);
            return List.of();
        }
        try {
            String json = Files.readString(path);
            JsonNode node = MAPPER.readTree(json);
            JsonNode arr = node.path("items");
            if (!arr.isArray()) return List.of();
            List<Top100NameCodeItem> list = new ArrayList<>();
            for (JsonNode item : arr) {
                String name = item.path("name").asText(null);
                String code = item.path("code").asText(null);
                if (code != null && code.matches("\\d{6}")) {
                    list.add(new Top100NameCodeItem(name != null ? name.trim() : null, code.trim()));
                }
            }
            return Collections.unmodifiableList(list);
        } catch (IOException e) {
            log.warn("탑100 종목명-코드 파일 읽기 실패: {}", e.getMessage());
            return List.of();
        }
    }

    /** 인기 종목 탑 100 종목코드 목록. data/top100-codes.txt (한 줄에 하나씩 국내 6자리 코드). 없으면 빈 리스트. */
    public List<String> loadTop100Codes() {
        Path path = dataDir.resolve(CODES_FILENAME);
        if (!Files.exists(path)) {
            log.debug("탑100 종목코드 파일 없음: {}", path);
            return List.of();
        }
        try (Stream<String> lines = Files.lines(path)) {
            List<String> codes = new ArrayList<>();
            lines.map(String::trim)
                .filter(s -> !s.isEmpty() && !s.startsWith("#"))
                .filter(s -> s.matches("\\d{6}"))
                .limit(100)
                .forEach(codes::add);
            return codes;
        } catch (IOException e) {
            log.warn("탑100 종목코드 파일 읽기 실패: {}", e.getMessage());
            return List.of();
        }
    }

    // ─── 해외 탑 100 ────────────────────────────────────────────────────────

    public Path getTop100OverseasAnalysisPath() {
        return dataDir.resolve(OVERSEAS_FILENAME);
    }

    /** 해외 탑 100 분석 텍스트 저장 */
    public void saveOverseas(String analysisText) {
        try {
            Files.createDirectories(dataDir);
            ObjectNode root = MAPPER.createObjectNode();
            root.put("generatedAt", Instant.now().toString());
            root.put("analysisText", analysisText != null ? analysisText : "");
            Files.writeString(getTop100OverseasAnalysisPath(), MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root));
            log.info("탑100 해외 분석 저장 완료: {}", getTop100OverseasAnalysisPath());
        } catch (IOException e) {
            log.error("탑100 해외 분석 저장 실패: {}", e.getMessage());
            throw new RuntimeException("탑100 해외 분석 파일 저장 실패", e);
        }
    }

    /** 저장된 해외 탑 100 분석 텍스트 조회 */
    public Optional<String> loadOverseas() {
        Path path = getTop100OverseasAnalysisPath();
        if (!Files.exists(path)) {
            log.debug("탑100 해외 분석 파일 없음: {}", path);
            return Optional.empty();
        }
        try {
            String json = Files.readString(path);
            JsonNode node = MAPPER.readTree(json);
            String text = node.path("analysisText").asText("");
            return Optional.ofNullable(text.isBlank() ? null : text);
        } catch (IOException e) {
            log.warn("탑100 해외 분석 파일 읽기 실패: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /** 해외 탑100 거래소·심볼 목록 저장 경로 */
    public Path getTop100OverseasNameCodePath() {
        return dataDir.resolve(OVERSEAS_NAME_CODE_FILENAME);
    }

    /**
     * 해외 탑100 거래소·심볼 목록 저장 (배치에서 생성). 카카오 스킬 해외 종목 검색용.
     */
    public void saveTop100OverseasNameCodeList(List<Top100OverseasNameCodeItem> items) {
        try {
            Files.createDirectories(dataDir);
            ObjectNode root = MAPPER.createObjectNode();
            root.put("generatedAt", Instant.now().toString());
            root.set("items", MAPPER.valueToTree(items != null ? items : List.of()));
            Files.writeString(getTop100OverseasNameCodePath(), MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root));
            log.info("탑100 해외 종목 목록 저장 완료: {} 건", items != null ? items.size() : 0);
        } catch (IOException e) {
            log.error("탑100 해외 종목 목록 저장 실패: {}", e.getMessage());
            throw new RuntimeException("탑100 해외 종목 목록 저장 실패", e);
        }
    }

    /**
     * 저장된 해외 탑100 거래소·심볼 목록 조회. 없으면 빈 리스트.
     */
    public List<Top100OverseasNameCodeItem> loadTop100OverseasNameCodeList() {
        Path path = getTop100OverseasNameCodePath();
        if (!Files.exists(path)) {
            log.debug("탑100 해외 종목 목록 파일 없음: {}", path);
            return List.of();
        }
        try {
            String json = Files.readString(path);
            JsonNode node = MAPPER.readTree(json);
            JsonNode arr = node.path("items");
            if (!arr.isArray()) return List.of();
            List<Top100OverseasNameCodeItem> list = new ArrayList<>();
            for (JsonNode item : arr) {
                String excd = item.path("excd").asText(null);
                String symbol = item.path("symbol").asText(null);
                if (excd != null && symbol != null && !excd.isBlank() && !symbol.isBlank()) {
                    list.add(new Top100OverseasNameCodeItem(excd.trim(), symbol.trim()));
                }
            }
            return Collections.unmodifiableList(list);
        } catch (IOException e) {
            log.warn("탑100 해외 종목 목록 파일 읽기 실패: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * 해외 탑 100 종목 목록. data/top100-overseas-codes.txt
     * 한 줄에 "거래소코드,종목심볼" (예: NAS,AAPL / NYS,TSLA). 없으면 빈 리스트.
     */
    public List<String> loadTop100OverseasCodes() {
        Path path = dataDir.resolve(OVERSEAS_CODES_FILENAME);
        if (!Files.exists(path)) {
            log.debug("탑100 해외 종목코드 파일 없음: {}", path);
            return List.of();
        }
        try (Stream<String> lines = Files.lines(path)) {
            List<String> codes = new ArrayList<>();
            lines.map(String::trim)
                .filter(s -> !s.isEmpty() && !s.startsWith("#"))
                .filter(s -> s.contains(","))
                .limit(100)
                .forEach(codes::add);
            return codes;
        } catch (IOException e) {
            log.warn("탑100 해외 종목코드 파일 읽기 실패: {}", e.getMessage());
            return List.of();
        }
    }

    // ─── 탑 500 (종목별 분석 저장/조회) ───────────────────────────────────────

    /**
     * 탑500 국내 종목코드 목록. top500-codes.txt 우선(최대 500), 없으면 top100-codes.txt(최대 500).
     */
    public List<String> loadTop500Codes() {
        Path path500 = dataDir.resolve(TOP500_CODES_FILENAME);
        Path path = Files.exists(path500) ? path500 : dataDir.resolve(CODES_FILENAME);
        if (!Files.exists(path)) {
            log.debug("탑500 국내 종목코드 파일 없음: {}", path);
            return List.of();
        }
        try (Stream<String> lines = Files.lines(path)) {
            List<String> codes = new ArrayList<>();
            lines.map(String::trim)
                .filter(s -> !s.isEmpty() && !s.startsWith("#"))
                .filter(s -> s.matches("\\d{6}"))
                .limit(TOP500_LIMIT)
                .forEach(codes::add);
            return codes;
        } catch (IOException e) {
            log.warn("탑500 국내 종목코드 파일 읽기 실패: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * 탑500 해외 종목 목록. top500-overseas-codes.txt 우선(최대 500), 없으면 top100-overseas-codes.txt(최대 500).
     */
    public List<String> loadTop500OverseasCodes() {
        Path path500 = dataDir.resolve(TOP500_OVERSEAS_CODES_FILENAME);
        Path path = Files.exists(path500) ? path500 : dataDir.resolve(OVERSEAS_CODES_FILENAME);
        if (!Files.exists(path)) {
            log.debug("탑500 해외 종목코드 파일 없음: {}", path);
            return List.of();
        }
        try (Stream<String> lines = Files.lines(path)) {
            List<String> codes = new ArrayList<>();
            lines.map(String::trim)
                .filter(s -> !s.isEmpty() && !s.startsWith("#"))
                .filter(s -> s.contains(","))
                .limit(TOP500_LIMIT)
                .forEach(codes::add);
            return codes;
        } catch (IOException e) {
            log.warn("탑500 해외 종목코드 파일 읽기 실패: {}", e.getMessage());
            return List.of();
        }
    }

    public Path getTop500DomesticAnalysisPath() {
        return dataDir.resolve(TOP500_DOMESTIC_ANALYSIS_FILENAME);
    }

    public Path getTop500OverseasAnalysisPath() {
        return dataDir.resolve(TOP500_OVERSEAS_ANALYSIS_FILENAME);
    }

    /** 탑500 국내 종목별 분석 저장 (cron에서 500건 수집·Gemini 분석 후 호출) */
    public void saveTop500DomesticAnalysis(List<Top500DomesticAnalysisItem> items) {
        try {
            Files.createDirectories(dataDir);
            ObjectNode root = MAPPER.createObjectNode();
            root.put("generatedAt", java.time.Instant.now().toString());
            root.set("items", MAPPER.valueToTree(items != null ? items : List.of()));
            Files.writeString(getTop500DomesticAnalysisPath(), MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root));
            log.info("탑500 국내 종목별 분석 저장 완료: {} 건", items != null ? items.size() : 0);
        } catch (IOException e) {
            log.error("탑500 국내 분석 저장 실패: {}", e.getMessage());
            throw new RuntimeException("탑500 국내 분석 파일 저장 실패", e);
        }
    }

    /** 탑500 국내 종목별 분석 조회. 없으면 빈 리스트. */
    public List<Top500DomesticAnalysisItem> loadTop500DomesticAnalysisItems() {
        Path path = getTop500DomesticAnalysisPath();
        if (!Files.exists(path)) {
            log.debug("탑500 국내 분석 파일 없음: {}", path);
            return List.of();
        }
        try {
            String json = Files.readString(path);
            JsonNode node = MAPPER.readTree(json);
            JsonNode arr = node.path("items");
            if (!arr.isArray()) return List.of();
            List<Top500DomesticAnalysisItem> list = new ArrayList<>();
            for (JsonNode item : arr) {
                String code = item.path("code").asText(null);
                String name = item.path("name").asText(null);
                String analysis = item.path("analysis").asText(null);
                if (code != null && code.matches("\\d{6}")) {
                    list.add(new Top500DomesticAnalysisItem(code, name, analysis));
                }
            }
            return Collections.unmodifiableList(list);
        } catch (IOException e) {
            log.warn("탑500 국내 분석 파일 읽기 실패: {}", e.getMessage());
            return List.of();
        }
    }

    /** 탑500 국내 code → analysis 맵 (스킬에서 즉시 리턴용) */
    public Map<String, String> loadTop500DomesticAnalysisMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (Top500DomesticAnalysisItem item : loadTop500DomesticAnalysisItems()) {
            if (item.getCode() != null && item.getAnalysis() != null) {
                map.put(item.getCode(), item.getAnalysis());
            }
        }
        return map;
    }

    /** 탑500 해외 종목별 분석 저장 */
    public void saveTop500OverseasAnalysis(List<Top500OverseasAnalysisItem> items) {
        try {
            Files.createDirectories(dataDir);
            ObjectNode root = MAPPER.createObjectNode();
            root.put("generatedAt", java.time.Instant.now().toString());
            root.set("items", MAPPER.valueToTree(items != null ? items : List.of()));
            Files.writeString(getTop500OverseasAnalysisPath(), MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root));
            log.info("탑500 해외 종목별 분석 저장 완료: {} 건", items != null ? items.size() : 0);
        } catch (IOException e) {
            log.error("탑500 해외 분석 저장 실패: {}", e.getMessage());
            throw new RuntimeException("탑500 해외 분석 파일 저장 실패", e);
        }
    }

    /** 탑500 해외 종목별 분석 조회. 없으면 빈 리스트. */
    public List<Top500OverseasAnalysisItem> loadTop500OverseasAnalysisItems() {
        Path path = getTop500OverseasAnalysisPath();
        if (!Files.exists(path)) {
            log.debug("탑500 해외 분석 파일 없음: {}", path);
            return List.of();
        }
        try {
            String json = Files.readString(path);
            JsonNode node = MAPPER.readTree(json);
            JsonNode arr = node.path("items");
            if (!arr.isArray()) return List.of();
            List<Top500OverseasAnalysisItem> list = new ArrayList<>();
            for (JsonNode item : arr) {
                String excd = item.path("excd").asText(null);
                String symbol = item.path("symbol").asText(null);
                String analysis = item.path("analysis").asText(null);
                if (excd != null && symbol != null && !excd.isBlank() && !symbol.isBlank()) {
                    list.add(new Top500OverseasAnalysisItem(excd, symbol, analysis));
                }
            }
            return Collections.unmodifiableList(list);
        } catch (IOException e) {
            log.warn("탑500 해외 분석 파일 읽기 실패: {}", e.getMessage());
            return List.of();
        }
    }

    /** 탑500 해외 "excd,symbol" → analysis 맵 (스킬에서 즉시 리턴용) */
    public Map<String, String> loadTop500OverseasAnalysisMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (Top500OverseasAnalysisItem item : loadTop500OverseasAnalysisItems()) {
            if (item.getExcd() != null && item.getSymbol() != null && item.getAnalysis() != null) {
                map.put(item.getExcd() + "," + item.getSymbol(), item.getAnalysis());
            }
        }
        return map;
    }
}
