package com.zqksk.stock.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqksk.stock.model.StockInfo;
import com.zqksk.stock.model.StockPrice;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class YahooFinanceClient {

    private static final String CHART_URL = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=%s&interval=1d";
    private static final String SEARCH_URL = "https://query1.finance.yahoo.com/v1/finance/search?q=%s&newsCount=10&quotesCount=0";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public YahooFinanceClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 1년간의 일별 주가 데이터 조회
     */
    public List<StockPrice> fetchDailyPrices(String symbol, String range) {
        try {
            String url = String.format(CHART_URL, symbol, range);
            JsonNode root = callApi(url);
            JsonNode result = root.path("chart").path("result").get(0);

            JsonNode timestamps = result.path("timestamp");
            JsonNode quotes = result.path("indicators").path("quote").get(0);

            JsonNode opens = quotes.path("open");
            JsonNode highs = quotes.path("high");
            JsonNode lows = quotes.path("low");
            JsonNode closes = quotes.path("close");
            JsonNode volumes = quotes.path("volume");

            List<StockPrice> prices = new ArrayList<>();
            for (int i = 0; i < timestamps.size(); i++) {
                long ts = timestamps.get(i).asLong();
                LocalDate date = Instant.ofEpochSecond(ts)
                        .atZone(ZoneId.of("America/New_York"))
                        .toLocalDate();

                double open = getDoubleOrZero(opens, i);
                double high = getDoubleOrZero(highs, i);
                double low = getDoubleOrZero(lows, i);
                double close = getDoubleOrZero(closes, i);
                long volume = volumes.get(i).isNull() ? 0 : volumes.get(i).asLong();

                if (close > 0) {
                    prices.add(new StockPrice(date, open, high, low, close, volume));
                }
            }
            return prices;
        } catch (Exception e) {
            throw new RuntimeException("주가 데이터 조회 실패: " + symbol, e);
        }
    }

    /**
     * 종목 기본 정보 조회
     */
    public StockInfo fetchStockInfo(String symbol) {
        try {
            String url = String.format(CHART_URL, symbol, "5d");
            JsonNode root = callApi(url);
            JsonNode result = root.path("chart").path("result").get(0);
            JsonNode meta = result.path("meta");

            String name = meta.path("longName").asText(
                    meta.path("shortName").asText(symbol));
            String exchange = meta.path("exchangeName").asText("N/A");
            String currency = meta.path("currency").asText("USD");
            double currentPrice = meta.path("regularMarketPrice").asDouble();
            double previousClose = meta.path("chartPreviousClose").asDouble(
                    meta.path("previousClose").asDouble());

            // 1년 데이터에서 52주 고저, 시총 등 계산
            List<StockPrice> yearPrices = fetchDailyPrices(symbol, "1y");
            double week52High = yearPrices.stream().mapToDouble(StockPrice::high).max().orElse(0);
            double week52Low = yearPrices.stream().mapToDouble(StockPrice::low).min().orElse(0);

            // 시가총액은 meta에서 가져오기 시도
            double marketCap = meta.path("marketCap").asDouble(0);

            // 추가 재무 데이터 (chart API에서는 제한적이므로 기본값 사용)
            double beta = 0;
            double trailingPE = 0;
            double forwardPE = 0;
            double operatingMargin = 0;
            double debtToEquity = 0;

            return new StockInfo(
                    symbol, name, exchange, currency,
                    currentPrice, previousClose, marketCap,
                    week52High, week52Low,
                    beta, trailingPE, forwardPE, operatingMargin, debtToEquity
            );
        } catch (Exception e) {
            throw new RuntimeException("종목 정보 조회 실패: " + symbol, e);
        }
    }

    /**
     * 최근 뉴스 헤드라인 조회
     */
    public List<String[]> fetchNews(String symbol) {
        try {
            String url = String.format(SEARCH_URL, symbol);
            JsonNode root = callApi(url);
            JsonNode news = root.path("news");

            List<String[]> headlines = new ArrayList<>();
            for (int i = 0; i < Math.min(news.size(), 8); i++) {
                JsonNode item = news.get(i);
                String title = item.path("title").asText("");
                long publishTime = item.path("providerPublishTime").asLong(0);
                String date = publishTime > 0
                        ? Instant.ofEpochSecond(publishTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().toString()
                        : "N/A";
                String publisher = item.path("publisher").asText("N/A");
                headlines.add(new String[]{title, date, publisher});
            }
            return headlines;
        } catch (Exception e) {
            System.err.println("뉴스 조회 실패 (무시하고 계속): " + e.getMessage());
            return List.of();
        }
    }

    private JsonNode callApi(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API 응답 오류: HTTP " + response.statusCode());
        }

        return objectMapper.readTree(response.body());
    }

    private double getDoubleOrZero(JsonNode array, int index) {
        if (index >= array.size() || array.get(index).isNull()) return 0;
        return array.get(index).asDouble();
    }
}
