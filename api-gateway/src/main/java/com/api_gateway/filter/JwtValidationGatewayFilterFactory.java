package com.api_gateway.filter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;


    public JwtValidationGatewayFilterFactory(@Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {

            String accessToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (accessToken == null || !accessToken.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClient.get()
                    .uri("/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .retrieve()
                    .toBodilessEntity()
                    .then(chain.filter(exchange))
                    .onErrorResume(WebClientResponseException.class, ex -> {
                        exchange.getResponse().setStatusCode(ex.getStatusCode());
                        return exchange.getResponse().setComplete();
                    });
        };
    }
}
