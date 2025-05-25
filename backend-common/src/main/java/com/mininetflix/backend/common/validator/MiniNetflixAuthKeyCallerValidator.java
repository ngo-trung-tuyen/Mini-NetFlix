package com.mininetflix.backend.common.validator;

import com.mininetflix.backend.common.utils.MiniNetflixCommonUtils;

public class MiniNetflixAuthKeyCallerValidator {
    public static boolean validateAuthKey(String authKey) {
        //first steps, so just validate easily like this
        return !MiniNetflixCommonUtils.isNullOrEmpty(authKey);
    }

    private MiniNetflixAuthKeyCallerValidator() {
        //empty constructor
    }
}
