package com.tjsoft.webhall.entity;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 传输实体类
 *
 * @author Administrator
 */
public class TransportEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -138849922532L;

    private String userId; //目前是手机号
    private String name; // 用户账号
    private String password;//密码  MD5
    private String loginPhone; // 电话
    private boolean isAdmin; //是否管理员
    private boolean realUserAuth;//是否实名
    private String enterpriseStatus;//企业服务开通状态 -1已驳回0未开通1已提交2待审核3已开通（审核通过）
    private String idcardType;//证件类型 "10 身份证", "11 军官证", "12 士兵证", "13 警官证", "14 港澳居民来往内地通行证", "15 台湾居民来往大陆通行证", "16 香港身份证", "17 澳门身份证", "18 台湾身份证", "20 护照"
    private String idcardNum;//证件号码
    private String token;//令牌
    private String userType;//用户类型  1.个人,2企业
    private String realName;//真实姓名
    private String Code;//证件号码或者组织机构代码
    private byte sex;//1 男 0 女
    private String INC_NAME;//企业名称
    private String INC_TYPE;//企业类型，1国有，2民营，3外资，4港澳台资，5其他
    private String INC_PERMIT;//营业执照
    private String INC_ZZJGDM;//组织机构代码
    private String TYSHXYDM;//统一社会信用代码（统一社会信用代码、营业执照、组织机构代码必须填写其中一个）
    private String INC_DEPUTY;//法人代表
    private String INC_PID;//法人身份证号码
    private String AGE_NAME;//经办人姓名
    private String AGE_PID;//经办人身份证
    private String AGE_MOBILE;//经办人移动电话
    private String INC_ADDR;//企业地址
    private String INC_INDICIA;//企业邮编
    private String EMAIL;//邮箱
    private byte userChannel;//用户注册渠道 1.宝安通 2.办事大厅 3…..
    private int version;

    public TransportEntity() {
    }

    public String getUserId() {
        return userId;
    }

    //	public String getAUTHLEVEL() {
//		return AUTHLEVEL;
//	}
//
//	public void setAUTHLEVEL(String AUTHLEVEL) {
//		this.AUTHLEVEL = AUTHLEVEL;
//	}
//
//	private String AUTHLEVEL;//认证级别（1密码级(初级)，2实名级别(中级)，3实人级别(高级1)，4CA级别(高级2)）"
    public TransportEntity(String userId, String name, String password, String loginPhone, boolean isAdmin,
                           boolean realUserAuth, String enterpriseStatus, String idcardType, String idcardNum, String token,
                           String userType, String realName, String code, byte sex, String iNC_NAME, String iNC_TYPE, String iNC_PERMIT,
                           String iNC_ZZJGDM, String tYSHXYDM, String iNC_DEPUTY, String iNC_PID, String iNC_ADDR, String iNC_INDICIA,
                           String aGE_NAME, String aGE_PID, String aGE_MOBILE, String eMAIL, byte userChannel) {
        super();
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.loginPhone = loginPhone;
        this.isAdmin = isAdmin;
        this.realUserAuth = realUserAuth;
        this.enterpriseStatus = enterpriseStatus;
        this.idcardType = idcardType;
        this.idcardNum = idcardNum;
        this.token = token;
        this.userType = userType;
        this.realName = realName;
        Code = code;
        this.sex = sex;
        INC_NAME = iNC_NAME;
        INC_TYPE = iNC_TYPE;
        INC_PERMIT = iNC_PERMIT;
        INC_ZZJGDM = iNC_ZZJGDM;
        TYSHXYDM = tYSHXYDM;
        INC_DEPUTY = iNC_DEPUTY;
        INC_PID = iNC_PID;
        INC_ADDR = iNC_ADDR;
        INC_INDICIA = iNC_INDICIA;
        AGE_NAME = aGE_NAME;
        AGE_PID = aGE_PID;
        AGE_MOBILE = aGE_MOBILE;
        EMAIL = eMAIL;
        this.userChannel = userChannel;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginPhone() {
        return loginPhone;
    }

    public void setLoginPhone(String loginPhone) {
        this.loginPhone = loginPhone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isRealUserAuth() {
        return realUserAuth;
    }

    public void setRealUserAuth(boolean realUserAuth) {
        this.realUserAuth = realUserAuth;
    }

    public String getEnterpriseStatus() {
        return enterpriseStatus;
    }

    public void setEnterpriseStatus(String enterpriseStatus) {
        this.enterpriseStatus = enterpriseStatus;
    }

    public String getIdcardType() {
        return idcardType;
    }

    public void setIdcardType(String idcardType) {
        this.idcardType = idcardType;
    }

    public String getIdcardNum() {
        return idcardNum;
    }

    public void setIdcardNum(String idcardNum) {
        this.idcardNum = idcardNum;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realname) {
        this.realName = realname;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getINC_NAME() {
        return INC_NAME;
    }

    public void setINC_NAME(String iNC_NAME) {
        INC_NAME = iNC_NAME;
    }

    public String getTYSHXYDM() {
        return TYSHXYDM;
    }

    public void setTYSHXYDM(String tYSHXYDM) {
        TYSHXYDM = tYSHXYDM;
    }

    public String getINC_DEPUTY() {
        return INC_DEPUTY;
    }

    public void setINC_DEPUTY(String iNC_DEPUTY) {
        INC_DEPUTY = iNC_DEPUTY;
    }

    public String getINC_PID() {
        return INC_PID;
    }

    public void setINC_PID(String iNC_PID) {
        INC_PID = iNC_PID;
    }

    public String getAGE_NAME() {
        return AGE_NAME;
    }

    public void setAGE_NAME(String aGE_NAME) {
        AGE_NAME = aGE_NAME;
    }

    public String getAGE_PID() {
        return AGE_PID;
    }

    public void setAGE_PID(String aGE_PID) {
        AGE_PID = aGE_PID;
    }

    public String getAGE_MOBILE() {
        return AGE_MOBILE;
    }

    public void setAGE_MOBILE(String aGE_MOBILE) {
        AGE_MOBILE = aGE_MOBILE;
    }

    public byte getUserChannel() {
        return userChannel;
    }

    public void setUserChannel(byte userChannel) {
        this.userChannel = userChannel;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String eMAIL) {
        EMAIL = eMAIL;
    }

    public String getINC_TYPE() {
        return INC_TYPE;
    }

    public void setINC_TYPE(String iNC_TYPE) {
        INC_TYPE = iNC_TYPE;
    }

    public String getINC_PERMIT() {
        return INC_PERMIT;
    }

    public void setINC_PERMIT(String iNC_PERMIT) {
        INC_PERMIT = iNC_PERMIT;
    }

    public String getINC_ADDR() {
        return INC_ADDR;
    }

    public void setINC_ADDR(String iNC_ADDR) {
        INC_ADDR = iNC_ADDR;
    }

    public String getINC_INDICIA() {
        return INC_INDICIA;
    }

    public void setINC_INDICIA(String iNC_INDICIA) {
        INC_INDICIA = iNC_INDICIA;
    }

    public String getINC_ZZJGDM() {
        return INC_ZZJGDM;
    }

    public void setINC_ZZJGDM(String iNC_ZZJGDM) {
        INC_ZZJGDM = iNC_ZZJGDM;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        Gson gs = new Gson();
        return gs.toJson(this).toString();
    }
}
