package com.mininetflix.backend.common.utils;

import com.google.protobuf.MessageLite;
import com.mininetflix.backend.common.entity.DataWrapper;
import com.mininetflix.backend.common.entity.GrpcMsgWrapper;
import com.mininetflix.backend.common.task.MiniNetflixTask;
import io.grpc.stub.StreamObserver;

import java.util.Objects;

public class GrpcServiceUtils {
    public static void exec(MiniNetflixTask miniNetflixTask, DataWrapper dataWrapper) {
        if (Objects.nonNull(miniNetflixTask) && Objects.nonNull(dataWrapper)) {
            miniNetflixTask.run(dataWrapper);
        }
    }

    public static <RQ extends MessageLite, RS extends MessageLite> DataWrapper buildDataWrapper(RQ req, StreamObserver<RS> resObserver) {
        // just pass by value of grpc req and stream observer, grpc res will be filled data when task done.
        GrpcMsgWrapper<RQ, RS> msgWrapper = new GrpcMsgWrapper<RQ, RS>()
            .setResponseObserver(resObserver)
            .setRequest(req);
        return new DataWrapper().setMsgWrapper(msgWrapper);
    }
}
