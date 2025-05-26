package com.mininetflix.backend.common.error;

public class BackendErrorMessage {
    public static final String SUCCESS = "success";
    public static final String NOT_FOUND = "not found";
    public static final String INTERNAL_SERVER_ERROR = "internal server error";
    public static final String INVALID_REQUEST = "invalid request";
    public static final String INVALID_CALLER = "invalid caller";

    private BackendErrorMessage() {
        //empty constructor
    }
}
