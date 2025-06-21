package com.mininetflix.video.catalog.service.task;

import com.mininetflix.backend.common.annotation.HandleRpcTask;
import com.mininetflix.backend.common.entity.DataWrapper;
import com.mininetflix.backend.common.entity.GrpcMsgWrapper;
import com.mininetflix.backend.common.error.BackendErrorCode;
import com.mininetflix.backend.common.error.BackendErrorMessage;
import com.mininetflix.backend.common.proto.GrpcMultiStringRequest;
import com.mininetflix.backend.common.task.MiniNetflixTask;
import com.mininetflix.backend.common.utils.CommonUtils;
import com.mininetflix.backend.common.validator.AuthKeyCallerValidator;
import com.mininetflix.video.catalog.service.entity.VideoMetadata;
import com.mininetflix.video.catalog.service.proto.GrpcMultiVideoMetadataResponse;
import com.mininetflix.video.catalog.service.proto.GrpcVideoMetadata;
import com.mininetflix.video.catalog.service.repository.VideoMetadataRepository;
import com.mininetflix.video.catalog.service.transformer.VideoMetadataTransformer;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@HandleRpcTask(rpcMethod = "multiGetVideoMetadataById")
public class MultiGetVideoMetadataByIdTask extends MiniNetflixTask {
    private final VideoMetadataRepository videoMetadataRepository;
    private final VideoMetadataTransformer videoMetadataTransformer;

    @Autowired
    public MultiGetVideoMetadataByIdTask(VideoMetadataRepository videoMetadataRepository) {
        super(MultiGetVideoMetadataByIdTask.class.getSimpleName());
        this.videoMetadataRepository = videoMetadataRepository;
        this.videoMetadataTransformer = new VideoMetadataTransformer();
    }

    @Override
    protected void exec(DataWrapper dataWrapper) {
        GrpcMsgWrapper<GrpcMultiStringRequest, GrpcMultiVideoMetadataResponse> grpcMsgWrapper = dataWrapper.getMsgWrapper(GrpcMsgWrapper.class);
        GrpcMultiStringRequest req = grpcMsgWrapper.getRequest();
        StreamObserver<GrpcMultiVideoMetadataResponse> resObserver = grpcMsgWrapper.getResponseObserver();
        GrpcMultiVideoMetadataResponse.Builder resBuilder = GrpcMultiVideoMetadataResponse.newBuilder();

        if (Objects.isNull(req)
            || !AuthKeyCallerValidator.validateAuthKey(req.getAuthKey())
            || CommonUtils.isNullOrEmpty(req.getValueList())) {
            resBuilder.setErrCode(BackendErrorCode.INVALID_REQUEST)
                .setErrMsg(BackendErrorMessage.INVALID_REQUEST);
            resObserver.onNext(resBuilder.build());
            resObserver.onCompleted();
            return;
        }

        try {
            List<UUID> uuids = CommonUtils.getKeyList(req.getValueList(), UUID::fromString);
            List<VideoMetadata> videoMetadataList = videoMetadataRepository.findAllById(uuids);
            if (!CommonUtils.isNullOrEmpty(videoMetadataList)) {
                List<GrpcVideoMetadata> grpcVideoMetadataList = CommonUtils.getKeyList(videoMetadataList, videoMetadataTransformer::fromObjectToGrpc);
                resBuilder.setErrCode(BackendErrorCode.SUCCESS)
                    .setErrMsg(BackendErrorMessage.SUCCESS)
                    .addAllVideoMetadata(grpcVideoMetadataList);
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
