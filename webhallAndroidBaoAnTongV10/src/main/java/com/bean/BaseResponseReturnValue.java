package com.bean;

/**
 * Created by Dino on 11/3 0003.
 */

public class BaseResponseReturnValue<T> {
    private T ReturnValue;

    /**
     * 返回说明
     */
    private String error;

    private int code;


    /**
     * 数据是否过期
     */
    private boolean upToDate;



    public boolean isUpToDate() {
        return upToDate;
    }

    public void setUpToDate(boolean upToDate) {
        this.upToDate = upToDate;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getReturnValue() {
        return ReturnValue;
    }

    public void setReturnValue(T returnValue) {
        ReturnValue = returnValue;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
