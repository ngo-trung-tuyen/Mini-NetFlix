package com.mininetflix.backend.common.entity;

import io.grpc.stub.StreamObserver;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GrpcMsgWrapper<RQ, RS> implements MessageWrapper {
    private RQ request;
    private RS response;
    private StreamObserver<RS> responseObserver;
}
