package com.mininetflix.backend.common;

import io.grpc.stub.StreamObserver;

public class MiniNetflixStreamObserverHandler {
    public static void handleFailed(StreamObserver<?> streamObserver, Throwable throwable) {
        streamObserver.onError(throwable);
        streamObserver.onCompleted();
    }
}
