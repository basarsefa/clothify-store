
package com.example.api_gateway.filter;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RequestLoggingWebFilter implements WebFilter {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingWebFilter.class);

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain) {

        long startTime = System.nanoTime();

        String requestId = UUID.randomUUID().toString();

        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .headers(headers ->
                        headers.set(REQUEST_ID_HEADER, requestId))
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(request)
                .build();

        mutatedExchange.getResponse()
                .getHeaders()
                .set(REQUEST_ID_HEADER, requestId);

        String method = request.getMethod() != null
                ? request.getMethod().name()
                : "UNKNOWN";

        String path = request.getURI().getPath();

        return chain.filter(mutatedExchange)
                .doFinally(signalType -> {
                    long durationMs =
                            (System.nanoTime() - startTime) / 1_000_000;

                    HttpStatusCode status =
                            mutatedExchange.getResponse().getStatusCode();

                    int statusCode = status != null
                            ? status.value()
                            : 0;

                    String statusValue = status != null
                            ? String.valueOf(statusCode)
                            : "UNKNOWN";

                    String signal = signalType.name();

                    if (signalType == reactor.core.publisher.SignalType.ON_ERROR
                            || statusCode >= 500) {

                        log.atError()
                                .addKeyValue("requestId", requestId)
                                .addKeyValue("method", method)
                                .addKeyValue("path", path)
                                .addKeyValue("status", statusValue)
                                .addKeyValue("durationMs", durationMs)
                                .addKeyValue("signal", signal)
                                .log("HTTP request completed");

                    } else if (statusCode >= 400) {

                        log.atWarn()
                                .addKeyValue("requestId", requestId)
                                .addKeyValue("method", method)
                                .addKeyValue("path", path)
                                .addKeyValue("status", statusValue)
                                .addKeyValue("durationMs", durationMs)
                                .addKeyValue("signal", signal)
                                .log("HTTP request completed");

                    } else {

                        log.atInfo()
                                .addKeyValue("requestId", requestId)
                                .addKeyValue("method", method)
                                .addKeyValue("path", path)
                                .addKeyValue("status", statusValue)
                                .addKeyValue("durationMs", durationMs)
                                .addKeyValue("signal", signal)
                                .log("HTTP request completed");
                    }
                });
    }
}
