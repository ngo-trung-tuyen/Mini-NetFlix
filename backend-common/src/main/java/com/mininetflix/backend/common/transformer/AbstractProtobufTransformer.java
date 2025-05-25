package com.mininetflix.backend.common.transformer;

import com.google.protobuf.MessageLite;

public abstract class AbstractProtobufTransformer<T1 extends MessageLite, T2> {
    public abstract T2 fromGrpcToObject(T1 t1);
    public abstract T1 fromObjectToGrpc(T2 t2);
}
