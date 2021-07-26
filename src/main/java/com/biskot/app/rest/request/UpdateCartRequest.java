package com.biskot.app.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UpdateCartRequest {

    @JsonProperty("product_id")
    long productId;

    @JsonProperty
    int quantity;
}
