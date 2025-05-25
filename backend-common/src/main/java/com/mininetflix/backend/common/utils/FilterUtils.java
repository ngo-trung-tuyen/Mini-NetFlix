package com.mininetflix.backend.common.utils;

import java.util.Objects;

public class FilterUtils {
    public static String getOrDefault(String src, String def) {
        return MiniNetflixCommonUtils.isNullOrEmpty(src) ? def : src;
    }

    public static Object getOrDefault(Object src, Object def) {
        return Objects.isNull(src) ? def : src;
    }

    private FilterUtils() {
        //empty constructor
    }
}
