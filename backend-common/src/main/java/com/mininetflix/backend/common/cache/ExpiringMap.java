package com.mininetflix.backend.common.cache;

import com.mininetflix.backend.common.utils.CommonUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExpiringMap<K, V> {
    private final ScheduledExecutorService scheduledExecutorService;
    private final Map<K, ScheduledFuture<V>> removingScheduleMap;
    private final Map<K, V> expiringMap;

    public ExpiringMap() {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        this.removingScheduleMap = new ConcurrentHashMap<>();
        this.expiringMap = new ConcurrentHashMap<>();
    }

    public boolean put(K key, V value) {
        V v = expiringMap.put(key, value);
        return Objects.nonNull(v);
    }

    public boolean putWithTtl(K key, V value, int second) {
        V v = expiringMap.put(key, value);

        if (Objects.nonNull(removingScheduleMap.get(key))) {
            removingScheduleMap.get(key)
                .cancel(false);
        }

        ScheduledFuture<V> newScheduleTask = scheduledExecutorService
            .schedule(() -> {
                V removed = expiringMap.remove(key);
                removingScheduleMap.remove(key);
                return removed;
            }, second, TimeUnit.SECONDS);
        return Objects.nonNull(v);
    }

    public boolean remove(K key) {
        V v = expiringMap.remove(key);
        return Objects.nonNull(v);
    }

    public V get(K key) {
        return expiringMap.get(key);
    }

    public List<V> multiGet(List<K> keys) {
        return CommonUtils.getKeyList(keys, this::get);
    }

    public void shutdown() {
        scheduledExecutorService.shutdown();
    }
}
