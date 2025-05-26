package com.mininetflix.video.catalog.service.task;

import com.mininetflix.backend.common.annotation.HandleRpcTask;
import com.mininetflix.backend.common.entity.DataWrapper;
import com.mininetflix.backend.common.entity.GrpcMsgWrapper;
import com.mininetflix.backend.common.error.BackendErrorCode;
import com.mininetflix.backend.common.error.BackendErrorMessage;
import com.mininetflix.backend.common.proto.GrpcStringResponse;
import com.mininetflix.backend.common.task.MiniNetflixTask;
import com.mininetflix.backend.common.validator.AuthKeyCallerValidator;
import com.mininetflix.video.catalog.service.entity.VideoMetadata;
import com.mininetflix.video.catalog.service.helper.VideoCatalogHelper;
import com.mininetflix.video.catalog.service.proto.GrpcCreateVideoMetadataRequest;
import com.mininetflix.video.catalog.service.repository.VideoMetadataRepository;
import com.mininetflix.video.catalog.service.transformer.VideoMetadataTransformer;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@HandleRpcTask(rpcMethod = "createVideoMetadata")
public class CreateVideoMetadataTask extends MiniNetflixTask {
    private final VideoMetadataRepository videoMetadataRepository;
    private final VideoMetadataTransformer videoMetadataTransformer;

    @Autowired
    public CreateVideoMetadataTask(VideoMetadataRepository videoMetadataRepository) {
        super(CreateVideoMetadataTask.class.getSimpleName());
        this.videoMetadataRepository = videoMetadataRepository;
        this.videoMetadataTransformer = new VideoMetadataTransformer();
    }

    @Override
    protected void exec(DataWrapper dataWrapper) {
        GrpcMsgWrapper<GrpcCreateVideoMetadataRequest, GrpcStringResponse> msgWrapper = dataWrapper.getMsgWrapper(GrpcMsgWrapper.class);
        GrpcCreateVideoMetadataRequest request = msgWrapper.getRequest();
        StreamObserver<GrpcStringResponse> responseObserver = msgWrapper.getResponseObserver();
        GrpcStringResponse.Builder messageBuilder = GrpcStringResponse.newBuilder();

        if (Objects.isNull(request)
            || !AuthKeyCallerValidator.validateAuthKey(request.getAuthKey())
            || !request.hasVideoMetadata()
            || !VideoCatalogHelper.validateVideoMetadata(request.getVideoMetadata())) {
            messageBuilder
                .setErrCode(BackendErrorCode.INVALID_REQUEST)
                .setErrMsg(BackendErrorMessage.INVALID_REQUEST);
            responseObserver.onNext(messageBuilder.build());
            responseObserver.onCompleted();
        }

        try {
            VideoMetadata createMetadata = videoMetadataTransformer.fromGrpcToObject(request.getVideoMetadata());
            VideoMetadata createdMetadata = videoMetadataRepository.save(createMetadata);

            messageBuilder
                .setErrCode(BackendErrorCode.SUCCESS)
                .setErrMsg(BackendErrorMessage.SUCCESS)
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
    }
}
