package com.tjsoft.webhall.constants;

import android.app.Application;
import android.util.DisplayMetrics;

import com.tjsoft.webhall.entity.ATTBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lianghuiyong on 2016/7/8.
 * App 启动初始化
 */
public class AppConfig extends Application {
    public static String DOMAIN = "";

    public static String AREAID = "410100000000";//全局区域id----这个是郑州自贸区的
    public static String AREANAME = "郑州自贸区";//全局区域名称
    public static String NEWS_CHANNEL_ID = "12735";//全局新闻channel_id
    public static String RESERVEONE = "4";//机构性质[1国家级 ; 2省级 ;3地市级; 4区县级 ;5乡镇级; 6社区级]
    public static int CHOOSEACTIVITYNUM = 0; //全局打开选择区域的activity数量

    public static Map<String, List<ATTBean>> material = new HashMap<String, List<ATTBean>>();//上传文件缓存

    public static List<String> countyAreaIdList;//区级id列表（部门列表分级显示判断时用到）

    public static int VERSIONCODE = 6;
    public static String APK_NAME = "wangshangbanshi.apk";
    /**
     * 应用文件缓存地址
     */
    public static String CACHE_DIR = "tjsoft/cache";
    public static final boolean CHECK_UPLOAD_FILE = true;

    /**
     * 是否开启cbus
     */
    public static boolean iscBus = false;

    /**
     * 是否开启空间共享
     */
    public static boolean isShare = false;

    /**
     * 是否开启银行
     */
    public static boolean isBankServiceOpen = false;

    /**
     * 是否打开网络收藏（办事指南目前只宝安、仲恺分支开放）
     */
    public static boolean isOpenOnlineCollection = true;


    private DisplayMetrics displayMetrics = null;

    public static String PARENT_AREA_NAME = "";//全局省级以下市级城市默认长春
    public static String PARENT_DEFAULT_AREA_NAME = "郑州自贸区";//默认长春（默认长春天气）
    public static String PARENT_DEFAULT_CITY_ID = "101180101";//全局默认的cityid（默认长春天气）
    public static String WEATHER_CACHE_KEY = "weatherjson";//全局天气缓存key值

    public static long fileSize;//更新包大小

    public static String WEATHER_KEY = "282f3846df6b41178e4a2218ae083ea7";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//
//        Bugly.init(this, "2f5d7c28cf", !BuildConfig.DEBUG);
//
//        //科大讯飞初始化
//        // 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
//        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
//        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
//        // 参数间使用“,”分隔。
//        // 设置你申请的应用appid
//        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5858b2b1");
//        UMShareAPI.get(this);
    }


    private static AppConfig instance;

    public static AppConfig getInstance() {
        return instance;
    }

}