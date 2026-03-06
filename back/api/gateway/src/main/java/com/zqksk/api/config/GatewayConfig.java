package com.zqksk.api.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        for (ApiServer server : ApiServer.values()) {
            String path = server.getPath();
            // path가 / 로 시작하면, / 없이 오는 경우도 매칭 (프록시에 따라 path가 api/stock/... 로 올 수 있음)
            if (path.startsWith("/") && path.length() > 1) {
                String pathNoLeadingSlash = path.substring(1);
                routes = routes.route(server.getId(), r -> r.path(path).or().path(pathNoLeadingSlash).uri(server.getUri()));
            } else {
                routes = routes.route(server.getId(), r -> r.path(path).uri(server.getUri()));
            }
        }
        return routes.build();
    }
}
