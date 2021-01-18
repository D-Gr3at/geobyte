package com.interview.geobyte.exception;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class GeoByteAsyncExceptionHandler extends Throwable implements AsyncUncaughtExceptionHandler {

    public GeoByteAsyncExceptionHandler() {
    }

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        throw new GeoByteException(ex.getMessage());
    }


    public GeoByteAsyncExceptionHandler(String message) {
        super(message);
    }
}
