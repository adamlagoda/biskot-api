package com.biskot.infra.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ProductsWebClientConfiguration {

    private static final String PRODUCTS_SERVER_URL = "http://localhost:9001/products";

    @Bean
    public WebClient productsWebClient() {
        return WebClient.create(PRODUCTS_SERVER_URL);
    }
}
