package com.tjsoft.webhall.constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.PostInfo;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.AddInfoCallBack;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.imp.TransportCallBack;
import com.tjsoft.webhall.ui.user.Login;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Constants {

	/**
	 * gradlew assemblerelease 打release包      gradlew makeJar
	 */
	public static Map<String, List<ATTBean>> materialXkz = new LinkedHashMap<String, List<ATTBean>>();// 许可证
	public static String XKZshili1;
	public static String XKZshili2;
	public static PostInfo getPostInfo;// 领取信息
	public static PostInfo sendPostInfo;// 发送信息
	public static Constants instance = new Constants();// 应用实例
	public static User user;// 用户信息
	TransportEntity transportEntity;
	public static String AREAID = "440306";// 全局区域id
	public static String AREANAME = "";// 全局区域名称
	
//	public static Map<String, List<ATT>> material = new HashMap<String, List<ATT>>();// 上传文件缓存
public static Map<String, List<ATTBean>> material = new LinkedHashMap<String, List<ATTBean>>();// 上传文件缓存
	public static Map<String, List<ATTBean>> materials = new LinkedHashMap<String, List<ATTBean>>();// 上传文件缓存
	// public static SlidingMenu menu;
	private static List<Activity> activityList = new LinkedList<Activity>();// 应用程序所有acitivity
	public static List<Activity> areaActivityList = new LinkedList<Activity>();// 区域选择activity
	public static final boolean isQiebian = false;//是否开启切边

	
	public static final boolean DEBUG_TOGGLE = false; // 调试开关 true表示调试
	public static final boolean CHECK_UPLOAD_FILE = true;

	public static final String ADD_INFO = "完善个人信息";
	public static final String AUTO_LOGIN = "自动绑定";
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
	
//	public static String IP = "http://10.1.48.100";//卢杨测试
//	public static String PORT="8088";//卢杨测试
	
//	public static String IP="http://10.99.76.8";//内网

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
//		System.exit(0);gradlew assemblerelease
	}

	public GloabDelegete gloabDelegete;

	public GloabDelegete getGloabDelegete() {
		GloabDelegete gloabDelegete=new GloabDelegete() {

			@Override
			public TransportEntity getUserInfo() {
				if(transportEntity == null) {
					transportEntity = new TransportEntity();
				}
				if(Constants.user != null) {
					transportEntity.setToken(Constants.user.getTOKEN());
					transportEntity.setUserId(Constants.user.getUSER_ID());
					transportEntity.setName(Constants.user.getUSERNAME());
					transportEntity.setRealName(Constants.user.getREALNAME());
					transportEntity.setLoginPhone(Constants.user.getMOBILE());
					transportEntity.setIdcardType(Constants.user.getTYPE());
					transportEntity.setIdcardNum(Constants.user.getCODE());
				}
                return transportEntity;
			}

			@Override
			public void doActivity(Context context, int tag, TransportCallBack callback) {
				if(tag == 1) {
					//说明是登录
					context.startActivity(new Intent(context,Login.class));
				}
			}

			@Override
			public void addInfo(Activity activity, AddInfoCallBack back) {

			}

			@Override
			public void doBoundMSTAccount(Context context, String todoAcountName, String todoAccountPwd, TransportCallBack transportCallBack) {

			}

			@Override
			public void loginMST(Context context) {

			}

			@Override
			public void modifyPWD(String todoAcountName, String todoAccountPwd) {

			}

			@Override
			public void startStatistics() {

			}

			@Override
			public void endStatistics(String ParentName, String CategoryName, String ModelName) {

			}

			@Override
			public void login(String user, String pwd) {

			}

			@Override
			public void Registered(String strName, String strNewpwd, String strMobile) {

			}

			@Override
			public void contactCustomer(Activity activity, String modelName, String eventName, String departName) {

			}
//			@Override
//			public void faceRecognition(Context context, String name, String id, String token, DefaultActionListener<String> callback) {
//
//			}
		};
		/*
		GloabDelegete gloabDelegete=new GloabDelegete() {
			
			@Override
			public void startStatistics() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void modifyPWD(String todoAcountName, String todoAccountPwd) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void loginMST(Context context) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void login(String user, String pwd) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public TransportEntity getUserInfo() {
				TransportEntity transportEntity=new TransportEntity();
				String password=Md5PwdEncoder.encodePassword("123456789");
				Byte sex='1';
				transportEntity.setUserId("18113142222");
				transportEntity.setName("tjdddtest1");
				transportEntity.setRealName("测试121");
				transportEntity.setPassword(password);
				transportEntity.setLoginPhone("18113142222");
				transportEntity.setToken("123456hefade");
				transportEntity.setIdcardType("1");				
				transportEntity.setIdcardNum("350524166603041596");
				transportEntity.setEnterpriseStatus("1");

				transportEntity.setINC_NAME("企业名字3");
				transportEntity.setINC_TYPE("2");
				transportEntity.setINC_PERMIT("48976114647");
				transportEntity.setINC_ZZJGDM("null");
				transportEntity.setTYSHXYDM("null");
				transportEntity.setINC_DEPUTY("测试企业3");
				transportEntity.setINC_PID("350524132204051274");
				transportEntity.setVersion(1);
				return transportEntity;
			}
			
			@Override
			public void endStatistics(String ParentName, String CategoryName, String ModelName) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void doBoundMSTAccount(Context context, String todoAcountName, String todoAccountPwd,
					TransportCallBack transportCallBack) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void doActivity(Context context, int tag, TransportCallBack callback) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addInfo(Activity activity, AddInfoCallBack back) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void Registered(String strName, String strNewpwd, String strMobile) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void contactCustomer(Activity activity, String modelName, String eventName, String departName) {
				// TODO Auto-generated method stub
				DialogUtil.showUIToast(activity, "模块名称="+modelName+"  事项名称="+eventName+" 部门名称"+departName);
			}
		};
		*/

		return gloabDelegete;
 
	}
	

	
	

	public void setGloabDelegete(GloabDelegete gloabDelegete) {
		
		this.gloabDelegete = gloabDelegete;
	}
}
