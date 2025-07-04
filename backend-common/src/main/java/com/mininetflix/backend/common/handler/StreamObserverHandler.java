package com.mininetflix.backend.common.handler;

import io.grpc.stub.StreamObserver;

public class StreamObserverHandler {
    public static void handleFailed(StreamObserver<?> streamObserver, Throwable throwable) {
        streamObserver.onError(throwable);
        streamObserver.onCompleted();
    }
}
