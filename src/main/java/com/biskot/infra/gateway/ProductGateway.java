package com.biskot.infra.gateway;

import com.biskot.domain.exception.generic.ProductNotFound;
import com.biskot.domain.model.Product;
import com.biskot.domain.spi.ProductPort;
import com.biskot.infra.gateway.payload.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class ProductGateway implements ProductPort {

    @Autowired
    private WebClient productsWebClient;

    public Optional<Product> getProduct(long productId) {
        ProductResponse response = productsWebClient.get()
                .uri("/" + productId)
                .retrieve()
                .onStatus(HttpStatus::isError, resp -> Mono.empty())
                .bodyToMono(ProductResponse.class)
                .block();

        return Optional.ofNullable(response)
                .map(Product::fromResponse);
    }

}
