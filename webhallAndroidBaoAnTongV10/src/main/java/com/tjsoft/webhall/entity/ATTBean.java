package com.tjsoft.webhall.entity;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.tjsoft.msfw.guangdongshenzhenbaoan.BR;

import java.io.File;
import java.io.Serializable;

import static com.tjsoft.webhall.entity.UploadStatus.UPLOAD_SUCCESS;


public class ATTBean extends BaseObservable implements Serializable {
    private boolean isCheck = false;//是否选中
    private boolean isEdit = false;//是否编辑中
    private int managerPosition;//当前列表ID
    private String ID;//文件id
    private String TYPE;//类型
    private String ATTACHNAME;//材料名称
    private String ATTACHURL;   //mogo
    private String localPath;   //mogo
    private String ATTACHUID;//

    // 上传状态：正在上传、上传成功、正在失败
    private UploadStatus upStatus = UPLOAD_SUCCESS;

    private boolean isDeleting = false;//是否正在删除
    private double progress = 0;

    public UploadStatus getUpStatus() {
        return upStatus;
    }

    public void setUpStatus(UploadStatus upStatus) {
        this.upStatus = upStatus;
    }

    public int getManagerPosition() {
        return this.managerPosition;
    }

    public void setManagerPosition(int managerPosition) {
        this.managerPosition = managerPosition;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    @Bindable
    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        notifyPropertyChanged(BR.attBean);
    }

    public ATTBean() {
        super();
    }

    public ATTBean(String iD, String aTTACHNAME, String aTTACHURL, String aTTACHUID) {
        super();
        ID = iD;
        ATTACHNAME = aTTACHNAME;
        ATTACHURL = aTTACHURL;
        ATTACHUID = aTTACHUID;
    }

    public ATTBean(String localPath) {
        super();
        this.localPath = localPath;
    }

    public ATTBean(String iD, String aTTACHNAME, String aTTACHURL, String aTTACHUID, File file) {
        super();
        ID = iD;
        ATTACHNAME = aTTACHNAME;
        ATTACHURL = aTTACHURL;
        ATTACHUID = aTTACHUID;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getATTACHUID() {
        return ATTACHUID;
    }

    public void setATTACHUID(String aTTACHUID) {
        ATTACHUID = aTTACHUID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getATTACHNAME() {
        return ATTACHNAME;
    }

    public void setATTACHNAME(String aTTACHNAME) {
        ATTACHNAME = aTTACHNAME;
    }

    public String getATTACHURL() {
        return ATTACHURL;
    }

    public void setATTACHURL(String aTTACHURL) {
        ATTACHURL = aTTACHURL;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    public boolean isDeleting() {
        return isDeleting;
    }

    public void setDeleting(boolean deleting) {
        isDeleting = deleting;
    }
}
