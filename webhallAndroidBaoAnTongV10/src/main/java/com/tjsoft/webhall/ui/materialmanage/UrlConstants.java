package com.tjsoft.webhall.ui.materialmanage;

/**
 * Created by Dino on 9/20 0020.
 */

public class UrlConstants {


    public volatile static String PUSH_HOST = "http://222.143.52.58:6009/cachepushupdatezimaoqu/";//推送服务地址
    public volatile static String CROSS_SERVER_URL = "http://222.143.52.58:6009/cachepushupdatezimaoqu/";//交叉服务地址
    public static String VERSON_BASE = "http://222.143.52.58:6009/cachepushupdatezimaoqu/";//版本跟新系统
//    public static String COLLECT_PRINT_CENTER = "http://collectPrintCenter.huizhoucloud.com/collectPrintCenter/";//收藏打印中心系统
    public static String COLLECT_PRINT_CENTER = "http://collectPrintCenter.huizhoucloud.com/collectPrintCenter/";//收藏打印中心系统
//    public volatile static String DOMAIN = "http://192.9.6.24:8180/";// 郑州自贸区测试
    public volatile static String DOMAIN_IMG = "http://192.9.6.24:8180";// 郑州自贸区拿图片的
    public volatile static String DOMAIN = "http://222.143.52.66:10001/";// 外网测试
//    public volatile static String DOMAIN = "http://10.1.48.100:8080/";// 测试地址
    //http://222.143.52.58:6009/cachepushupdatezimaoqu/a/login;JSESSIONID=a170800b3e38457b8701f8292edd64e5上传新版本的地址

    public static String ZFGB_IP = "http://58.16.65.158/"; // 政府公报地址
    public static final String ZFGBNAMESPACE = "http://tempuri.org/"; // 政府公报
    public static final String ZFGB_WS_URL = ZFGB_IP + "zfgb/";


    public static  final String AchieveNumUrl="http://le17172264.imwork.net:44092/";

    public static final String NAMESPACE = "http://service.rest2.cms.jeecms.com/";
    public static final String WS_URL = DOMAIN + "services/";
    public static final String WS_CBUS_URL = "http://112.91.118.220:8280/";
    public static final String WS_BANK_URL = "http://175.17.148.7:7003/services/";
    public static final String WS_SHARE_URL = DOMAIN + "rest/";
    public static String VERSON_CHECK = VERSON_BASE + "services/appupdate/GetAppDetails/a82d9a260a60465e8229b11571b1bd78";//版本跟新查询

    public static String GET_HOT_TAG = CROSS_SERVER_URL + "services/interAction/hotTags";// 热门搜索标签
    public static String SET_HOT_TAG = CROSS_SERVER_URL + "services/interAction/hotTag/";// 插入热门搜索标签

    public static String BASE_WEATHER_URL = "https://free-api.heweather.com/v5/";

    public static String REQUEST_INFO_KEY = "*MZ#U_&S_in^fo%";

}