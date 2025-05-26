package com.mininetflix.backend.common.error;

public class BackendErrorCode {
    public static final int SUCCESS = 200;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int INVALID_REQUEST = -99;
    public static final int INVALID_CALLER = -77;

    private BackendErrorCode() {
        // empty constructor
    }
}
