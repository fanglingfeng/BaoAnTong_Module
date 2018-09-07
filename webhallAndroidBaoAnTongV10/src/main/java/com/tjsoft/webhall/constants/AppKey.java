package com.tjsoft.webhall.constants;

/**
 * Created by lianghuiyong@outlook.com
 * Date on 2016/9/23
 */

public class AppKey {
    public static final String DEPTNAME = "DEPTNAME";
    public static  String AREAID = "AREAID";
    public static String AREANAME = "AREANAME";
    public static String RESERVEONE = "RESERVEONE";
    public static String SELECED_PARENT_AREAID = "SELECED_PARENT_AREAID";
    public static String SELECED_PARENT_AREANAME = "SELECED_PARENT_AREANAME";
    public static String ISFIRSTRUN = "ISFIRSTRUN";
    public static String ISREGISTERPUSH = "ISREGISTERPUSH";

    //二维码标题栏
    public static String SCANTITLE = "SCANTITLE";
    //二维码扫描结果
    public static int SCAN_ERROR        = 99;
    public static int SCAN_SUCCESS      = 100;
    public static String SCAN_TYPE      = "SCAN_TYPE";
    public static String SCAN_RESULT    = "SCAN_RESULT";

    //跳转到收藏详情传递的key
    public static String BUSINESSBEAN = "BUSINESSBEAN";
    //收藏存储记录
    public static String COLLECTION_RECORDS = "COLLECTION_RECORDS";
    //申报跳到我的办件
    public static String PATH = "PATH";

    //是否不再显示申报须知界面
    public static String SHOW_GUIDE = "SHOW_GUIDE";//share文件名
    public static String IS_SHOW_GUIDE = "IS_SHOW_GUIDE";//状态key名

    //机构导航列表跳转到网点详情传递对象的key
    public static String NETWORK_BEAN = "NETWORK_BEAN";
    //网点详情跳转到地图预览界面经纬度的key名
    public static String LATITUDE = "LATITUDE";
    public static String LONGITUDE = "LONGITUDE";
    //办事指南跳转到登录界面传递的key
    public static String ISNEEDIDENTITY = "ISNEEDIDENTITY";
    //办事指南跳转到个人信息完善界面传递的key
    public static String START_ISREGISTER = "START_ISREGISTER";
    //传递Permission业务bean的key
    public static String PERMISSION = "permission";
    public static String PERMISSIONID = "PERMID";
    public static String FROM_FLAG = "fromFlag";//跳转到办事指南传递的key，用来显示隐藏底部按钮
    public static String PERMGROUP = "PERMGROUP";

    /******************************** todo 推送Start **************************************/
    //推送信息存储
    public static String REG_PUSH_APPID = "REG_PUSH_APPID";                   //appid
    public static String REG_PUSH_USERID = "REG_PUSH_USERID";                  //userid
    public static String REG_PUSH_CHANNELID = "REG_PUSH_CHANNELID";               //channelId
    public static String REG_PUSH_BINDUSERID = "REG_PUSH_BINDUSERID";              //推送绑定的id
    /******************************** todo 推送Start **************************************/
    //办事指南跳转到申报
    public static String STATUS = "STATUS";//申报状态key
    public static String BSNUM = "BSNUM";//业务流水号key
    public static String P_GROUP_ID = "P_GROUP_ID";//分组ID key
    public static String P_GROUP_NAME = "P_GROUP_NAME";//分组名称key
    public static String mark = "mark";//传递的标志key，最终传递到材料编辑页面，但没用上
    public static String APPLY_PID_FILEID = "APPLY_PID_FILEID";//申请人照片id
    public static String APPLY_PID_FILEID_TIME = "APPLY_PID_FILEID_TIME";//申请人照片id
    public static String APPLY_PID_FILENAME = "申请人照片.jpg";
    public static final String COOPERATE_ID="COOPERATE_ID";//协同项目组ID
    public static final String COOPERATE_CBUSINESSID="COOPERATE_CBUSINESSID";//协同事项业务流水号
    public static final String COOPERATE_NAME="COOPERATE_NAME";//协同项目组名称
    public static final String COOPERATE_BEAN = "COOPERATE_BEAN";//协同项目组ID
    public static final String APPLICANT = "APPLICANT";//协同项目申请人
    public static final String COOPERATE_TASKIDS = "COOPERATE_TASKIDS";//协同申报补交件的事项任务id，传给材料上传用的
    public static String INTENT_BEAN = "INTENT_BEAN";
    public static String INTENT_BUNDLE = "INTENT_BUNDLE";//传递bundle的标志key
    //跳转到事项列表传递的key
    public static String SFYDSB = "SFYDSB";//是否能在移动端申报 key
    public static String SORTCODE = "SORTCODE";//分类编号key
    public static String name = "name";//事项名称 key
    public static String flag = "flag";//传递的标识 key
    public static String fromYuyue = "fromYuyue";//是否是预约
    public static String DEPTID = "DEPTID";//部门id key
    public static String NETWORK_DETAIL_ID = "NETWORK_DETAIL_ID";
    public static String dept = "dept";//部门对象 key
    public static String url = "url";//跳转网页的key
    public static String title = "title";//跳转网页的显示的标题
    //intent传递值通用key
    public static String INTENT_KEY = "INTENT_KEY";

    public static String WEIXIN_INFO = "WEIXIN_INFO";
    public static String IS_WEIXIN = "IS_WEIXIN";

    //选择事项
    public static final int ENTER_ONLINE_BUSINESS = 2;//企业办事
    public static final int PERSON_ONLINE_BUSINESS = 1;//个人办事
    public static final int EXAMIN_ONLINE_BUSINESS = 5;//行政审批
    public static final int COMMON_SERVICE_ONLINE_BUSINESS = 6;//公共服务
    public static final int SUB_ONLINE_BUSINESS = 3;//我要预约
    public static final int LPD =7 ;//零跑动服务

    public static final int PERSON_AND_ENTER_BUSINESS = 4;//只要企业和个人，不要部门
    public static final int SUB_ONLINE_BUSINESS_COOP = 5;//个人,企业,部门,协同
    public static final int[] ONLINE_BUSINESS = {ENTER_ONLINE_BUSINESS, PERSON_ONLINE_BUSINESS, SUB_ONLINE_BUSINESS};

    //材料上传跳转到拍照
    public static final int FROM_FILE_UPLOAD = 1;//来自材料列表
    public static final int FROM_MATERIAL_MANAGE = 2;//来自材料编辑
    //开封选择事项
    public static final String REGISTER_FILLIN_BUISINESS = "05";//注册登记
    public static final String INVEST_BUSINESS = "06";//投资建设
    public static final String COMPANY_MANAGE_BUSINESS = "7";//企业经营
    public static final String SINGLE_WINDOW_BUSINESS = "8";//单一敞口
    //材料列表跳转到材料编辑
    public static String applyBean = "applyBean";//事项业务bean
    public static String position = "position";//索引
    public static String TYPE = "TYPE";//类型
    public static String from = "from";

    //办件跳转相关
    public static String BUSINESS = "business";//传递事项业务bean时的key
    public static String APPNAME = "APPNAME";//办件跳转到进度查询时的key

    //评价列表跳转相关
    public static String ADVICE = "advice";//事项业务类key

    //修改个人信息相关
    public static String EDIT_INFO_TYPE = "editInfoType";

    //咨询跳转相关
    public static String CONSULT = "CONSULT";//咨询业务类key
    //投诉跳转相关
    public static String COMPLAINT = "COMPLAINT";//投诉业务类key

    //企业身份信息完善
    public static String COMPANY_REGISTER_SEND_BEAN = "COMPANY_REGISTER_SEND_BEAN";
    public static String IS_REGISTER = "IS_REGISTER";

    public static String IS_NEED_IDENTITY = "IS_NEED_IDENTITY";//是否需要完善身份信息
    //个人注册
    public static String PERSONAL_REGISTER_SEND_NEAN = "PERSONAL_REGISTER_SEND_NEAN";

    //新闻
    public static String CONTENT_ID = "CONTENT_ID";
    public static String CHANNEL_ID = "CHANNEL_ID";

    //扫描结果
    public static String RESULT = "RESULT";

    //跳转到认证清单传递的业务bean
    public static String AUTH_IDCARD_SEND_BEAN = "AUTH_IDCARD_SEND_BEAN";

    //支付标题
    public static String PAY_TITLE = "PAY_TITLE";

    //周口便民服务跳转到webview传递的flag
    public static String BIANMIN_ITEM = "BIANMIN_ITEM";

    public static final int FROM_NETWORK_DETAIL = 0;
    public static final int FROM_DEPARTMENT_LIST = 1;

    public static String CURRENT_POSITION = "CURRENT_POSITION";//当前选中的哪个tab

    public static String SEARCH_LIST_INTENT_BEAN = "SEARCH_LIST_INTENT_BEAN";//查询办理进度传递的列表key

    //消息
    public static String PUSH_BEAN = "PUSH_BEAN";

    public static String ACHIEVE_NUM_BUSINESSID="ACHIEVE_NUM_BUSINESSID";
    public static String ACHIEVE_NUM_BRANCHBEAN="ACHIEVE_NUM_BRANCHBEAN";
    public static String MYACHIEVEBEAN="MYACHIEVEBEAN";
    public static String ACHIEVE_NUM_CARD="ACHIEVE_NUM_CARD";
    public static String ACHIEVE_TICKET="ACHIEVE_TICKET";
    public static String ACHIEVE_STATE="ACHIEVE_STATE";
    public static String PERMGROUPLIST="PERMGROUPLIST";
    public static String DOMAIN="DOMAIN";
}
