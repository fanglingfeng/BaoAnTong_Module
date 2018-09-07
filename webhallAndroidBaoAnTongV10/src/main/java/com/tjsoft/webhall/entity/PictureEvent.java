package com.tjsoft.webhall.entity;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/1/10.
 */

public class PictureEvent {
    private String fileUri;

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public ArrayList<String> getFileUris() {
        return fileUris;
    }

    public void setFileUris(ArrayList<String> fileUris) {
        this.fileUris = fileUris;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    private ArrayList<String> fileUris;
    private boolean isSingle;
}
