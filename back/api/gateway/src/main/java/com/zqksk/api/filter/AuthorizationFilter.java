package com.zqksk.api.filter;

import com.zqksk.api.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    public AuthorizationFilter(@Lazy AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String urlPath = exchange.getRequest().getPath().value();

        if (isExcludedPath(urlPath)) {
            return chain.filter(exchange);
        }

        return authenticateRequest(exchange)
                .flatMap(isAuthenticated -> {
                    if (Boolean.TRUE.equals(isAuthenticated)) {
                        return chain.filter(exchange);
                    }
                    return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                });
    }

    private boolean isExcludedPath(String path) {
        return path.contains("/external-api/zqksk-external-api-service")
                || path.contains("/internal-api/zqksk-internal-api-service")
                || path.contains("/sign-up")
                || path.contains("/login")
                || path.contains("/logout")
                || path.contains("/authentication")
                || path.contains("/forgot-password")
                || path.contains("/change-password")
                || path.contains("/create-user")
                || path.contains("/external-api/v1/notices/view")
                || path.contains("/api/stock/");
    }

    private Mono<Boolean> authenticateRequest(ServerWebExchange exchange) {
        return authService.validateApiToken(exchange.getRequest());
    }

    @Override
    public int getOrder() {
        return -99;
    }
}
