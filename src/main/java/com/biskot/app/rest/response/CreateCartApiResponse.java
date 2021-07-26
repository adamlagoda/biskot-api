package com.biskot.app.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class CreateCartApiResponse {

    @JsonProperty
    long id;

    public static CreateCartApiResponse from(long id) {
        return new CreateCartApiResponse(id);
    }
}
