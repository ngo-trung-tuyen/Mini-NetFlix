package com.mininetflix.backend.common.task;

import com.mininetflix.backend.common.entity.DataWrapper;

public abstract class MiniNetflixTask {
    protected String taskName;

    protected MiniNetflixTask(String taskName) {
        this.taskName = taskName;
    }

    protected abstract void exec(DataWrapper dataWrapper);

    public void run(DataWrapper dataWrapper) {
        Thread.ofVirtual()
            .name(taskName + " >> executing")
            .start(() -> exec(dataWrapper));
    }
}
