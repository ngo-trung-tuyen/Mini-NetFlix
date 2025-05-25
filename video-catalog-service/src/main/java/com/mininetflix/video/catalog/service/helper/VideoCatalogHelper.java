package com.mininetflix.video.catalog.service.helper;

import com.mininetflix.backend.common.utils.MiniNetflixCommonUtils;
import com.mininetflix.video.catalog.service.proto.GrpcVideoMetadata;

import java.util.Objects;

public class VideoCatalogHelper {
    public static boolean validateVideoMetadata(GrpcVideoMetadata videoMetadata) {
        return Objects.nonNull(videoMetadata)
            && !MiniNetflixCommonUtils.isNullOrEmpty(videoMetadata.getTitle())
            && !MiniNetflixCommonUtils.isNullOrEmpty(videoMetadata.getDescription())
            && !MiniNetflixCommonUtils.isNullOrEmpty(videoMetadata.getGenre())
            && !MiniNetflixCommonUtils.isNullOrEmpty(videoMetadata.getThumbnailUrl())
            && !MiniNetflixCommonUtils.isNullOrEmpty(videoMetadata.getUrl());
    }

    private VideoCatalogHelper() {
        //empty constructor
    }
}
