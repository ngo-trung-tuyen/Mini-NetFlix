package com.mininetflix.video.catalog.service.task;

import com.mininetflix.backend.common.annotation.HandleRpcTask;
import com.mininetflix.backend.common.entity.DataWrapper;
import com.mininetflix.backend.common.entity.GrpcMsgWrapper;
import com.mininetflix.backend.common.error.BackendErrorCode;
import com.mininetflix.backend.common.error.BackendErrorMessage;
import com.mininetflix.backend.common.proto.GrpcStringRequest;
import com.mininetflix.backend.common.task.MiniNetflixTask;
import com.mininetflix.backend.common.utils.CommonUtils;
import com.mininetflix.backend.common.validator.AuthKeyCallerValidator;
import com.mininetflix.video.catalog.service.entity.VideoMetadata;
import com.mininetflix.video.catalog.service.proto.GrpcVideoMetadata;
import com.mininetflix.video.catalog.service.proto.GrpcVideoMetadataResponse;
import com.mininetflix.video.catalog.service.repository.VideoMetadataRepository;
import com.mininetflix.video.catalog.service.transformer.VideoMetadataTransformer;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@HandleRpcTask(rpcMethod = "getVideoMetadataById")
public class GetVideoMetadataByIdTask extends MiniNetflixTask {
    private final VideoMetadataRepository videoMetadataRepository;
    private final VideoMetadataTransformer videoMetadataTransformer;

    @Autowired
    public GetVideoMetadataByIdTask(VideoMetadataRepository videoMetadataRepository) {
        super(GetVideoMetadataByIdTask.class.getSimpleName());
        this.videoMetadataRepository = videoMetadataRepository;
        this.videoMetadataTransformer = new VideoMetadataTransformer();
    }

    @Override
    protected void exec(DataWrapper dataWrapper) {
        GrpcMsgWrapper<GrpcStringRequest, GrpcVideoMetadataResponse> grpcMsgWrapper = dataWrapper.getMsgWrapper(GrpcMsgWrapper.class);
        GrpcStringRequest req = grpcMsgWrapper.getRequest();
        StreamObserver<GrpcVideoMetadataResponse> resObserver = grpcMsgWrapper.getResponseObserver();
        GrpcVideoMetadataResponse.Builder resBuilder = GrpcVideoMetadataResponse.newBuilder();

        if (Objects.isNull(req)
            || !AuthKeyCallerValidator.validateAuthKey(req.getAuthKey())
            || CommonUtils.isNullOrEmpty(req.getValue())) {
            resBuilder.setErrCode(BackendErrorCode.INVALID_REQUEST)
                .setErrMsg(BackendErrorMessage.INVALID_REQUEST);
            resObserver.onNext(resBuilder.build());
            resObserver.onCompleted();
            return;
        }

        try {
            Optional<VideoMetadata> optionalVideoMetadata = videoMetadataRepository.findById(UUID.fromString(req.getValue()));
            if (optionalVideoMetadata.isPresent()) {
                VideoMetadata videoMetadata = optionalVideoMetadata.get();
                GrpcVideoMetadata grpcVideoMetadata = videoMetadataTransformer.fromObjectToGrpc(videoMetadata);
                resBuilder.setErrCode(BackendErrorCode.SUCCESS)
                    .setErrMsg(BackendErrorMessage.SUCCESS)
                    .setVideoMetadata(grpcVideoMetadata);
            } else {
                resBuilder.setErrCode(BackendErrorCode.NOT_FOUND);
            }

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
