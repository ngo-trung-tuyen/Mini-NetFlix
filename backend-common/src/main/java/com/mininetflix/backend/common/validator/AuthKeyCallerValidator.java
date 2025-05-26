package com.mininetflix.backend.common.validator;

import com.mininetflix.backend.common.utils.CommonUtils;

public class AuthKeyCallerValidator {
    public static boolean validateAuthKey(String authKey) {
        //first steps, so just validate easily like this
        return !CommonUtils.isNullOrEmpty(authKey);
    }

    private AuthKeyCallerValidator() {
        //empty constructor
    }
}
