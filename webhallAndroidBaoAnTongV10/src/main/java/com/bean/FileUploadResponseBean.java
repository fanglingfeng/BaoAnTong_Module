package com.bean;

/**
 * Created by lenovo on 2017/4/11.
 * 文件上传成功返回的实体类
 */

public class FileUploadResponseBean {
    private String FILEID;
    private String FILENAME;
    private String FILEPATH;

    public String getFILEID() {
        return FILEID;
    }

    public void setFILEID(String FILEID) {
        this.FILEID = FILEID;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getFILEPATH() {
        return FILEPATH;
    }

    public void setFILEPATH(String FILEPATH) {
        this.FILEPATH = FILEPATH;
    }
}
