package com.mininetflix.backend.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DataWrapper {
    private int status;
    private String statusMsg = "";
    private MessageWrapper msgWrapper;

    public <T extends MessageWrapper> T getMsgWrapper(Class<T> clazz) {
        return clazz.cast(msgWrapper);
    }
}
