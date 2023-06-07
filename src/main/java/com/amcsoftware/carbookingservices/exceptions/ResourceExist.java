package com.amcsoftware.carbookingservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class ResourceExist extends RuntimeException {
    public ResourceExist(String message) {
        super(message);
    }
}
