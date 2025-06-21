package com.mininetflix.video.catalog.service.transformer;

import com.mininetflix.backend.common.transformer.AbstractProtobufTransformer;
import com.mininetflix.backend.common.utils.CommonUtils;
import com.mininetflix.backend.common.utils.FilterUtils;
import com.mininetflix.video.catalog.service.entity.VideoMetadata;
import com.mininetflix.video.catalog.service.proto.GrpcVideoMetadata;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
@NoArgsConstructor
public class VideoMetadataTransformer extends AbstractProtobufTransformer<GrpcVideoMetadata, VideoMetadata> {
    @Override
    public VideoMetadata fromGrpcToObject(GrpcVideoMetadata message) {
        try {
            UUID videoId = CommonUtils.isNullOrEmpty(message.getVideoId())
                ? UUID.randomUUID()
                : UUID.fromString(message.getVideoId());
            return new VideoMetadata()
                .setVideoId(videoId)
                .setTitle(message.getTitle())
                .setDescription(message.getDescription())
                .setAuthor(message.getAuthor())
                .setGenre(message.getGenre())
                .setAuthor(message.getAuthor())
                .setPublisher(message.getPublisher())
                .setThumbnailUrl(message.getThumbnailUrl())
                .setUrl(message.getUrl())
                .setCreatedTime(message.getCreatedTime())
                .setUpdatedTime(message.getUpdatedTime())
                .setShowingFromTime(message.getShowingFromTime())
                .setShowingToTime(message.getShowingToTime());
        } catch (Exception ex) {
            log.error("fromGrpcToObject error {}", ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public GrpcVideoMetadata fromObjectToGrpc(VideoMetadata metadata) {
        return GrpcVideoMetadata.newBuilder()
            .setVideoId(FilterUtils.getOrDefault(metadata.getVideoId().toString(), ""))
            .setTitle(FilterUtils.getOrDefault(metadata.getTitle(), ""))
            .setDescription(FilterUtils.getOrDefault(metadata.getDescription(), ""))
            .setAuthor(FilterUtils.getOrDefault(metadata.getAuthor(), ""))
            .setPublisher(FilterUtils.getOrDefault(metadata.getPublisher(), ""))
            .setGenre(FilterUtils.getOrDefault(metadata.getGenre(), ""))
            .setThumbnailUrl(FilterUtils.getOrDefault(metadata.getThumbnailUrl(), ""))
            .setUrl(FilterUtils.getOrDefault(metadata.getUrl(), ""))
            .setCreatedTime(metadata.getCreatedTime())
            .setUpdatedTime(metadata.getUpdatedTime())
            .setShowingFromTime(metadata.getShowingFromTime())
            .setShowingToTime(metadata.getShowingToTime())
            .build();
    }
}
