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
            routes = routes.route(server.getId(), r -> r.path(server.getPath()).uri(server.getUri()));
        }
        return routes.build();
    }
}
