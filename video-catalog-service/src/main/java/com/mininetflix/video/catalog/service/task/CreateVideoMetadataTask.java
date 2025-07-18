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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Log4j2
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
        GrpcCreateVideoMetadataRequest req = msgWrapper.getRequest();
        StreamObserver<GrpcStringResponse> resObserver = msgWrapper.getResponseObserver();
        GrpcStringResponse.Builder resBuilder = GrpcStringResponse.newBuilder();

        if (Objects.isNull(req)
            || !AuthKeyCallerValidator.validateAuthKey(req.getAuthKey())
            || !req.hasVideoMetadata()
            || !VideoCatalogHelper.validateVideoMetadata(req.getVideoMetadata())) {
            resBuilder.setErrCode(BackendErrorCode.INVALID_REQUEST)
                .setErrMsg(BackendErrorMessage.INVALID_REQUEST);
            resObserver.onNext(resBuilder.build());
            resObserver.onCompleted();
            return;
        }

        try {
            VideoMetadata createMetadata = videoMetadataTransformer.fromGrpcToObject(req.getVideoMetadata());
            VideoMetadata createdMetadata = videoMetadataRepository.save(createMetadata);

            resBuilder.setErrCode(BackendErrorCode.SUCCESS)
                .setErrMsg(BackendErrorMessage.SUCCESS)
                .setValue(createdMetadata.getVideoId().toString());
            resObserver.onNext(resBuilder.build());
            resObserver.onCompleted();
        } catch (Exception ex) {
            log.error("execError {}", ex.getMessage(), ex);
            resObserver.onError(
                Status.INTERNAL
                    .withDescription(ex.getMessage())
                    .withCause(ex)
                    .asRuntimeException()
            );
        }
    }
}
