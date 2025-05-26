package com.mininetflix.video.catalog.service.helper;

import com.mininetflix.backend.common.utils.CommonUtils;
import com.mininetflix.video.catalog.service.proto.GrpcVideoMetadata;

import java.util.Objects;

public class VideoCatalogHelper {
    public static boolean validateVideoMetadata(GrpcVideoMetadata videoMetadata) {
        return Objects.nonNull(videoMetadata)
            && !CommonUtils.isNullOrEmpty(videoMetadata.getTitle())
            && !CommonUtils.isNullOrEmpty(videoMetadata.getDescription())
            && !CommonUtils.isNullOrEmpty(videoMetadata.getGenre())
            && !CommonUtils.isNullOrEmpty(videoMetadata.getThumbnailUrl())
            && !CommonUtils.isNullOrEmpty(videoMetadata.getUrl());
    }

    private VideoCatalogHelper() {
        //empty constructor
    }
}
