package com.interview.geobyte.exception;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@AllArgsConstructor
public class GeoByteException extends RuntimeException {
    public GeoByteException(String s) {
        super(s);
    }

    public GeoByteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
