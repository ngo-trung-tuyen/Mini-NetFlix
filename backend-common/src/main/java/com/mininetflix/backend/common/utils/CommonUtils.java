package com.mininetflix.backend.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonUtils {
    public static <T> boolean isNullOrEmpty(Collection<T> source) {
        return source == null || source.isEmpty();
    }

    public static boolean isNullOrEmpty(String source) {
        return source == null || source.isEmpty();
    }

    public static <K, V> List<V> getKeyList(List<K> source, Function<K, V> mappingFunc) {
        return source.stream()
            .map(mappingFunc)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
