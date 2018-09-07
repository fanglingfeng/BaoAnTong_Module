package com.tjsoft.webhall.constants;

/**
 * Created by Dino on 9/25 0025.
 */

public enum BusConstant {
    LOGIN_SUCCESS,  //登录成功
    LOGIN_OUT,      //退出登录
    REGET_WEATHER_DATA,  //重新拉取天气通知
    UPDATE_WEATHER,      //更新天气显示通知
    CHANGE_USER_INFO,    //修改用户信息

    //主界面底部栏推送消息：红点提醒
    SHOW_READ_DOT,
    NOT_SHOW_READ_DOT,

    CHECK_FORM_WRONG,//检测基本表单错误
    CHECK_FORM_RIGHT,//检测基本表单正确
    UPDATE,//申报滑动，发出更新消息
    SHOWALERT,//申报滑动，通知基本表单页，是否弹出js的alert
    GOTOBASEFORM,//申报滑动，通知基本表单页，是否弹出js的alert

    AREA_CHANGED,//区划被改变
    PUSH_UPDATA,    //更新推送数据
    FAILED_TO_SAVE_BASE_FORM,   //保存基本表单失败
    UPDATA_ATTS_STUTAS,   //更新材料状态

    UPDATE_USER_DETAIL,    //更新用户详细信息

    UPDATE_CHOOSE_ITEMS,    //更新常用事项各个模块数据
    UPDATE_MYTICKEY_LIST,   //更新我的取号列表
    OPEN_DRAWERLAYOUT,CLOSE_DRAWERLAYOUT,

    UPDATE_MYHEADIMAGE, CHANGE_SKIN, DEPT_CHANGED, AuthSuccess;   //图片上传成功，更新我的信息
}
