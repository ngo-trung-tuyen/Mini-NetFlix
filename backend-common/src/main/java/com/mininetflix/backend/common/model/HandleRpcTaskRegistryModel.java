package com.mininetflix.backend.common.model;

import com.mininetflix.backend.common.annotation.HandleRpcTask;
import com.mininetflix.backend.common.task.MiniNetflixTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class HandleRpcTaskRegistryModel {
    private final Map<String, MiniNetflixTask> handleRpcTaskMap;

    @Autowired
    public HandleRpcTaskRegistryModel(List<MiniNetflixTask> handleRpcTasks) {
        this.handleRpcTaskMap = new ConcurrentHashMap<>();
        handleRpcTasks.forEach(miniNetflixTask -> {
            HandleRpcTask handleRpcMethod = miniNetflixTask.getClass().getAnnotation(HandleRpcTask.class);
            handleRpcTaskMap.put(handleRpcMethod.rpcMethod(), miniNetflixTask);
        });
    }

    public MiniNetflixTask getHandleRpcTask(String rpcMethod) {
        return handleRpcTaskMap.get(rpcMethod);
    }
}
