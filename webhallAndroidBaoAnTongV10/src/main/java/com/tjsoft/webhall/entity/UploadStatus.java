package com.tjsoft.webhall.entity;

/**
 * Created by lenovo on 2017/2/23.
 */

public enum UploadStatus {
    ADD_ATT_FOR_FILE_UPLOAD,            //添加材料上传通知（通知材料清单列表）
    ADD_ATT_FOR_MATERIAL_MANAGE,    //添加材料上传通知（通知材料编辑页面）
    UPLOADING,          //正在上传
    UPLOAD_SUCCESS,     //上传成功
    UPLOAD_ERROR,       //正在失败
    DELETE_ERROR,       //删除失败
    DELETE_SUCCESS;       //删除成功
}
