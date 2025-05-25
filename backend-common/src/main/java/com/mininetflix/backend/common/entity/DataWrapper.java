package com.mininetflix.backend.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DataWrapper {
    private int status;
    private String statusMessage;
    private MiniNetflixMessage message;
}
