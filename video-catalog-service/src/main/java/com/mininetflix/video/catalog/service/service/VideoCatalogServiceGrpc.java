package com.mininetflix.video.catalog.service.service;

import com.mininetflix.backend.common.entity.DataWrapper;
import com.mininetflix.backend.common.model.HandleRpcTaskRegistryModel;
import com.mininetflix.backend.common.proto.GrpcMultiStringRequest;
import com.mininetflix.backend.common.proto.GrpcStringRequest;
import com.mininetflix.backend.common.proto.GrpcStringResponse;
import com.mininetflix.backend.common.task.MiniNetflixTask;
import com.mininetflix.backend.common.utils.GrpcServiceUtils;
import com.mininetflix.video.catalog.service.proto.GrpcCreateVideoMetadataRequest;
import com.mininetflix.video.catalog.service.proto.GrpcMultiVideoMetadataResponse;
import com.mininetflix.video.catalog.service.proto.GrpcVideoMetadataResponse;
import com.mininetflix.video.catalog.service.proto.MiniNetflixVideoCatalogServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class VideoCatalogServiceGrpc extends
    MiniNetflixVideoCatalogServiceGrpc.MiniNetflixVideoCatalogServiceImplBase {
    private final HandleRpcTaskRegistryModel handleRpcTaskRegistry;

    @Autowired
    public VideoCatalogServiceGrpc(HandleRpcTaskRegistryModel handleRpcTaskRegistry) {
        this.handleRpcTaskRegistry = handleRpcTaskRegistry;
    }

    @Override
    public void createVideoMetadata(GrpcCreateVideoMetadataRequest request, StreamObserver<GrpcStringResponse> responseObserver) {
        MiniNetflixTask task = handleRpcTaskRegistry.getHandleRpcTask("createVideoMetadata");
        DataWrapper dataWrapper = GrpcServiceUtils.buildDataWrapper(request, responseObserver);
        GrpcServiceUtils.exec(task, dataWrapper);
    }

    @Override
    public void getVideoMetadataById(GrpcStringRequest request, StreamObserver<GrpcVideoMetadataResponse> responseObserver) {
        MiniNetflixTask task = handleRpcTaskRegistry.getHandleRpcTask("getVideoMetadataById");
        DataWrapper dataWrapper = GrpcServiceUtils.buildDataWrapper(request, responseObserver);
        GrpcServiceUtils.exec(task, dataWrapper);
    }

    @Override
    public void multiGetVideoMetadataById(GrpcMultiStringRequest request, StreamObserver<GrpcMultiVideoMetadataResponse> responseObserver) {
        MiniNetflixTask task = handleRpcTaskRegistry.getHandleRpcTask("multiGetVideoMetadataById");
        DataWrapper dataWrapper = GrpcServiceUtils.buildDataWrapper(request, responseObserver);
        GrpcServiceUtils.exec(task, dataWrapper);
    }
}
