package com.zqksk.api.service;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthService {

    private final WebClient webClient;

    public AuthService(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        ConnectionProvider provider = ConnectionProvider.builder("auth-provider")
                .maxConnections(120)
                .maxIdleTime(Duration.ofSeconds(60))
                .maxLifeTime(Duration.ofMinutes(5))
                .pendingAcquireTimeout(Duration.ofMillis(10000))
                .pendingAcquireMaxCount(50)
                .evictInBackground(Duration.ofSeconds(30))
                .fifo()
                .build();

        HttpClient client = HttpClient.create(provider)
                .wiretap(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .responseTimeout(Duration.ofMillis(30000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(30000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(30000, TimeUnit.MILLISECONDS)));

        ClientHttpConnector connector = new ReactorClientHttpConnector(client);


        this.webClient = WebClient.builder()
                .clientConnector(connector)
                .filter(lbFunction)
                .baseUrl("http://ZQKSK-AUTH-SERVICE")
                .build();
    }

    public Mono<Boolean> validateApiToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String cookie = request.getHeaders().getFirst(HttpHeaders.COOKIE);

        return webClient.post()
                .uri("/auth/v1/verify")
                .header("Authorization", authHeader)
                .header("Cookie", cookie)
                .retrieve()
                .bodyToMono(Boolean.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(10))
                        .doBeforeRetry(signal -> log.warn("Retrying due to WebClientResponseException"))
                        .filter(throwable ->
                                throwable instanceof WebClientResponseException.ServiceUnavailable ||
                                throwable instanceof WebClientResponseException.TooManyRequests
                        ));
    }

    public Mono<Map> validateAccessToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String cookie = request.getHeaders().getFirst(HttpHeaders.COOKIE);
        return webClient.post()
                .uri("/auth/v1/validate")
                .header("Authorization", authHeader)
                .header("Cookie", cookie)
                .retrieve()
                .bodyToMono(Map.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(10))
                        .doBeforeRetry(signal -> log.warn("Retrying due to WebClientResponseException"))
                        .filter(throwable ->
                                throwable instanceof WebClientResponseException.ServiceUnavailable ||
                                throwable instanceof WebClientResponseException.TooManyRequests
                        ));
    }
}
