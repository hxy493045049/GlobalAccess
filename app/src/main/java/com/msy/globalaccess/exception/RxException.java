package com.msy.globalaccess.exception;

import com.msy.globalaccess.common.enums.ResultCode;

/**
 * Created by hxy on 2017/2/15.
 * class description: 使用了这个exception 表示在rx的转换过程中,已经处理过error的回调了,subscriber不需要另外处理
 */

public class RxException extends Exception {

    private String errorMessage;
    @ResultCode
    private int mResultCode;

    public RxException(@ResultCode int resultCode, String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        mResultCode = resultCode;

    }
    @ResultCode
    public int getResultCode() {
        return mResultCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
