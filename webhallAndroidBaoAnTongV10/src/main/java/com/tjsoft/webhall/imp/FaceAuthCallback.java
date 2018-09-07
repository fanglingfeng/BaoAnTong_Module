package com.tjsoft.webhall.imp;

public interface FaceAuthCallback {

    public void onSuccess(String o);

    public void onFailure(int errCode, String errMsg, Throwable t);

}
