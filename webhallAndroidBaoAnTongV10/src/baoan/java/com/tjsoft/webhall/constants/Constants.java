package com.tjsoft.webhall.constants;

import android.app.Activity;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.PostInfo;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.AddInfoCallBack;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.imp.TransportCallBack;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Constants {

	
	public static PostInfo getPostInfo;// 领取信息
	public static PostInfo sendPostInfo;// 发送信息
	public static Constants instance = new Constants();// 应用实例
	public static User user;// 用户信息
	public static String AREAID = "440306";// 全局区域id
	public static String AREANAME = "";// 全局区域名称
	
	public static Map<String, List<ATTBean>> material = new LinkedHashMap<String, List<ATTBean>>();// 上传文件缓存
	public static Map<String, List<ATTBean>> materialXkz = new LinkedHashMap<String, List<ATTBean>>();// 许可证
//	public static Map<String, List<ATTBean>> materials = new LinkedHashMap<String, List<ATTBean>>();// 上传文件缓存
	// public static SlidingMenu menu;
	private static List<Activity> activityList = new LinkedList<Activity>();// 应用程序所有acitivity
	public static List<Activity> areaActivityList = new LinkedList<Activity>();// 区域选择activity

	
	public static final boolean DEBUG_TOGGLE = false; // 调试开关 true表示调试
	public static final boolean CHECK_UPLOAD_FILE = true;

	public static final String ADD_INFO = "完善个人信息";
	public static final String AUTO_LOGIN = "自动绑定";
	public static final boolean isQiebian = true;//是否开启切边
    /**记录用户点击操作
     * 0：查看办事指南
     * 1：网上申报
     * 2：网上预约
     * 
     * */
	public static  int WSBS_PATH = 0;



//	public static String PORT = "8090"; // 宝安
//	public static String IP = "http://bsdt.baoan.gov.cn";// 宝安

	public static String IP="http://203.91.37.98";//外网
	public static String PORT="8083";//外网地址

//	public static String IP = "http://10.1.48.211";//测试
//	public static String PORT="8080";//测试

//	public static String IP="http://10.99.76.8";//内网。


//	public static String IP="http://10.99.76.9";//测试
//	public static String PORT="8083";//测试
//	public static String IP="http://10.1.48.204";//李
//	public static String PORT="8087";//李
	public static String DOMAIN = IP + ":" + PORT + "/";
//	public static String DOMAIN = "http://ws.gov.huizhoucloud.com/";//仲恺手机办事大厅

	public static final String NAMESPACE = "http://service.rest2.cms.jeecms.com/";
	public static final String WS_URL = DOMAIN + "services/";
	public static final String WS_CBUS_URL = "http://192.9.207.151:8000/services/";
	public static final String WS_SHARE_URL = DOMAIN + "rest/";
	public static String REQUEST_INFO_KEY = "*MZ#U_&S_in^fo%";
	/** 应用文件缓存地址 */
	public static String CACHE_DIR = "tjsoft/cache";
	/** 是否开启空间共享 */
	public static boolean isShare = false;//因为开启了一身份证对应多用户，关闭空间共享。
	/** 是否开启cbus */
	public static boolean iscBus = false;
	public static String XKZshili1;
	public static String XKZshili2;

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 3).denyCacheImageMultipleSizesInMemory() // 设置不将大图片数据缓存到memory
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		ImageLoader.getInstance().init(config);
	}

	public static Constants getInstance() {
		return instance;
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 退出软件
	 */
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
//		System.exit(0);
	}

	public GloabDelegete gloabDelegete ;

	public GloabDelegete getGloabDelegete() {
//	这个是测试的时候打开这个，用万能密码登录了的，打包的时候注释掉下面这个

//		GloabDelegete gloabDelegete=new GloabDelegete() {
//
//			@Override
//			public void startStatistics() {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void modifyPWD(String todoAcountName, String todoAccountPwd) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void loginMST(Context context) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void login(String user, String pwd) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public TransportEntity getUserInfo() {
////
//				TransportEntity transportEntity=new TransportEntity();
//				String password= Md5PwdEncoder.encodePassword("Ba123456");
//				Byte sex='1';
//				transportEntity.setUserId("16560c0cd57d4e2a941f364489086c2a");
//				transportEntity.setName("211bb9d3fe2c4de58812a486dbfd9254");
////				transportEntity.setName("baotong_20180326176");
////				transportEntity.setRealName("测试121");
//				transportEntity.setRealName("测试人员2");
//				transportEntity.setPassword(password);
//				transportEntity.setLoginPhone("13600000006");
////				transportEntity.setToken("123456hefade");
//				transportEntity.setToken("c45fe1d7dcb28fa6204d87dccf57ed79");
//				transportEntity.setIdcardType("1");
////				transportEntity.setIdcardNum("350524166603041596");
//				transportEntity.setIdcardNum("220881197103034671");
//				transportEntity.setEnterpriseStatus("3");
//				transportEntity.setUserType("2");
//				transportEntity.setRealUserAuth(false);
//
//				transportEntity.setINC_NAME("企业名字3");
//				transportEntity.setINC_TYPE("2");
//				transportEntity.setINC_PERMIT("48976114647");
//				transportEntity.setINC_ZZJGDM("123456789");
//				transportEntity.setTYSHXYDM("null");
//				transportEntity.setINC_DEPUTY("测试企业3");
//				transportEntity.setINC_PID("350524132204051274");
//				transportEntity.setVersion(1);
//				return transportEntity;
//			}
//
//			@Override
//			public void endStatistics(String ParentName, String CategoryName, String ModelName) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void doBoundMSTAccount(Context context, String todoAcountName, String todoAccountPwd,
//					TransportCallBack transportCallBack) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void doActivity(Context context, int tag, TransportCallBack callback) {
//				// TODO Auto-generated method stub
//			}
//
////			/**
////			 * @param context
////			 * @param name
////			 * @param id
////			 * @param token
////			 * @param callback
////			 */
////			@Override
////			public void faceRecognition(Context context, String name, String id, String token, DefaultActionListener<String> callback) {
////
////			}
//
//
//
//			@Override
//			public void addInfo(Activity activity, AddInfoCallBack back) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void Registered(String strName, String strNewpwd, String strMobile) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void contactCustomer(Activity activity, String modelName, String eventName, String departName) {
//				// TODO Auto-generated method stub
////				DialogUtil.showUIToast(activity, "模块名称="+modelName+"  事项名称="+eventName+" 部门名称"+departName);
//			}
//		};

		return gloabDelegete;

	}


	
	

	public void setGloabDelegete(GloabDelegete gloabDelegete) {
		
		this.gloabDelegete = gloabDelegete;
	}
}
