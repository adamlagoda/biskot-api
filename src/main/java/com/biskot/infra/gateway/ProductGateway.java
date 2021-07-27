package com.biskot.infra.gateway;

import com.biskot.domain.model.Product;
import com.biskot.domain.spi.ProductPort;
import com.biskot.infra.gateway.payload.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

@Component
public class ProductGateway implements ProductPort {

    private static final List<String> FILE_NAMES = List.of(
            "/mocks/product_1.json",
            "/mocks/product_2.json",
            "/mocks/product_3.json",
            "/mocks/product_4.json"
    );

    private List<ProductResponse> products;

    @Autowired
    private ObjectMapper mapper;

    @PostConstruct
    private void readProducts() {
        List<ProductResponse> modifiableProducts = new LinkedList<>();
        for (String fileName : FILE_NAMES) {
            try (InputStream is = ProductResponse.class.getResourceAsStream(fileName)) {
                ProductResponse product = mapper.readValue(is, ProductResponse.class);
                modifiableProducts.add(product);
            } catch (IOException e) {
                e.printStackTrace();
            }
            products = unmodifiableList(modifiableProducts);
        }
    }

    public Optional<Product> getProduct(long productId) {
        return products.stream()
                .filter(product -> product.getId() == productId)
                .map(Product::fromResponse)
                .findAny();
    }

}
