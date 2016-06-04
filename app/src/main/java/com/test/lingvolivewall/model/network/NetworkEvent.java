package com.test.lingvolivewall.model.network;

/**
 * Created by Maxim Blumental on 6/4/2016.
 * bvmaks@gmail.com
 */
public enum NetworkEvent {
    SUCCESS, FAILURE;

    private Throwable throwable;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
