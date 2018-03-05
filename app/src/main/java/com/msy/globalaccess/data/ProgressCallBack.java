package com.msy.globalaccess.data;

/**
 * Created by shawn on 2017/5/15 0015.
 * <p>
 * description : 下载进度回调
 */

public interface ProgressCallBack {
    void update(long bytesRead, long contentLength, boolean done);
}
