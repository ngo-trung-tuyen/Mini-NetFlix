package com.mininetflix.backend.common;

public class MiniNetflixAuthKeyCallerValidator {
    public static boolean validateAuthKey(String authKey) {
        //first steps, so just validate easily like this
        return !MiniNetflixCommonUtils.isNullOrEmpty(authKey);
    }

    private MiniNetflixAuthKeyCallerValidator() {
        //empty constructor
    }
}
