package com.mininetflix.video.catalog.service.service;

import com.mininetflix.backend.common.error.MiniNetflixBackendErrorCode;
import com.mininetflix.backend.common.error.MiniNetflixBackendErrorMessage;
import com.mininetflix.backend.common.proto.GrpcStringResponse;
import com.mininetflix.backend.common.validator.MiniNetflixAuthKeyCallerValidator;
import com.mininetflix.video.catalog.service.entity.VideoMetadata;
import com.mininetflix.video.catalog.service.helper.VideoCatalogHelper;
import com.mininetflix.video.catalog.service.proto.GrpcCreateVideoMetadataRequest;
import com.mininetflix.video.catalog.service.proto.MiniNetflixVideoCatalogServiceGrpcGrpc;
import com.mininetflix.video.catalog.service.repository.VideoMetadataRepository;
import com.mininetflix.video.catalog.service.transformer.VideoMetadataTransformer;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class VideoCatalogServiceGrpc extends
    MiniNetflixVideoCatalogServiceGrpcGrpc.MiniNetflixVideoCatalogServiceGrpcImplBase {
    private final VideoMetadataRepository videoMetadataRepository;
    private final VideoMetadataTransformer videoMetadataTransformer = new VideoMetadataTransformer();

    @Override
    public void createVideoMetadata(GrpcCreateVideoMetadataRequest request, StreamObserver<GrpcStringResponse> responseObserver) {
        Thread.startVirtualThread(() -> {
            GrpcStringResponse.Builder messageBuilder = GrpcStringResponse.newBuilder();
            if (!MiniNetflixAuthKeyCallerValidator.validateAuthKey(request.getAuthKey())
                || !VideoCatalogHelper.validateVideoMetadata(request.getVideoMetadata())
                || !request.hasVideoMetadata()) {
                messageBuilder
                    .setErrCode(MiniNetflixBackendErrorCode.INVALID_REQUEST)
                    .setErrMsg(MiniNetflixBackendErrorMessage.INVALID_REQUEST);
                responseObserver.onNext(messageBuilder.build());
                responseObserver.onCompleted();
            }

            try {
                VideoMetadata createMetadata = videoMetadataTransformer.fromGrpcToObject(request.getVideoMetadata());
                VideoMetadata createdMetadata = videoMetadataRepository.save(createMetadata);

                messageBuilder
                    .setErrCode(MiniNetflixBackendErrorCode.SUCCESS)
                    .setErrMsg(MiniNetflixBackendErrorMessage.SUCCESS)
                    .setValue(createdMetadata.getVideoId().toString());
                responseObserver.onNext(messageBuilder.build());
                responseObserver.onCompleted();
            } catch (Exception ex) {
                responseObserver.onError(
                    Status.INTERNAL
                        .withDescription(ex.getMessage())
                        .withCause(ex)
                        .asRuntimeException()
                );
            }
        });
    }
}
