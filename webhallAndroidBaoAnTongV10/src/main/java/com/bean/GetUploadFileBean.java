package com.bean;

/**
 * Created by lenovo on 2017/1/25.
 * 文件上传返回
 */

public class GetUploadFileBean {
    private String FILEID;
    private String FILEPATH;

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    private String FILENAME;

    public String getFILEID() {
        return FILEID;
    }

    public void setFILEID(String FILEID) {
        this.FILEID = FILEID;
    }

    public String getFILEPATH() {
        return FILEPATH;
    }

    public void setFILEPATH(String FILEPATH) {
        this.FILEPATH = FILEPATH;
    }
}
