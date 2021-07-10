package com.ruoyi.exception;


public class GlobalShieldCacheException extends RuntimeException {
    public GlobalShieldCacheException() {
        super();
    }

    public GlobalShieldCacheException(String message) {
        super(message);
    }

    public GlobalShieldCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalShieldCacheException(Throwable cause) {
        super(cause);
    }

    protected GlobalShieldCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
