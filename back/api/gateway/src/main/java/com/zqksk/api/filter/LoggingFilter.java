package com.zqksk.api.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final int MAX_BODY_LOG_SIZE = 10_000;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        long startTime = System.currentTimeMillis();

        if (shouldSkipLogging(request)) {
            return chain.filter(exchange);
        }

        if (request.getMethod() == HttpMethod.POST || request.getMethod() == HttpMethod.PUT) {
            return ServerWebExchangeUtils.cacheRequestBody(exchange, (serverHttpRequest) -> {
                ServerHttpRequest replicationRequest = exchange.mutate().request(serverHttpRequest).build().getRequest();
                return DataBufferUtils.join(replicationRequest.getBody())
                        .doOnNext(dataBuffer -> {
                            DataBuffer copy = dataBuffer.slice(0, dataBuffer.readableByteCount());
                            byte[] bytes = new byte[copy.readableByteCount()];
                            copy.read(bytes);
                            String requestBody = truncateBody(new String(bytes, StandardCharsets.UTF_8));
                            logRequest(replicationRequest, requestBody);
                            DataBufferUtils.release(copy);
                        })
                        .then(chain.filter(exchange.mutate().request(serverHttpRequest).build()))
                        .doFinally(signalType -> logResponse(response, startTime));
            });
        } else {
            logRequest(request, null);
            return chain.filter(exchange)
                    .doFinally(signalType -> logResponse(response, startTime));
        }
    }

    private boolean shouldSkipLogging(ServerHttpRequest request) {
        return request.getPath().toString().contains("/health") ||
                request.getPath().toString().contains("/actuator");
    }

    private String truncateBody(String body) {
        return body.length() > MAX_BODY_LOG_SIZE
                ? body.substring(0, MAX_BODY_LOG_SIZE) + "... (truncated)"
                : body;
    }

    private void logRequest(ServerHttpRequest request, String body) {
        logger.info(">>>>> Request Id=[{}], Method=[{}], Path=[{}], Header=[{}], Body=[{}]",
                request.getId(),
                request.getMethod(),
                request.getPath(),
                maskSensitiveHeaders(request.getHeaders()),
                body
        );
    }

    private void logResponse(ServerHttpResponse response, long startTime) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("<<<<< Response statusCode=[{}], headers=[{}], elapsedTime(ms)=[{}]",
                response.getStatusCode(),
                response.getHeaders(),
                elapsedTime
        );
    }

    private HttpHeaders maskSensitiveHeaders(HttpHeaders headers) {
        HttpHeaders maskedHeaders = new HttpHeaders();
        headers.forEach((key, value) -> {
            if (isSensitiveHeader(key)) {
                maskedHeaders.add(key, "***MASKED***");
            } else {
                maskedHeaders.addAll(key, value);
            }
        });
        return maskedHeaders;
    }

    private boolean isSensitiveHeader(String headerName) {
//        headerName.toLowerCase().contains("authorization") ||
//                headerName.toLowerCase().contains("token") ||
        return headerName.toLowerCase().contains("password");
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
