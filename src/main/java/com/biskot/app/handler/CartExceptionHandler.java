package com.biskot.app.handler;

import com.biskot.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CartExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BUSINESS_RULES_VIOLATION_MESSAGE = "Business rules have not been respected";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "Cart or product not found";

    @ExceptionHandler({
            MaximumCartValueExceeded.class,
            MaximumNumberOfProductsExceeded.class,
            StockAvailabilityExceeded.class
    })
    public ResponseEntity<String> handleBusinessRulesViolationExceptions(RuntimeException ex, WebRequest webRequest) {
        return new ResponseEntity<>(BUSINESS_RULES_VIOLATION_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            CartNotFound.class,
            ProductNotFound.class
    })
    public ResponseEntity<String> handleEntityNotFoundExceptions(RuntimeException ex, WebRequest webRequest) {
        return new ResponseEntity<>(ENTITY_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
    }

}
