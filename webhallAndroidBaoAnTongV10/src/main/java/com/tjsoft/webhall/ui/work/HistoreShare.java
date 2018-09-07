package com.tjsoft.webhall.ui.work;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.ShowPopupMoreUtil;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.util.XMLUtil;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.BLMSBean;
import com.tjsoft.webhall.entity.EMSBean;
import com.tjsoft.webhall.entity.Form;
import com.tjsoft.webhall.entity.FormItemBean;
import com.tjsoft.webhall.entity.NetWorkBean;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.PostInfo;
import com.tjsoft.webhall.entity.Region;
import com.tjsoft.webhall.entity.ShareMaterial;
import com.tjsoft.webhall.entity.WSFWSD;
import com.tjsoft.webhall.entity.YWGSDBean;
import com.tjsoft.webhall.fileChoose.FileChooserActivity;
import com.tjsoft.webhall.lib.ListViewForScrollView;
import com.tjsoft.webhall.lib.NoTouchViewPage;
import com.tjsoft.webhall.ui.bsdt.WDBJ;
import com.tjsoft.webhall.ui.expressage.ExpressageChoose;
import com.tjsoft.webhall.ui.expressage.ExpressageList;
import com.tjsoft.webhall.ui.expressage.ExpressageProgress;
import com.tjsoft.webhall.ui.search.SearchSchedule;
import com.tjsoft.webhall.ui.wsbs.Download;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 历史共享、在线申报
 * 证照界面证照信息数据在这个界面没有用到了----licensePage  licenseDate  HistoreShareAdapter
 * @author Administrator
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class HistoreShare extends AutoDialogActivity {

	private NoTouchViewPage viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView textView1, textView3;// 滑动头部
	private List<TextView> titles;
	private List<View> views;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int TYPE = 1;// 当前页卡编号
	private int bmpW = 0;// 动画图片宽度
	private View formPage, bigFilePage, licensePage, LzfsPage;// 各个页卡 新添加领证方式
	private DisplayMetrics dm;// 设备管理
	private LayoutInflater inflater;
	private Button save, goBack2, next;
	private RelativeLayout back;
	private Intent intent;
	private WebView webView;
	private Permission permission;
	private String PERMID = "";
	private List<Form> forms;
	private int index = 0;
	private List<FormItemBean> formItemBeans;// 表单实例list
	public static List<ApplyBean> applyBeans, bigFileDate, licenseDate;
	private Handler handler = new MyHandler();
	private final int GET_APPLY_BEAN_SUCCESS = 2;
	private final int GET_NETWORK_SUCCESS = 3;
	private final int GET_BLMS_SUCCESS = 4;
	private ListView bigFileList, licenseList;
	private String mark = "6";
	private TextView title;
	private LinearLayout applyLay, formBtn;
	private Button applySave, applySubmit;
	private String status = "0";
	public String STATE = "5";// 表示手机申报
	private int STATUS = -1;
	private String BSNUM;
	private JSONArray dataidArray;
	private String MATERIALS;
	private String formsXML = "";// 表单信息

	public static final String EXTRA_FILE_CHOOSER = "file_chooser";

	protected LinearLayout main, applyLayNoPre, titleBar;
	protected Button exit, applySubmitNoPre, applySaveNoPre;
	private String P_GROUP_ID;// 分组id
	private String P_GROUP_NAME;// 分组名称
	private Context mContext;
	private boolean showCert = false;
	private int pageCount = 3;
	private Button ems, btnFlow;
	private TextView tvIdea;
	private ImageView finish_form, finish_file, finish_lzfs;// 完成申报的箭头指向
	private boolean isFinishForm, isFinishFile;

	/*
	 * 以下为新添加的领证方式的变量   根据5个接口界面展示。。。有点复杂。。。
	 */
	private TextView textView4;// 领证方式头部
	private RadioGroup LZFS_RG;// 领证方式选择
	private RadioButton WDLQ;// 网点领取
	private RadioButton ChooseAddress;// 邮寄领取
	private RadioButton ZXDY;// 自行打印
	private RadioGroup DJFS_RG;// 递交材料方式选择
	private RadioButton WDDJ;// 网点递交
	private RadioButton YJDJ;// 邮寄递交
	private RelativeLayout address_rl;// 邮寄选择后的整个布局
	private LinearLayout parent_ll;// 父布局 点击隐藏输入法
	private List<PostInfo> postInfos, postInfos2;// 从服务得到领证信息
	private List<NetWorkBean> NetWorkItems;
	private BLMSBean blms;// 办理模式实体
	private AddressAdapter addressAdapter;
	private ListViewForScrollView WDDJlist, YJDJlist, WDLQlist;
	private TextView blmsTitle;
	private TextView emptyTextTip;
	private LinearLayout DJCLparent_ll;// 递交材料整个布局
	private LinearLayout LZFSparent_ll;// 领证方式整个布局
	private String LzfsType = "";// 选择领证方式类别 D:网点领取，EMS:邮政速递，P：自行打印
	private EMSBean emsBean;// 获取邮政速递地址及领证方式
	private TextView chooseLZFS_tv;// 选择领证方式
	private TextView chooseDJCL_tv;// 选择递交材料方式
	private TextView RECEIVE;// 收件人
	private TextView PHONE;// 手机号
	private TextView ADDRESS;// 联系地址
	private TextView address_Empty;// 空地址
	private RelativeLayout address_detail;// 领证邮寄地址详情
	private TextView receiveTip;
	private final int ADD_ADDRESS_REQUEST = 100;
	private final int FILE_REQUEST = 66;
	private Spinner parentSpinner;
	private Spinner childSpinner;
	private List<Region> parentItems;
	private List<Region> childItems;
	private ArrayAdapter<String> parentAdapter;
	private ArrayAdapter<String> childAdapter;
	private List<String> parentNames;
	private List<String> childNames;
	private String YWGSDID,YWGSDName;//业务归属地名称和id
	
	
	private ImageView home;
	private ImageView help;
	private RelativeLayout baseForm_title_rl, fileUpload_title_rl, lzfs_title_rl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatisticsTools.start();
		mContext = this;
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_history_share"));
		Constants.getInstance().addActivity(this);
		P_GROUP_ID = getIntent().getStringExtra("P_GROUP_ID");
		P_GROUP_NAME = getIntent().getStringExtra("P_GROUP_NAME");
		permission = (Permission) getIntent().getSerializableExtra("permission");
		PERMID = permission.getID();
		STATUS = getIntent().getIntExtra("STATUS", -1);
		// mark = getIntent().getStringExtra("mark");//暂时屏蔽  好像没有用到了。。。
		BSNUM = getIntent().getStringExtra("BSNUM");
		// init();

		if (STATUS == -1) {// 新申报
			dialog = Background.Process(HistoreShare.this, GetApplyList,
					getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));
		} else {// 在办件
			dialog = Background.Process(HistoreShare.this, GetApplyListByBS,
					getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));
		}

	}

	@Override
	public void onBackPressed() {

		if (STATUS == -1 || STATUS == 9) {
			new AlertDialog.Builder(this).setMessage("是否确定退出当前事项办理？退出将会丢失部分录入信息，您可以选择暂存功能保持当前申报进度。").setTitle("提示")
					.setCancelable(false).setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							finish();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					}).show();
		} else {
			finish();
		}

	}

	/**
	 * 获取网上服务深度
	 */
	final Runnable GetWsfwsdByPermid = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", permission.getID());
				String response = HTTP.excuteAndCache("getWsfwsdByPermid", "RestEMSService", param.toString(),
						HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {

					WSFWSD wsfwsd = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), WSFWSD.class);
					if (null != wsfwsd.getDJZZCL() && wsfwsd.getDJZZCL().equals("2"))// 速递服务
					{
						runOnUiThread(new Runnable() {
							public void run() {
								ems.setVisibility(View.VISIBLE);
							}
						});

					}
					if (null != wsfwsd.getLQSPJG() && wsfwsd.getLQSPJG().equals("2")) {// 速递服务

						runOnUiThread(new Runnable() {
							public void run() {
								ems.setVisibility(View.VISIBLE);
							}
						});
					}

				} else {
					DialogUtil.showUIToast(HistoreShare.this, json.getString("error"));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(HistoreShare.this,
				// getString(MSFWResource.getResourseIdByName(HistoreShare.this,
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	private void init() {
		InitImageView();
		InitTextView();
		InitViewPager();
		InitView1();// 页面初始化
		InitApply();// 初始化在线申
		initSetOnListener();
		if (STATUS != -1 && STATUS != 4 && STATUS != 9) {
			applyLay.setVisibility(View.GONE);
			formBtn.setVisibility(View.GONE);
			// webView.loadUrl("javascript:setreadonly()");//设置表单不可编辑

		}
		if (STATUS == 4 || STATUS == 8 || STATUS == 10) {// 是否显示意见

			new Thread(GetAdvice).start();

		}

		// new Thread(GetWsfwsdByPermid).start();
		dialog = Background.Process(HistoreShare.this, GetFormByPermid,
				getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));// 获取表单
	};

	/**
	 * 获取办理意见
	 */
	final Runnable GetAdvice = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("BSNUM", BSNUM);
				String response = HTTP.excuteAndCache("getAdvice", "RestOnlineDeclareService", param.toString(),
						HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					final String ADVICE = new JSONObject(json.getString("ReturnValue")).getString("ADVICE");
					if (null == ADVICE || ADVICE.equals("") || ADVICE.equals("null")) {
						// DialogUtil.showUIToast(HistoreShare.this,
						// "没有找到相关意见！");
					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								tvIdea.setVisibility(View.VISIBLE);
								tvIdea.setText("审核意见：" + ADVICE);
							}
						});

					}

				} else {
					DialogUtil.showUIToast(HistoreShare.this, getString(
							MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(HistoreShare.this,
				// getString(MSFWResource.getResourseIdByName(HistoreShare.this,
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};

	private void initSetOnListener() {
		applySave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				webView.loadUrl("javascript:androidSave()");
				status = "9";
				if (WDLQ.isChecked()) {
					Constants.getPostInfo = null;
				} else if (ChooseAddress.isChecked()) {
					String RECEIVE_STR = RECEIVE.getText().toString().trim();
					String PHONE_STR = PHONE.getText().toString().trim();
					String ADDRESS_STR = ADDRESS.getText().toString().trim();
					if (!TextUtils.isEmpty(RECEIVE_STR) && !TextUtils.isEmpty(PHONE_STR)
							&& !TextUtils.isEmpty(ADDRESS_STR)) {
						PostInfo postInfo = new PostInfo();
						postInfo.setRECEIVE(RECEIVE_STR);
						postInfo.setADDRESS(ADDRESS_STR);
						postInfo.setPHONE(PHONE_STR);
						Constants.getPostInfo = postInfo;
					} else {
						Constants.getPostInfo = null;
					}

				}
				dialog = Background.Process(HistoreShare.this, ApplySubmit,
						getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));
			}
		});
		applySubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Constants.CHECK_UPLOAD_FILE) {
					if (!isFinishForm) {
						DialogUtil.showUIToast(HistoreShare.this, "基本表单信息未填写完整，请填写完整后再进行申报！");
						return;
					}
					if (!checkLicense()) {
						DialogUtil.showUIToast(HistoreShare.this, "有必交证照未提交，请提交完全后再进行申报！");
						return;
					}
					if (!checkBigFile()) {
						DialogUtil.showUIToast(HistoreShare.this, "有必交材料未提交，请提交完全后再进行申报！");
						return;
					}

					if (WDLQ.isChecked()) {
						Constants.getPostInfo = null;
					} else if (ChooseAddress.isChecked()) {
						String RECEIVE_STR = RECEIVE.getText().toString().trim();
						String PHONE_STR = PHONE.getText().toString().trim();
						String ADDRESS_STR = ADDRESS.getText().toString().trim();
						PostInfo postInfo = new PostInfo();
						postInfo.setRECEIVE(RECEIVE_STR);
						postInfo.setADDRESS(ADDRESS_STR);
						postInfo.setPHONE(PHONE_STR);
						Constants.getPostInfo = postInfo;
					}
				}
				webView.loadUrl("javascript:androidSave()");
				Background.Process(HistoreShare.this, checkFZBD,
						getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));

			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				webView.loadUrl("javascript:androidSave()");
			}
		});

		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HistoreShare.this.finish();
			}
		});
		applySubmitNoPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		applySaveNoPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	/**
	 * 判断证照是否全部提交
	 * 
	 * @return
	 */
	private boolean checkLicense() {
		for (int i = 0; i < licenseDate.size(); i++) {// &&
														// !licenseDate.get(i).getDZHYQ().equals("5")
														// 信息中心要求修改 只要是必要材料都必需提交
			if (licenseDate.get(i).getSFBY().equals("1") && !licenseDate.get(i).getSTATUS().equals("1")) {// SFBY:是否必要，0否，1是,
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断大附件是否全部提交
	 * 
	 * @return
	 */
	private boolean checkBigFile() {
		for (int i = 0; i < bigFileDate.size(); i++) {// &&
														// !bigFileDate.get(i).getDZHYQ().equals("5")
														// 信息中心要求修改 只要是必要材料都必需提交
			if (bigFileDate.get(i).getSFBY().equals("1") && !bigFileDate.get(i).getSTATUS().equals("1")) {
				isFinishFile = false;
				return false;
			}
		}
		isFinishFile = true;
		return true;
	}

	/** 空间获取大附件并比对 */
	final Runnable GetAttachShare = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				JSONArray attachCode = new JSONArray();
				for (int i = 0; i < bigFileDate.size(); i++) {
					attachCode.put(bigFileDate.get(i).getCLBH());
				}
				param.put("USERCODE", Constants.user.getCODE());
				param.put("ATTACHCODE", attachCode);
				String response = HTTP.excuteShare("attachSearch", "SpaceAttachInfoService", param.toString());
				JSONObject json = new JSONObject(response);
				List<ShareMaterial> attachShares = JSONUtil.getGson().fromJson(json.getString("ReturnValue"),
						new TypeToken<List<ShareMaterial>>() {
				}.getType());
				Log.e("sps====", attachShares.size() + "===vvv");
				for (int i = 0; i < bigFileDate.size(); i++) {
					for (int j = 0; j < attachShares.size(); j++) {
						if (bigFileDate.get(i).getCLBH().equals(attachShares.get(j).getATTACHCODE())) {
							bigFileDate.get(i).setSTATUS(attachShares.get(j).getCOMPRESULT());
							bigFileDate.get(i).setFILEID(attachShares.get(j).getID());
							bigFileDate.get(i).setFILEPATH(attachShares.get(j).getATTACHURL());
							bigFileDate.get(i).setFILENAME(attachShares.get(j).getATTACHNAME());
							bigFileDate.get(i).setFILEDEL("0");
							bigFileDate.get(i).setATTS(attachShares.get(j).getATTS());
							Constants.material.put(attachShares.get(j).getATTACHCODE(), attachShares.get(j).getATTBeans());// 将共享结果放入map中

						}

					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						Collections.sort(bigFileDate, new ApplyBeanComparator());
						// bigFileList.setAdapter(new
						// HistoreShareAdapter(bigFileDate, 1));
						bigFileList.setAdapter(new ApplyBeanAdapter(HistoreShare.this, bigFileDate));

					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onDestroy() {
		StatisticsTools.end("申报页面", null, null);
		Constants.material.clear();
		super.onDestroy();
	}

	/** 空间获取证照并比对 */
	final Runnable GetCertShare = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				JSONArray certCode = new JSONArray();
				for (int i = 0; i < licenseDate.size(); i++) {
					certCode.put(licenseDate.get(i).getCLBH());
				}
				param.put("USERCODE", Constants.user.getCODE());
				param.put("ATTACHCODE", certCode);
				String response = HTTP.excuteShare("certInfoSearch", "SpaceCertInfoService", param.toString());
				JSONObject json = new JSONObject(response);
				List<ShareMaterial> certShares = JSONUtil.getGson().fromJson(json.getString("ReturnValue"),
						new TypeToken<List<ShareMaterial>>() {
				}.getType());
				for (int i = 0; i < licenseDate.size(); i++) {
					for (int j = 0; j < certShares.size(); j++) {
						if (licenseDate.get(i).getCLBH().equals(certShares.get(j).getATTACHCODE())) {// 有共享
							licenseDate.get(i).setSTATUS(certShares.get(j).getCOMPRESULT());
							licenseDate.get(i).setFILEID(certShares.get(j).getID());
							licenseDate.get(i).setFILEPATH(certShares.get(j).getATTACHURL());
							licenseDate.get(i).setFILENAME(certShares.get(j).getATTACHNAME());
							licenseDate.get(i).setFILEDEL("0");
							licenseDate.get(i).setATTS(certShares.get(j).getATTS());

							licenseDate.get(i).setCERTCODE(certShares.get(j).getCERTCODE());
							licenseDate.get(i).setCERTSTARTTIME(certShares.get(j).getCERTSTARTTIME());
							licenseDate.get(i).setCERTENDTIME(certShares.get(j).getCERTENDTIME());
							Constants.material.put(certShares.get(j).getATTACHCODE(), certShares.get(j).getATTBeans());// 将共享结果放入map中
						}

					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						Collections.sort(licenseDate, new ApplyBeanComparator());
						licenseList.setAdapter(new HistoreShareAdapter(licenseDate, 2));
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void InitApply() {
		formBtn.setVisibility(View.GONE);
		// if(STATUS==9){
		// title.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent=new Intent();
		// intent.putExtra("flag", 2);
		// intent.putExtra("SXID", PERMID);
		// intent.putExtra("BSNUM", BSNUM);
		// intent.setClass(HistoreShare.this, CaptureActivity.class);
		// startActivity(intent);
		// }
		// });
		// }
		title.setText(permission.getSXZXNAME());

	}

	public String getUrlById(String id) {
		String url = Constants.DOMAIN + "u/forms/" + id + "/" + id + ".html";
		Log.e("sps", "url===" + url);
		return url;
	}

	private void InitView1() {

		if (STATUS != -1 && STATUS != 9) {
			finish_form
					.setImageResource(MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
			finish_file
					.setImageResource(MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
			finish_lzfs
					.setImageResource(MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
		}
		tvIdea = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "tvIdea"));
		goBack2 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "goBack2"));
		next = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "next"));
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currIndex == 0) {
					// webView.loadUrl("javascript:androidSave()");
					viewPager.setCurrentItem(1);
				} else if (currIndex == 1 && showCert) {
					viewPager.setCurrentItem(2);
				} else if (currIndex == 1 && !showCert) {
					// if (!checkBigFile()) {
					// DialogUtil.showUIToast(HistoreShare.this,
					// "有必交材料未提交，请提交完全后再进行下一步操作！");
					// return;
					// }
					viewPager.setCurrentItem(2);
				}

			}
		});
		goBack2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("currIndex        " + currIndex);
				if (currIndex == 2) {
					viewPager.setCurrentItem(1);
				} else if (currIndex == 1) {
					viewPager.setCurrentItem(0);

				} else if (currIndex == 3) {
					viewPager.setCurrentItem(2);
				}else if(currIndex==0){
					onBackPressed();
				}

			}
		});

		main = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "main"));
		applyLayNoPre = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "applyLayNoPre"));
		titleBar = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "titleBar"));
		exit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "exit"));
		applySubmitNoPre = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "applySubmitNoPre"));
		applySaveNoPre = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "applySaveNoPre"));
		ems = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "ems"));
		btnFlow = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnFlow"));
		home = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		help = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "help"));
		if (STATUS == -1 || STATUS == 9) {// 新申报，暂存件
			home.setVisibility(View.GONE);
			help.setVisibility(View.VISIBLE);
			btnFlow.setVisibility(View.GONE);
		} else {
			btnFlow.setVisibility(View.VISIBLE);
			home.setVisibility(View.GONE);
			help.setVisibility(View.GONE);
		}
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constants.getInstance().exit();
			}
		});
		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowPopupMoreUtil.showPopupMore(v, HistoreShare.this, PERMID,"网上申报",permission.getSXZXNAME(),permission.getDEPTNAME(),null);
			}
		});
		btnFlow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(HistoreShare.this, SearchSchedule.class);
				intent.putExtra("BSNUM", BSNUM);
				intent.putExtra("APPNAME", "erweima");
				startActivity(intent);

			}
		});

		ems.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (STATUS == -1 || STATUS == 9) {// 新申报
					Intent intent = new Intent();
					intent.setClass(HistoreShare.this, ExpressageChoose.class);
					startActivity(intent);
				} else {// 已申报
					Intent intent = new Intent();
					intent.setClass(HistoreShare.this, ExpressageProgress.class);
					intent.putExtra("BSNUM", BSNUM);
					startActivity(intent);
				}

			}
		});

		ems.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		btnFlow.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		title = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "title"));
		applyLay = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "applyLay"));
		formBtn = (LinearLayout) formPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "formBtn"));
		applySave = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "applySave"));
		applySubmit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "applySubmit"));

		save = (Button) formPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "save"));
		webView = (WebView) formPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "webView"));

		WebSettings webSettings = webView.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setSupportZoom(false);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.addJavascriptInterface(new JSI(), "android");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {// 自动填充数据
				if (null != BSNUM && !BSNUM.equals("")) {		
					new Thread(GetFormByBsNo).start();// 已申报的
				} else {
					new Thread(GetInfoByUserid).start();// 新申报的
				}

				if (STATUS != -1 && STATUS != 4 && STATUS != 9) {
					webView.loadUrl("javascript:setreadonly()");// 设置表单不可编辑
				}
				super.onPageFinished(view, url);

			}

		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
				AlertDialog.Builder alert = new AlertDialog.Builder(HistoreShare.this)
						.setTitle(getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_notify")))
						.setMessage(message).setPositiveButton(
								getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_OK")),
								new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
					}
				});

				alert.setCancelable(false);
				alert.create();
				alert.show();
				return true;
			}

		});

	}

	/**
	 * 根据流水号获取材料列表
	 */
	final Runnable GetApplyListByBS = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("BSNUM", BSNUM);
				String response = HTTP.excuteAndCache("getInsMaterialInfo", "RestOnlineDeclareService",
						param.toString(), HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					MATERIALS = new String(
							Base64.decode(new JSONObject(json.getString("ReturnValue")).getString("MATERIALS"), 1));
					System.out.println("fuchl    MATERIALS:" + MATERIALS);
					applyBeans = XMLUtil.parseMaterials(MATERIALS);
					if (Constants.material.size() == 0) {// 只填充一次
						XMLUtil.material2Map(MATERIALS);
					}
					if (null != applyBeans) {
						bigFileDate = new ArrayList<ApplyBean>();
						licenseDate = new ArrayList<ApplyBean>();
						for (int i = 0; i < applyBeans.size(); i++) {
							if (applyBeans.get(i).getDZHYQ().contains("1") || applyBeans.get(i).getDZHYQ().contains("3")
									|| (applyBeans.get(i).getDZHYQ().contains("5"))) {
								bigFileDate.add(applyBeans.get(i));

							} else if (applyBeans.get(i).getDZHYQ().contains("4")) {
								licenseDate.add(applyBeans.get(i));
							}
						}

					}
					handler.sendEmptyMessage(GET_APPLY_BEAN_SUCCESS);
				} else {
					DialogUtil.showUIToast(HistoreShare.this, json.getString("error"));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(HistoreShare.this,
				// getString(MSFWResource.getResourseIdByName(HistoreShare.this,
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};

	private class ApplyBeanComparator implements Comparator<ApplyBean> {
		public int compare(ApplyBean o1, ApplyBean o2) {
			int a = Integer.parseInt(o1.getSTATUS());
			int b = Integer.parseInt(o2.getSTATUS());
			return b - a;
		}
	}

	final Runnable GetApplyList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", PERMID);
				param.put("P_GROUP_ID", P_GROUP_ID);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				String response = HTTP.excuteAndCache("getClxxByPermid", "RestPermissionitemService", param.toString(),
						HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					applyBeans = JSONUtil.getGson().fromJson(json.getString("ReturnValue"),
							new TypeToken<List<ApplyBean>>() {
					}.getType());
					if (null != applyBeans) {
						bigFileDate = new ArrayList<ApplyBean>();
						licenseDate = new ArrayList<ApplyBean>();
						for (int i = 0; i < applyBeans.size(); i++) {
							if (applyBeans.get(i).getDZHYQ().contains("1") || applyBeans.get(i).getDZHYQ().contains("3")
									|| (applyBeans.get(i).getDZHYQ().contains("5"))) {
								bigFileDate.add(applyBeans.get(i));

							} else if (applyBeans.get(i).getDZHYQ().equals("4")) {
								licenseDate.add(applyBeans.get(i));
							}
						}
					}
					handler.sendEmptyMessage(GET_APPLY_BEAN_SUCCESS);
				} else {
					DialogUtil.showUIToast(HistoreShare.this, json.getString("error"));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(HistoreShare.this,
				// getString(MSFWResource.getResourseIdByName(HistoreShare.this,
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	final Runnable GetFormByPermid = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", PERMID);
				param.put("P_GROUP_ID", P_GROUP_ID);
				param.put("P_GROUP_NAME", P_GROUP_NAME);
				param.put("ENTERANCE", "1");// 1手机
				String response = HTTP.excuteAndCache("getFormByPermidV2", "RestPermissionitemService",
						param.toString(), HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					forms = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Form>>() {
					}.getType());
					runOnUiThread(new Runnable() {
						public void run() {
							if (null != forms) {// 去掉1
								for (int i = 0; i < forms.size(); i++) {
									if (null != forms.get(i).getFORMTYPE() && forms.get(i).getFORMTYPE().equals("1")) {
										forms.remove(i);
									}
								}
								webView.loadUrl(getUrlById(forms.get(0).getID()));
							}
						}
					});

				} else {
					DialogUtil.showUIToast(HistoreShare.this, json.getString("error"));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(HistoreShare.this,
				// getString(MSFWResource.getResourseIdByName(HistoreShare.this,
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	final Runnable GetFormByBsNo = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("BSNUM", BSNUM);
				String response = HTTP.excuteAndCache("getInsFormData", "RestOnlineDeclareService", param.toString(),
						HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					String returnValue = json.getString("ReturnValue");
					String FORMS = new JSONObject(returnValue).getString("FORMS");
					String formXml = new String(Base64.decode(FORMS.getBytes(), 1));
					Log.e("sps====", "formXml  fcl===="+formXml.replaceAll("[\n\r]",""));
					final String jsonData = XMLUtil.toJsonForJS(formXml.replaceAll("[\n\r]",""));
					dataidArray = new JSONObject(jsonData).getJSONArray("dataid");

					runOnUiThread(new Runnable() {
						public void run() {
							webView.loadUrl("javascript:shareformvalue('" + jsonData + "','form1')");
						}
					});

				} else {
					DialogUtil.showUIToast(HistoreShare.this, json.getString("error"));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(HistoreShare.this,
				// getString(MSFWResource.getResourseIdByName(HistoreShare.this,
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	final Runnable GetCorInfo = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("USERCODE", Constants.user.getCODE());
				param.put("USERTYPE", Constants.user.getTYPE());
				String response = HTTP.excuteShare("baseInfoShare", "SpaceDataInfoService", param.toString());
				final String ReturnValue = new JSONObject(response).getString("ReturnValue");
				runOnUiThread(new Runnable() {
					public void run() {
						webView.loadUrl("javascript:shareformvalue('" + ReturnValue + "')");
					}
				});

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	};
	final Runnable GetInfoByUserid = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("ID", Constants.user.getUSER_ID());
				String response = HTTP.excuteAndCache("getInfoByUserid", "RestUserService", param.toString(),
						HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					final String userDetail = json.getString("ReturnValue");
					Log.e("sps", "执行了…userDetail…====" + userDetail);
					runOnUiThread(new Runnable() {
						public void run() {
							if (null != FileUtil.Load(HistoreShare.this, Constants.user.getUSER_ID() + "_" + PERMID)
									&& !FileUtil.Load(HistoreShare.this, Constants.user.getUSER_ID() + "_" + PERMID)
											.equals("")) {
								// 从缓存中读取
								webView.loadUrl("javascript:shareformvalue('"
										+ FileUtil.Load(HistoreShare.this, Constants.user.getUSER_ID() + "_" + PERMID)
										+ "')");
							} else {
								// 从个人信息读取
								webView.loadUrl("javascript:shareformvalue('" + userDetail + "')");
								if (Constants.isShare) {
									new Thread(GetCorInfo).start();
								}
							}
						}
					});

				} else {
				}

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	};

	final class JSI {
		JSI() {
		}

		@JavascriptInterface
		public void save(final String s) {
			Log.e("sps", "   s====   " + s);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (s.toString().length() < 10) {
						finish_form.setImageResource(
								MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_no"));
						isFinishForm = false;
						return;
					} else {
						finish_form.setImageResource(
								MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
						isFinishForm = true;
					}
				}
			});

			// runOnUiThread(new Runnable() {
			// public void run() {
			// if (currIndex == 0) {
			// viewPager.setCurrentItem(1);
			// return;
			// }
			// }
			// });
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			try {
				JSONObject json = new JSONObject(s);
				formItemBeans = JSONUtil.getGson().fromJson(json.getString("data"),
						new TypeToken<List<FormItemBean>>() {
						}.getType());
				serializer.setOutput(writer);
				serializer.startDocument("utf-8", null);
				serializer.startTag("", "data");

				serializer.startTag("", "dataid");
				if (null != dataidArray) {
					serializer.cdsect(dataidArray.getString(index));
				}
				serializer.endTag("", "dataid");
				serializer.startTag("", "pid");
				serializer.cdsect(PERMID);
				serializer.endTag("", "pid");
				serializer.startTag("", "bsnum");
				if (null != BSNUM) {
					serializer.cdsect(BSNUM);
				}
				serializer.endTag("", "bsnum");
				serializer.startTag("", "formid");
				serializer.cdsect(forms.get(index).getID());
				serializer.endTag("", "formid");
				serializer.startTag("", "version");
				serializer.cdsect(forms.get(index).getFORMVER());
				serializer.endTag("", "version");
				serializer.startTag("", "formtype");
				serializer.cdsect(forms.get(index).getFORMTYPE());
				serializer.endTag("", "formtype");
				JSONObject formDataCache;
				if (FileUtil.Load(HistoreShare.this, Constants.user.getUSER_ID() + "_" + PERMID).equals("")) {
					formDataCache = new JSONObject();
				} else {
					formDataCache = new JSONObject(
							FileUtil.Load(HistoreShare.this, Constants.user.getUSER_ID() + "_" + PERMID));
				}

				for (int j = 0; j < formItemBeans.size(); j++) {
					serializer.startTag("", formItemBeans.get(j).getName());
					serializer.attribute("", "type", formItemBeans.get(j).getType());
					serializer.cdsect(formItemBeans.get(j).getValue());
					serializer.endTag("", formItemBeans.get(j).getName());
					formDataCache.put(formItemBeans.get(j).getName(), formItemBeans.get(j).getValue());
				}
				FileUtil.Write(HistoreShare.this, Constants.user.getUSER_ID() + "_" + PERMID, formDataCache.toString());

				serializer.endTag("", "data");
				serializer.endDocument();
				forms.get(index).setXML(writer.toString());
				System.out.println("fuchl  " + forms.get(index).getXML());
				if (index == forms.size() - 1) {// 最后一张表单时将xml组装
					for (int i = 0; i < forms.size(); i++) {
						formsXML += forms.get(i).getXML();
						formsXML = formsXML.replaceAll("<\\?xml.*.\\?>", "");
					}
					formsXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><forms>" + formsXML + "</forms>";
					Log.e("sps===", "formsXML==" + formsXML);
					FileUtil.Write(HistoreShare.this, Constants.user.getUSER_ID() + "_" + PERMID + "XML", formsXML);
				} else {
					index++;
					runOnUiThread(new Runnable() {
						public void run() {
							webView.loadUrl(getUrlById(forms.get(index).getID()));
							// goBack2.setVisibility(View.VISIBLE);
						}
					});
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@JavascriptInterface
		public void back(String s) {
			if (index == 0) {
				HistoreShare.this.finish();
			} else {
				index--;
				webView.goBack();
			}
		}

	}

	private void InitViewPager() {
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		viewPager = (NoTouchViewPage) findViewById(MSFWResource.getResourseIdByName(this, "id", "vPager"));
		viewPager.setScrollble(true);
		views = new ArrayList<View>();
		inflater = getLayoutInflater();
		formPage = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_forms"), null);
		bigFilePage = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_share_big_file"), null);
		licensePage = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_share_big_file"), null);
		LzfsPage = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_lzfs_page"), null);

		views.add(formPage);
		if (showCert) {
			views.add(licensePage);
		}
		views.add(bigFilePage);
		views.add(LzfsPage);

		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		bigFileList = (ListView) bigFilePage.findViewById(MSFWResource.getResourseIdByName(this, "id", "listView"));
		licenseList = (ListView) licensePage.findViewById(MSFWResource.getResourseIdByName(this, "id", "listView"));
		ImageView empty = (ImageView) bigFilePage.findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
		bigFileList.setEmptyView(empty);
		empty = (ImageView) licensePage.findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
		licenseList.setEmptyView(empty);

		initLZFS();
	}

	/**
	 * 初始化头标
	 */

	private void InitTextView() {

		textView1 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text1"));
		// textView2 = (TextView)
		// findViewById(MSFWResource.getResourseIdByName(this, "id", "text2"));
		textView3 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text3"));
		textView4 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text4"));
		titles = new ArrayList<TextView>();
		baseForm_title_rl = (RelativeLayout) findViewById(
				MSFWResource.getResourseIdByName(this, "id", "base_form_title_rl"));
		fileUpload_title_rl = (RelativeLayout) findViewById(
				MSFWResource.getResourseIdByName(this, "id", "upload_file_title_rl"));
		lzfs_title_rl = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lzfs_title_rl"));

		// if (showCert) {
		// textView2.setVisibility(View.VISIBLE);
		// }

		titles.add(textView1);
		// if (showCert) {
		// titles.add(textView2);
		// }
		titles.add(textView3);
		titles.add(textView4);
		baseForm_title_rl.setOnClickListener(new MyOnClickListener(0));
		// textView2.setOnClickListener(new MyOnClickListener(1));
		fileUpload_title_rl.setOnClickListener(new MyOnClickListener(1));
		lzfs_title_rl.setOnClickListener(new MyOnClickListener(2));
	}

	/**
	 * 
	 * 头标点击监听 3
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		/**
		 * 控制点击标题只能一步步往下走，不能直接到第三个标题
		 */
		public void onClick(View v) {
			// Log.d("click", "index===="+index);
			// if(index==1&&currIndex==0){//点击上传材料 当前页在基本表单
			// Log.d("click222", "index=22==="+index);
			// webView.loadUrl("javascript:androidSave()");
			// }else if(index==2&&currIndex==1){//点击领取证照 当前页在上传材料
			// if (!checkBigFile()) {
			// DialogUtil.showUIToast(HistoreShare.this,
			// "有必交材料未提交，请提交完全后再进行下一步操作！");
			// return;
			// }
			// viewPager.setCurrentItem(index);
			// }else if(index==2&&currIndex==0){//点击领取证照 当前页在基本表单 跳转到上传材料页
			// webView.loadUrl("javascript:androidSave()");
			// }else{
			// viewPager.setCurrentItem(index);
			// }
			viewPager.setCurrentItem(index);

		}

	}

	/**
	 * 2 * 初始化动画 3
	 */

	private void InitImageView() {
		finish_form = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "finish_form"));
		finish_file = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "finish_file"));
		finish_lzfs = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "finish_lzfs"));

		imageView = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "cursor"));
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dm.widthPixels / 3,
				DensityUtil.dip2px(this, 2));
		imageView.setLayoutParams(params);
		bmpW = dm.widthPixels / 3;
		int screenW = dm.widthPixels;// 获取分辨率宽度 dm.widthPixels * 4 / 25
		if (showCert) {
			pageCount = 4;
		}

		offset = (screenW / pageCount - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		Animation animation = new TranslateAnimation(0, offset, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(1);
		imageView.startAnimation(animation);
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;
		int two = one * 2;

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {

			if (arg0 == 0) {

				if (isFinishForm) {
					finish_form.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
				} else {
					finish_form.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_no"));
				}
				if (isFinishFile) {
					finish_file.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
				} else {
					finish_file.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_no"));
				}

				applySubmit.setVisibility(View.GONE);
				next.setVisibility(View.VISIBLE);
				goBack2.setVisibility(View.VISIBLE);

			}
			if (arg0 == 1) {
				webView.loadUrl("javascript:androidSave()");
				if (isFinishForm) {
					finish_form.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
				} else {
					finish_form.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_no"));
				}
				if (isFinishFile) {
					finish_file.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
				} else {
					finish_file.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_no"));
				}

				if (showCert) {
					next.setVisibility(View.VISIBLE);
					applySubmit.setVisibility(View.GONE);
				} else {
					next.setVisibility(View.VISIBLE);
					applySubmit.setVisibility(View.GONE);
				}
				goBack2.setVisibility(View.VISIBLE);
				TYPE = 1;// 材料附件
			} else if (arg0 == 2 && showCert) {// 证照信息
				TYPE = 2;
				applySubmit.setVisibility(View.VISIBLE);
				goBack2.setVisibility(View.VISIBLE);
				next.setVisibility(View.GONE);
			}
			if (arg0 == 2 && !showCert) {
				finish_lzfs.setImageResource(
						MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
				webView.loadUrl("javascript:androidSave()");
				checkBigFile();
				if (isFinishForm) {
					finish_form.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
				} else {
					finish_form.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_no"));
				}
				if (isFinishFile) {
					finish_file.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
				} else {
					finish_file.setImageResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_no"));
				}
				dialog = Background.Process(mContext, GetPermWsfwsd,
						getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));
				dialog = Background.Process(mContext, GetNetworkByPermid,
						getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));

				applySubmit.setVisibility(View.VISIBLE);
				goBack2.setVisibility(View.VISIBLE);
				next.setVisibility(View.GONE);
				if (isFinishFile && isFinishForm) {
					applySubmit.setBackgroundResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_blue_btn_shape"));
				} else {
					applySubmit.setBackgroundResource(
							MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_gray_btn_shape"));
				}
			}

			Animation animation = new TranslateAnimation(one * currIndex + offset, one * arg0 + offset, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);
			for (int i = 0; i < titles.size(); i++) {
				titles.get(i).setTextColor(MSFWResource.getResourseIdByName(HistoreShare.this, "color", "tj_tab_text"));
			}
			titles.get(arg0).setTextColor(HistoreShare.this.getResources()
					.getColor(MSFWResource.getResourseIdByName(HistoreShare.this, "color", "tj_my_green")));
		}

	}

	/**
	 * 已经没有用到了……   
	 */
	class HistoreShareAdapter extends BaseAdapter {

		private List<ApplyBean> data;
		private int flag;

		/**
		 * 
		 * @param applyBeans
		 * @param flag
		 *            1、大附件 2、证照
		 */
		public HistoreShareAdapter(List<ApplyBean> applyBeans, int flag) {
			this.data = applyBeans;
			this.flag = flag;
		}

		@Override
		public int getCount() {
			return null == data ? 0 : data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			Container container;
			if (null == convertView) {
				container = new Container();
				convertView = inflater.inflate(
						MSFWResource.getResourseIdByName(mContext, "layout", "tj_histore_share_item"), parent, false);
				container.shyj = (TextView) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "shyj"));
				container.type = (TextView) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "type"));
				container.name = (TextView) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "name"));
				container.status = (ImageButton) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "status"));
				container.chooseFile = (Button) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "chooseFile"));
				container.choosePhoto = (Button) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "choosePhoto"));
				container.download = (Button) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "download"));
				container.tj_pc = (ImageView) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "tj_pc"));
				container.SFBY = (TextView) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "SFBY"));
				container.isPhone = (TextView) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "tj_isPhone"));
				convertView.setTag(container);
			} else {
				container = (Container) convertView.getTag();
			}

			final ApplyBean applyBean = data.get(position);
			final String status = applyBean.getSTATUS();
			// 已共享 未共享分类
			if (position == 0 && status.equals("1")) {// 第一个
				container.type.setVisibility(View.VISIBLE);
				container.type.setText("已提交");
				container.type.setBackgroundColor(getResources()
						.getColor(MSFWResource.getResourseIdByName(HistoreShare.this, "color", "tj_is_share")));

			} else if (position == 0 && !status.equals("1")) {
				container.type.setVisibility(View.VISIBLE);
				container.type.setText("未提交");
				container.type.setBackgroundColor(getResources()
						.getColor(MSFWResource.getResourseIdByName(HistoreShare.this, "color", "tj_no_share")));
			} else if (position > 0 && !status.equals(data.get(position - 1).getSTATUS())) {// 状态不一样加分割条
				container.type.setVisibility(View.VISIBLE);
				container.type.setText("未提交");
				container.type.setBackgroundColor(getResources()
						.getColor(MSFWResource.getResourseIdByName(HistoreShare.this, "color", "tj_no_share")));
			} else {
				container.type.setVisibility(View.GONE);
			}
			// 审核意见
			if (null != data && applyBean != null && applyBean.getSH() != null && applyBean.getSH().equals("0")) {
				container.shyj.setVisibility(View.VISIBLE);
				container.shyj.setText(applyBean.getSHYJ());
			} else {
				container.shyj.setVisibility(View.GONE);
			}

			container.download.setOnClickListener(new OnClickListener() { // 文件下载

				@Override
				public void onClick(View v) {
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						Intent intent = new Intent();
						intent.setClass(HistoreShare.this, Download.class);
						intent.putExtra("title", applyBean.getCLMC());
						intent.putExtra("FILEID", applyBean.getFILEID());
						startActivity(intent);
					} else {
						DialogUtil.showUIToast(HistoreShare.this, getString(MSFWResource
								.getResourseIdByName(HistoreShare.this, "string", "tj_sdcard_unmonted_hint")));
					}
				}
			});
			container.name.setText(applyBean.getCLMC());
			if (status.equals("1") && (null == applyBean.getFILEID() || applyBean.getFILEID().equals(""))) {// 设置提交状态
				container.status
						.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_is_submit"));
				container.download.setVisibility(View.GONE);
			} else if (status.equals("1") && null != applyBean.getFILEID() && !applyBean.getFILEID().equals("")) {
				container.status
						.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_is_share"));
				container.download.setVisibility(View.GONE);

			} else {
				container.status
						.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_no_submit"));
				container.download.setVisibility(View.GONE);
			}
			if (applyBean.getSFBY().equals("1")) {// 必要提交除窗口递交外
				container.SFBY.setVisibility(View.VISIBLE);
			} else {
				container.SFBY.setVisibility(View.GONE);
			}
			if (applyBean.getDZHYQ().contains("1")) {
				container.tj_pc.setVisibility(View.VISIBLE);
			} else {
				container.tj_pc.setVisibility(View.GONE);
			}
			if (applyBean.getDZHYQ().equals("3")) {
				container.isPhone.setText("电");
			} else if (applyBean.getDZHYQ().equals("4")) {
				container.isPhone.setText("证");
			} else if (applyBean.getDZHYQ().equals("5")) {
				container.isPhone.setText("纸");
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (applyBean.getDZHYQ().contains("4")) {
						TYPE = 1;
					} else {
						TYPE = 2;

					}
					if (!status.equals("1")) {
						if (STATUS != -1 && STATUS != 9 && STATUS != 4) {
							Intent intent = new Intent();
							intent.putExtra("applyBean", applyBean);
							intent.putExtra("PERMID", PERMID);
							intent.putExtra("position", position);
							intent.putExtra("TYPE", TYPE);
							intent.putExtra("from", "HistoreShare");
							intent.putExtra("STATUS", STATUS);
							intent.putExtra("flag", flag);
							intent.putExtra("mark", mark);
							intent.setClass(HistoreShare.this, MaterialManage.class);
							startActivityForResult(intent, 3);
						} else {
							uploadFile(applyBean, position);
						}
					} else {
						Intent intent = new Intent();
						intent.putExtra("applyBean", applyBean);
						intent.putExtra("PERMID", PERMID);
						intent.putExtra("position", position);
						intent.putExtra("TYPE", TYPE);
						intent.putExtra("from", "HistoreShare");
						intent.putExtra("STATUS", STATUS);
						intent.putExtra("flag", flag);
						intent.putExtra("mark", mark);
						intent.setClass(HistoreShare.this, MaterialManage.class);
						startActivityForResult(intent, 3);
					}

				}
			});

			return convertView;

		}

		public final class Container {
			TextView type;// 材料大分类
			TextView name;// 材料名称
			TextView shyj;// 审核意见
			TextView SFBY;// 是否必要
			ImageButton status;// 已提交、未提交
			Button chooseFile;// 选择文件
			Button choosePhoto;// 拍照上传
			Button download;// 下载
			ImageView tj_pc;// 复杂表单标记
			TextView isPhone;// 手机是否必要上传 纸质 电子 证照
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ADD_ADDRESS_REQUEST && resultCode == 101) {//邮寄地址选择之后界面展示
			finish_lzfs
					.setImageResource(MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
			PostInfo postInfo = (PostInfo) data.getSerializableExtra("postInfo");
			Constants.getPostInfo = postInfo;
			address_detail.setVisibility(View.VISIBLE);
			address_Empty.setVisibility(View.GONE);
			RECEIVE.setText(postInfo.getRECEIVE());
			ADDRESS.setText(postInfo.getADDRESS());
			PHONE.setText(postInfo.getPHONE());
		} else if (requestCode == FILE_REQUEST && data != null) {//添加完图片之后界面更新adapter
			ApplyBean applyBean = (ApplyBean) data.getSerializableExtra("apply");
			if (applyBean != null) {
				for (ApplyBean apply : licenseDate) {
					if (applyBean.getID().equals(apply.getID())) {
						apply.setCERTIFICATEID(applyBean.getCERTIFICATEID());
						apply.setCERTIFICATESTARTDATE(applyBean.getCERTIFICATESTARTDATE());
						apply.setCERTIFICATEENDDATE(applyBean.getCERTIFICATEENDDATE());
					}
				}
			}
//			Collections.sort(licenseDate, new ApplyBeanComparator());// 重新排序
			if (bigFileDate!=null&&bigFileDate.size()!=0) {
				
				Collections.sort(bigFileDate, new ApplyBeanComparator());
			}
			bigFileList.setAdapter(new ApplyBeanAdapter(HistoreShare.this, bigFileDate));
//			licenseList.setAdapter(new HistoreShareAdapter(licenseDate, 2));
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case GET_APPLY_BEAN_SUCCESS:

				if (licenseDate.size() > 0) {
					showCert = true;
				} else {
					showCert = false;
				}
				init();

				Collections.sort(licenseDate, new ApplyBeanComparator());// 重新排序
				Collections.sort(bigFileDate, new ApplyBeanComparator());
				bigFileList.setAdapter(new ApplyBeanAdapter(HistoreShare.this, bigFileDate));
				licenseList.setAdapter(new HistoreShareAdapter(licenseDate, 2));
				// 获取空间大附件和证照信息
				if (Constants.isShare && (STATUS == -1 || STATUS == 9)) {// ||
					dialog = Background.Process(HistoreShare.this, GetAttachShare,
							getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));
					dialog = Background.Process(HistoreShare.this, GetCertShare,
							getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));
				}
				break;
			case GET_NETWORK_SUCCESS:
				addressAdapter = new AddressAdapter(mContext, NetWorkItems);
				WDDJlist.setAdapter(addressAdapter);
				YJDJlist.setAdapter(addressAdapter);
				WDLQlist.setAdapter(addressAdapter);
				break;
			case GET_BLMS_SUCCESS:
				if (blms != null) {
					parent_ll.setVisibility(View.VISIBLE);
					controllViewGetBLMS();
					if (STATUS != -1) {
						dialog = Background.Process(HistoreShare.this, GetBusiPostInfo2,
								getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));
						dialog = Background.Process(HistoreShare.this, GetBusiPostInfo,
								getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));
					}
				} else {
					emptyTextTip.setVisibility(View.VISIBLE);
					parent_ll.setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 检测复杂表单
	 */
	final Runnable checkFZBD = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject object = new JSONObject();
				object.put("PERMID", PERMID);
				String str = HTTP.excuteAndCache("checkComplexForm", "RestPermissionitemService", object.toString(),
						HistoreShare.this);
				JSONObject jb = new JSONObject(str);
				String cd = jb.getString("code");
				if (cd.equals("200")) {
					String SFFZBD = new JSONObject(jb.getString("ReturnValue")).getString("SFFZBD");
					if (null != SFFZBD && (SFFZBD.equals("1") || cheackFormbyPC())) {// 包含复杂表单
						runOnUiThread(new Runnable() {
							public void run() {
								new AlertDialog.Builder(HistoreShare.this)
										.setMessage(MSFWResource.getResourseIdByName(HistoreShare.this, "string",
												"tj_notice"))
										.setTitle("温馨提示")
										.setPositiveButton("暂存", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										status = "9";
										dialog = Background.Process(HistoreShare.this, ApplySubmit,
												getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string",
														"tj_loding")));
									}
								}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										return;
									}
								}).show();
							}
						});

					} else {
						status = "0";
						runOnUiThread(new Runnable() {
							public void run() {
								dialog = Background.Process(HistoreShare.this, ApplySubmit, getString(
										MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));
							}
						});
					}

				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(HistoreShare.this,
				// getString(MSFWResource.getResourseIdByName(HistoreShare.this,
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}

	};

	final Runnable ApplySubmit = new Runnable() {
		@Override
		public void run() {
			try {

				formsXML = getFormsXML();
				System.out.println("fuchl############:" + formsXML);
				String businessXML = getBusinessXML();
				String materialsXML = getMaterialsXML();
				String postXML = getPostXML();
				System.out.println("fuchl#####:businessXML" + businessXML);
				System.out.println("fuchl############:postXML" + postXML);
				System.out.println("------------------------------materialsXML=" + materialsXML);

				if (null == formsXML || formsXML.equals("")) {
					DialogUtil.showUIToast(HistoreShare.this, "请先完善表单信息");
					return;
				}
				if (null == businessXML || businessXML.equals("")) {
					DialogUtil.showUIToast(HistoreShare.this, "业务信息异常");
					return;
				}
				if (null == materialsXML || materialsXML.equals("")) {
					DialogUtil.showUIToast(HistoreShare.this, "材料信息异常");
					return;
				}

				// 非新申请formXML 处理
				if (null != BSNUM) {
					InputStream is = new ByteArrayInputStream(formsXML.getBytes());
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
					NodeList dataidList = doc.getElementsByTagName("dataid");
					for (int i = 0; i < dataidList.getLength(); i++) {
						Element dataidElement = (Element) dataidList.item(i);
						dataidElement.setTextContent(dataidArray.getString(i));
					}
					NodeList bsnumList = doc.getElementsByTagName("bsnum");
					for (int i = 0; i < bsnumList.getLength(); i++) {
						Element bsnumElement = (Element) bsnumList.item(i);
						bsnumElement.setTextContent(BSNUM);
					}
					DOMSource domSource = new DOMSource(doc);
					StringWriter writer = new StringWriter();
					StreamResult result = new StreamResult(writer);
					TransformerFactory tf = TransformerFactory.newInstance();
					Transformer transformer = tf.newTransformer();
					transformer.transform(domSource, result);
					formsXML = writer.toString();

				}

				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("FORMS", new String(Base64.encode(formsXML.getBytes(), 0), "UTF-8"));
				param.put("BUSINESS", new String(Base64.encode(businessXML.getBytes(), 0), "UTF-8"));
				param.put("MATERIALS", new String(Base64.encode(materialsXML.getBytes(), 0), "UTF-8"));
				param.put("POSTXML", new String(Base64.encode(postXML.getBytes(), 0), "UTF-8"));

				String response = HTTP.excuteAndCache("submit", "RestOnlineDeclareService", param.toString(),
						HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					if (status.equals("0")) {
						// DialogUtil.showUIToast(HistoreShare.this,
						// "温馨提示：您的业务已经申报成功，请根据手机短信提醒，到现场递交纸质材料或领取证照。"+ "\n"
						// +"服务网点：宝安区政务服务中心，地址：深圳市宝安区体育中心综合训练馆一楼");
						DialogUtil.showUIToast(HistoreShare.this, "申报成功！");
					} else {
						DialogUtil.showUIToast(HistoreShare.this, "暂存成功！");
					}
					if (STATUS == -1) {
						intent = new Intent();
						intent.setClass(HistoreShare.this, WDBJ.class);
						startActivity(intent);
						finish();
					} else {
						setResult(100);
						finish();
					}

				} else {
					DialogUtil.showUIToast(HistoreShare.this, json.getString("error"));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(HistoreShare.this,
				// getString(MSFWResource.getResourseIdByName(HistoreShare.this,
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}

	};

	/**
	 * 获取表单xml
	 * 
	 * @return 表单xml
	 */
	private String getFormsXML() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				webView.loadUrl("javascript:androidSave()");// 将表单数据拼装xml后写入手机文件
			}
		});
		Thread.currentThread();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return FileUtil.Load(this, Constants.user.getUSER_ID() + "_" + PERMID + "XML");// 从文件里面获取xml
	}

	private String getMaterialsXML() throws Exception {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);
		serializer.startDocument("utf-8", null);
		serializer.startTag("", "materials");
		for (int i = 0; i < applyBeans.size(); i++) {
			serializer.startTag("", "materialinfo");
			serializer.startTag("", "id");
			if (null != applyBeans.get(i).getCASEID()) {
				serializer.cdsect(applyBeans.get(i).getCASEID());
			}
			serializer.endTag("", "id");
			serializer.startTag("", "materialid");
			serializer.cdsect(applyBeans.get(i).getID());
			serializer.endTag("", "materialid");
			serializer.startTag("", "materialname");
			serializer.cdsect(applyBeans.get(i).getCLMC());
			serializer.endTag("", "materialname");
			serializer.startTag("", "materialcode");
			serializer.cdsect(applyBeans.get(i).getCLBH());
			serializer.endTag("", "materialcode");
			serializer.startTag("", "submittype");
			serializer.cdsect(applyBeans.get(i).getDZHYQ());
			serializer.endTag("", "submittype");
			serializer.startTag("", "orinum");
			serializer.cdsect(applyBeans.get(i).getORINUM());
			serializer.endTag("", "orinum");
			serializer.startTag("", "copynum");
			serializer.cdsect(applyBeans.get(i).getCOPYNUM());
			serializer.endTag("", "copynum");
			serializer.startTag("", "isneed");
			serializer.cdsect(applyBeans.get(i).getSFBY());
			serializer.endTag("", "isneed");
			serializer.startTag("", "status");

			if (null != Constants.material.get(applyBeans.get(i).getCLBH())
					&& Constants.material.get(applyBeans.get(i).getCLBH()).size() > 0) {// 设置提交状态
				serializer.cdsect("1");
			} else {
				serializer.cdsect("0");
			}

			serializer.endTag("", "status");
			serializer.startTag("", "formid");
			if (null != applyBeans.get(i).getFORMID()) {
				serializer.cdsect(applyBeans.get(i).getFORMID());
			}
			serializer.endTag("", "formid");
			serializer.startTag("", "formver");
			if (null != applyBeans.get(i).getFORMVER()) {
				serializer.cdsect(applyBeans.get(i).getFORMVER());
			}
			serializer.endTag("", "formver");
			serializer.startTag("", "dataid");
			serializer.cdsect("");
			serializer.endTag("", "dataid");
			if (!TextUtils.isEmpty(applyBeans.get(i).getCERTIFICATEID())) {
				serializer.startTag("", "certificateid");
				serializer.cdsect(applyBeans.get(i).getCERTIFICATEID());
				serializer.endTag("", "certificateid");
			} else {
				serializer.startTag("", "certificateid");
				serializer.cdsect("");
				serializer.endTag("", "certificateid");
			}
			if (!TextUtils.isEmpty(applyBeans.get(i).getCERTIFICATESTARTDATE())) {
				serializer.startTag("", "certificatestartdate");
				serializer.cdsect(applyBeans.get(i).getCERTIFICATESTARTDATE());
				serializer.endTag("", "certificatestartdate");
			} else {
				serializer.startTag("", "certificatestartdate");
				serializer.cdsect("");
				serializer.endTag("", "certificatestartdate");
			}
			if (!TextUtils.isEmpty(applyBeans.get(i).getCERTIFICATEENDDATE())) {
				serializer.startTag("", "certificateenddate");
				serializer.cdsect(applyBeans.get(i).getCERTIFICATEENDDATE());
				serializer.endTag("", "certificateenddate");
			} else {
				serializer.startTag("", "certificateenddate");
				serializer.cdsect("");
				serializer.endTag("", "certificateenddate");
			}

			serializer.startTag("", "remark");
			serializer.cdsect("");
			serializer.endTag("", "remark");
			serializer.startTag("", "username");
			serializer.cdsect(Constants.user.getREALNAME());
			serializer.endTag("", "username");

			if (null != Constants.material.get(applyBeans.get(i).getCLBH())) {// 设置文件节点

				for (int j = 0; j < Constants.material.get(applyBeans.get(i).getCLBH()).size(); j++) {
					serializer.startTag("", "file");

					serializer.startTag("", "fileid");
					serializer.cdsect(Constants.material.get(applyBeans.get(i).getCLBH()).get(j).getID());
					serializer.endTag("", "fileid");
					serializer.startTag("", "filename");
					serializer.cdsect(Constants.material.get(applyBeans.get(i).getCLBH()).get(j).getATTACHNAME());
					serializer.endTag("", "filename");
					serializer.startTag("", "filepath");
					serializer.cdsect(Constants.material.get(applyBeans.get(i).getCLBH()).get(j).getATTACHURL());
					serializer.endTag("", "filepath");
					serializer.startTag("", "id");
					serializer.cdsect(null == Constants.material.get(applyBeans.get(i).getCLBH()).get(j).getATTACHUID()
							? "" : Constants.material.get(applyBeans.get(i).getCLBH()).get(j).getATTACHUID());
					serializer.endTag("", "id");

					serializer.startTag("", "filedel");
					serializer.cdsect("0");
					serializer.endTag("", "filedel");

					serializer.endTag("", "file");
				}

			}

			serializer.endTag("", "materialinfo");
		}
		serializer.endTag("", "materials");
		serializer.endDocument();
		return writer.toString().replaceAll("<\\?xml.*.\\?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

	}

	private String getBusinessXML() throws Exception {
		Log.e("sps====", "YWGSDID==="+YWGSDID);
		Log.e("sps====", "YWGSDName==="+YWGSDName);

		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);
		serializer.startDocument("utf-8", null);
		serializer.startTag("", "business");

		serializer.startTag("", "cbusinessid");// 协同业务流水号
		serializer.cdsect(null == permission.getCBUSINESSID() ? "" : permission.getCBUSINESSID());
		serializer.endTag("", "cbusinessid");
		serializer.startTag("", "citemid");// 协同事项编号
		serializer.cdsect(null == permission.getCITEMID() ? "" : permission.getCITEMID());
		serializer.endTag("", "citemid");
		serializer.startTag("", "citemversion"); // 协同事项版本
		serializer.cdsect(null == permission.getCITEMVERSION() ? "" : permission.getCITEMVERSION());
		serializer.endTag("", "citemversion");

		serializer.startTag("", "businessid");
		if (null != BSNUM) {
			serializer.cdsect(BSNUM);
		}
		serializer.endTag("", "businessid");
		serializer.startTag("", "permid");
		serializer.cdsect(PERMID);
		serializer.endTag("", "permid");
		serializer.startTag("", "largeitemid");
		serializer.cdsect(permission.getLARGEITEMID());
		serializer.endTag("", "largeitemid");
		serializer.startTag("", "smallitemid");
		serializer.cdsect(permission.getSMALLITEMID());
		serializer.endTag("", "smallitemid");
		serializer.startTag("", "smallitemname");
		serializer.cdsect(permission.getSXZXNAME());
		serializer.endTag("", "smallitemname");
		serializer.startTag("", "itemversion");
		serializer.cdsect(permission.getITEMVERSION());
		serializer.endTag("", "itemversion");
		serializer.startTag("", "itemlimittime");
		serializer.cdsect(permission.getITEMLIMITTIME());
		serializer.endTag("", "itemlimittime");
		serializer.startTag("", "itemlimitunit");
		serializer.cdsect(permission.getITEMLIMITUNIT());
		serializer.endTag("", "itemlimitunit");
		serializer.startTag("", "regionid");
		serializer.cdsect(permission.getREGIONID());
		serializer.endTag("", "regionid");
		serializer.startTag("", "deptcode");
		serializer.cdsect(permission.getDEPTID());
		serializer.endTag("", "deptcode");
		serializer.startTag("", "deptname");
		serializer.cdsect(permission.getDEPTNAME());
		serializer.endTag("", "deptname");
		serializer.startTag("", "lawaddr");
		serializer.cdsect(permission.getLAWADDR());
		serializer.endTag("", "lawaddr");
		serializer.startTag("", "realaddr");
		serializer.cdsect(permission.getREALADDR());
		serializer.endTag("", "realaddr");
		serializer.startTag("", "status");
		serializer.cdsect(status);
		serializer.endTag("", "status");
		serializer.startTag("", "applicantid");
		serializer.cdsect(Constants.user.getUSER_ID());
		serializer.endTag("", "applicantid");
		serializer.startTag("", "state");
		serializer.cdsect(STATE);
		serializer.endTag("", "state");
		serializer.startTag("", "groupid");
		serializer.cdsect(null == P_GROUP_ID ? "" : P_GROUP_ID);
		serializer.endTag("", "groupid");
		serializer.startTag("", "groupname");
		serializer.cdsect(null == P_GROUP_NAME ? "" : P_GROUP_NAME);
		serializer.endTag("", "groupname");
		serializer.startTag("", "lzfs");
		serializer.cdsect(LzfsType);
		serializer.endTag("", "lzfs");

		serializer.startTag("", "ywgsd");
		serializer.cdsect(null==YWGSDID? "":YWGSDID);
		serializer.endTag("", "ywgsd");
		serializer.startTag("", "ywgsdmc");
		serializer.cdsect(null==YWGSDName? "":YWGSDName);
		serializer.endTag("", "ywgsdmc");
		
		serializer.endTag("", "business");
		serializer.endDocument();
		String xml = writer.toString();
		return xml;

	}

	/**
	 * 获取邮政速递xml
	 * 
	 */
	private String getPostXML() throws Exception {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);
		serializer.startDocument("utf-8", null);
		serializer.startTag("", "emsinfos");
		if (null != Constants.sendPostInfo) {
			serializer.startTag("", "emsinfo");

			serializer.startTag("", "ddhm");// 订单号码
			serializer.cdsect("");
			serializer.endTag("", "ddhm");
			serializer.startTag("", "ddmc");// 订单名称
			serializer.cdsect("");
			serializer.endTag("", "ddmc");
			serializer.startTag("", "ddlx");
			serializer.cdsect("1");// 订单类型,1:单向－材料，2：单向－证照，3：双向
			serializer.endTag("", "ddlx");

			serializer.startTag("", "item");
			serializer.startTag("", "type");
			serializer.cdsect("S");// 类型： S:申请人 D:业务部门 非空
			serializer.endTag("", "type");
			serializer.startTag("", "xm");
			serializer.cdsect(Constants.sendPostInfo.getRECEIVE());
			serializer.endTag("", "xm");
			serializer.startTag("", "dw");
			serializer.cdsect("");
			serializer.endTag("", "dw");
			serializer.startTag("", "dz");
			serializer.cdsect(Constants.sendPostInfo.getADDRESS());
			serializer.endTag("", "dz");
			serializer.startTag("", "dh");
			serializer.cdsect(Constants.sendPostInfo.getPHONE());
			serializer.endTag("", "dh");
			serializer.startTag("", "sj");
			serializer.cdsect("");
			serializer.endTag("", "sj");
			serializer.startTag("", "yb");
			serializer.cdsect("");
			serializer.endTag("", "yb");
			serializer.startTag("", "csdm");
			serializer.cdsect("");
			serializer.endTag("", "csdm");
			serializer.endTag("", "item");

			serializer.endTag("", "emsinfo");
		}
		if (null != Constants.getPostInfo) {// 改为getPostInfo
			serializer.startTag("", "emsinfo");

			serializer.startTag("", "ddhm");// 订单号码
			serializer.cdsect("");
			serializer.endTag("", "ddhm");
			serializer.startTag("", "ddmc");// 订单名称
			serializer.cdsect("");
			serializer.endTag("", "ddmc");
			serializer.startTag("", "ddlx");
			serializer.cdsect("2");// 订单类型,1:单向－材料，2：单向－证照，3：双向
			serializer.endTag("", "ddlx");

			serializer.startTag("", "item");
			serializer.startTag("", "type");
			serializer.cdsect("S");// 类型： S:申请人 D:业务部门 非空
			serializer.endTag("", "type");
			serializer.startTag("", "xm");
			serializer.cdsect(Constants.getPostInfo.getRECEIVE());
			serializer.endTag("", "xm");
			serializer.startTag("", "dw");
			serializer.cdsect("");
			serializer.endTag("", "dw");
			serializer.startTag("", "dz");
			serializer.cdsect(Constants.getPostInfo.getADDRESS());
			serializer.endTag("", "dz");
			serializer.startTag("", "dh");
			serializer.cdsect(Constants.getPostInfo.getPHONE());
			serializer.endTag("", "dh");
			serializer.startTag("", "sj");
			serializer.cdsect("");
			serializer.endTag("", "sj");
			serializer.startTag("", "yb");
			serializer.cdsect("");
			serializer.endTag("", "yb");
			serializer.startTag("", "csdm");
			serializer.cdsect("");
			serializer.endTag("", "csdm");
			serializer.endTag("", "item");

			serializer.endTag("", "emsinfo");
		}

		serializer.endTag("", "emsinfos");
		serializer.endDocument();
		String xml = writer.toString();
		return xml;

	}

	/**
	 * 
	 * @return 是否包含复杂表单
	 */
	private boolean cheackFormbyPC() {
		if (null != applyBeans) {
			for (int i = 0; i < applyBeans.size(); i++) {
				if (applyBeans.get(i).getDZHYQ().contains("1")) {
					return true;
				}

			}
		}
		return false;
	}

	/**
	 * 领证方式操作的一些初始化
	 */
	private void initLZFS() {
		LZFS_RG = (RadioGroup) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "radioGroup"));
		WDLQ = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "WDLQ"));
		ChooseAddress = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "YJLQ"));
		ZXDY = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "ZXDY"));

		address_rl = (RelativeLayout) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "addAddress_ll"));
		address_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HistoreShare.this, ExpressageList.class);
				startActivityForResult(intent, ADD_ADDRESS_REQUEST);
			}
		});
		parent_ll = (LinearLayout) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "parent_ll"));
		parent_ll.setVisibility(View.GONE);
		DJFS_RG = (RadioGroup) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "DJRidaoGroup"));
		WDDJ = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "WDDJ"));
		YJDJ = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "YJDJ"));

		WDDJlist = (ListViewForScrollView) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "WDDJ_address_list"));
		YJDJlist = (ListViewForScrollView) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "YJDJ_address_list"));
		WDLQlist = (ListViewForScrollView) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "WDLQ_address_list"));
		blmsTitle = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "blms_title"));
		emptyTextTip = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "empty_permid"));
		DJCLparent_ll = (LinearLayout) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "DJCLparent_ll"));
		LZFSparent_ll = (LinearLayout) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "LZFSparent_ll"));
		chooseDJCL_tv = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "chooseDJCL_tv"));
		chooseLZFS_tv = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "chooseLZFS_tv"));

		RECEIVE = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "RECEIVE"));
		PHONE = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "PHONE"));
		ADDRESS = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "ADDRESS"));
		address_Empty = (TextView) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "address_emptp_tip"));
		address_detail = (RelativeLayout) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "address_info_detail"));
		receiveTip=(TextView) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "recever_tip"));
		receiveTip.setText("承诺办理时限:"+permission.getITEMLIMITTIME()+"个工作日，具体结果以短信通知为主");
		if (STATUS != -1 && STATUS != 9) {
			chooseDJCL_tv.setText("递交材料方式：");
			chooseLZFS_tv.setText("领取证照方式：");
			address_rl.setClickable(false);
		}
		
		parentSpinner=(Spinner) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "parent_spinner"));
		childSpinner=(Spinner) LzfsPage
				.findViewById(MSFWResource.getResourseIdByName(this, "id", "child_spinner"));
		parentNames=new ArrayList<String>();
		parentAdapter = new ArrayAdapter<String>(HistoreShare.this, MSFWResource.getResourseIdByName(HistoreShare.this, "layout", "left_spinner"), parentNames);
		parentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		parentSpinner.setAdapter(parentAdapter);
		parentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				YWGSDID=parentItems.get(position).getAREAID();
				YWGSDName=parentItems.get(position).getAREANAME();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		childNames=new ArrayList<String>();
		childAdapter = new ArrayAdapter<String>(HistoreShare.this, MSFWResource.getResourseIdByName(HistoreShare.this, "layout", "left_spinner"), childNames);
		childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		childSpinner.setAdapter(childAdapter);
		childSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				YWGSDID=childItems.get(position).getAREAID();
				YWGSDName=childItems.get(position).getAREANAME();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		dialog=Background.Process(HistoreShare.this, getYWGSD,getString(MSFWResource.getResourseIdByName(HistoreShare.this, "string", "tj_loding")));
		
		/**
		 * 递交材料选择
		 */
		DJFS_RG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == WDDJ.getId()) {
					WDDJlist.setVisibility(View.VISIBLE);
					YJDJlist.setVisibility(View.GONE);
					Constants.sendPostInfo = null;
				} else if (checkedId == YJDJ.getId()) {
					WDDJlist.setVisibility(View.GONE);
					YJDJlist.setVisibility(View.VISIBLE);
					PostInfo sendPost = new PostInfo();
					if (NetWorkItems != null && NetWorkItems.size() != 0) {
						sendPost.setRECEIVE(NetWorkItems.get(0).getNETWORKNAME());
						sendPost.setPHONE(NetWorkItems.get(0).getNETWORKPHONE());
						sendPost.setPOSTCODE(NetWorkItems.get(0).getNETWORKPOSTCODE());
						sendPost.setADDRESS(NetWorkItems.get(0).getNETWORKADDRESS());
						Constants.sendPostInfo = sendPost;
					} else {
						Constants.sendPostInfo = null;
					}
					Constants.sendPostInfo = sendPost;
				}
			}
		});
		// 领证方式选择
		LZFS_RG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == WDLQ.getId()) {// 网点领取
					LzfsType = "D";
					address_rl.setVisibility(View.GONE);
					WDLQlist.setVisibility(View.VISIBLE);
					receiveTip.setVisibility(View.VISIBLE);
					Constants.getPostInfo = null;
				} else if (checkedId == ChooseAddress.getId()) {// 邮寄领取
					LzfsType = "EMS";
					address_rl.setVisibility(View.VISIBLE);
					WDLQlist.setVisibility(View.GONE);
					receiveTip.setVisibility(View.GONE);
					if (TextUtils.isEmpty(ADDRESS.getText().toString().trim())) {
						finish_lzfs.setImageResource(
								MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_no"));
					} else {
						finish_lzfs.setImageResource(
								MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_right_yes"));
					}
				} else if (checkedId == ZXDY.getId()) {// 自行打印
					LzfsType = "P";
					address_rl.setVisibility(View.GONE);
					WDLQlist.setVisibility(View.GONE);
					receiveTip.setVisibility(View.GONE);
					Constants.getPostInfo = null;
				}
			}
		});
		parent_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});

		if (STATUS != -1) {// 不是新申报
			if (STATUS == 9) {// 暂存件可以编辑
				ChooseAddress.setEnabled(true);
				WDLQ.setEnabled(true);
				ZXDY.setEnabled(true);
				WDDJ.setEnabled(true);
				YJDJ.setEnabled(true);
			} else {
				ChooseAddress.setEnabled(false);
				WDLQ.setEnabled(false);
				ZXDY.setEnabled(false);
				WDDJ.setEnabled(false);
				YJDJ.setEnabled(false);
			}
		} else {

			WDLQ.setChecked(true);
			WDDJ.setChecked(true);
			// YJDJlist.setVisibility(View.GONE);
			// address_rl.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取结果材料速递 和领证方式 2016/11/10修改接口 获取领取结果多返回一个LZFS 根据领证方式去显示界面
	 */
	final Runnable GetBusiPostInfo2 = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("TYPE", "2");// 速递类型（1递交纸质材料，2领取结果）
				param.put("BSNUM", BSNUM);
				String response = HTTP.excuteAndCache("getBusiPostInfo", "RestEMSService", param.toString(),
						HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					// postInfos2 = JSONUtil.getGson().fromJson(new
					// JSONObject(json.getString("ReturnValue")).getString("Items"),
					// new TypeToken<List<PostInfo>>() {
					// }.getType());
					emsBean = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), EMSBean.class);
					postInfos2 = emsBean.getItems();
					runOnUiThread(new Runnable() {
						public void run() {
							if (!TextUtils.isEmpty(emsBean.getLZFS())) {// 在10号之前暂存和申报没有lzfs传到服务器，兼容以前版本。
								if (STATUS == 9) {// 暂存件
									if (emsBean.getLZFS().equals("D")) {
										WDLQ.setChecked(true);
										address_rl.setVisibility(View.GONE);
									} else if (emsBean.getLZFS().equals("EMS")) {
										ChooseAddress.setChecked(true);
										address_rl.setVisibility(View.VISIBLE);
										if (postInfos2 != null && postInfos2.size() > 0) {
											address_detail.setVisibility(View.VISIBLE);
											address_Empty.setVisibility(View.GONE);
											RECEIVE.setText(postInfos2.get(0).getRECEIVE());
											PHONE.setText(postInfos2.get(0).getPHONE());
											ADDRESS.setText(postInfos2.get(0).getADDRESS());
										} else {
											address_detail.setVisibility(View.GONE);
											address_Empty.setVisibility(View.VISIBLE);
										}

									} else if (emsBean.getLZFS().equals("P")) {
										address_rl.setVisibility(View.GONE);
										ZXDY.setVisibility(View.VISIBLE);
										ZXDY.setChecked(true);
									}else{
										LZFSparent_ll.setVisibility(View.GONE);
									}
								} else {// 非暂存件
									if (emsBean.getLZFS().equals("D")) {
										address_rl.setVisibility(View.GONE);
										WDLQ.setChecked(true);
										WDLQ.setVisibility(View.VISIBLE);
										ChooseAddress.setVisibility(View.GONE);
										ZXDY.setVisibility(View.GONE);
									} else if (emsBean.getLZFS().equals("EMS")) {
										ZXDY.setVisibility(View.GONE);
										WDLQ.setVisibility(View.GONE);
										ChooseAddress.setChecked(true);
										ChooseAddress.setVisibility(View.VISIBLE);
										address_detail.setVisibility(View.VISIBLE);
										address_Empty.setVisibility(View.GONE);
										address_rl.setVisibility(View.VISIBLE);
										RECEIVE.setText(postInfos2.get(0).getRECEIVE());
										PHONE.setText(postInfos2.get(0).getPHONE());
										ADDRESS.setText(postInfos2.get(0).getADDRESS());
									} else if (emsBean.getLZFS().equals("P")) {
										ChooseAddress.setVisibility(View.GONE);
										address_rl.setVisibility(View.GONE);
										ZXDY.setVisibility(View.VISIBLE);
										ZXDY.setChecked(true);
										WDLQ.setVisibility(View.GONE);
									}else{
										LZFSparent_ll.setVisibility(View.GONE);
									}
								}
							} else if (null != postInfos2 && postInfos2.size() > 0) {// 兼容以前没有拼接领证方式到后台的处理
								if (STATUS == 9) {
									ChooseAddress.setChecked(true);
									ChooseAddress.setVisibility(View.VISIBLE);
									address_rl.setVisibility(View.VISIBLE);
									address_detail.setVisibility(View.VISIBLE);
									address_Empty.setVisibility(View.GONE);
									RECEIVE.setText(postInfos2.get(0).getRECEIVE());
									PHONE.setText(postInfos2.get(0).getPHONE());
									ADDRESS.setText(postInfos2.get(0).getADDRESS());
								} else {
									ZXDY.setVisibility(View.GONE);
									WDLQ.setVisibility(View.GONE);
									ChooseAddress.setChecked(true);
									ChooseAddress.setVisibility(View.VISIBLE);
									address_detail.setVisibility(View.VISIBLE);
									address_Empty.setVisibility(View.GONE);
									address_rl.setVisibility(View.VISIBLE);
									RECEIVE.setText(postInfos2.get(0).getRECEIVE());
									PHONE.setText(postInfos2.get(0).getPHONE());
									ADDRESS.setText(postInfos2.get(0).getADDRESS());

								}

							} else {
								if (STATUS == 9) {
									address_rl.setVisibility(View.GONE);
									WDLQ.setChecked(true);
								} else {
									ChooseAddress.setVisibility(View.GONE);
									address_rl.setVisibility(View.GONE);
									WDLQ.setChecked(true);
									ZXDY.setVisibility(View.GONE);
								}

							}
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	/**
	 * 获取递交材料速递
	 */
	final Runnable GetBusiPostInfo = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("TYPE", "1");// 速递类型（1递交纸质材料，2领取结果）
				param.put("BSNUM", BSNUM);
				String response = HTTP.excuteAndCache("getBusiPostInfo", "RestEMSService", param.toString(),
						HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					postInfos = JSONUtil.getGson().fromJson(
							new JSONObject(json.getString("ReturnValue")).getString("Items"),
							new TypeToken<List<PostInfo>>() {
					}.getType());
					if (null != postInfos && postInfos.size() > 0) {
						runOnUiThread(new Runnable() {
							public void run() {
								YJDJ.setChecked(true);
								YJDJlist.setVisibility(View.VISIBLE);
								WDDJlist.setVisibility(View.GONE);
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								YJDJlist.setVisibility(View.GONE);
								WDDJ.setChecked(true);
								WDDJlist.setVisibility(View.VISIBLE);
							}
						});

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	/**
	 * 根据事项编号获取服务网点信息
	 * 以后可能提供多网点选择
	 */
	final Runnable GetNetworkByPermid = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", PERMID);
				String response = HTTP.excuteAndCache("getNetworkByPermid", "RestNetworkService", param.toString(),
						HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					NetWorkItems = JSONUtil.getGson().fromJson(
							new JSONObject(json.getString("ReturnValue")).getString("Items"),
							new TypeToken<List<NetWorkBean>>() {
					}.getType());
					handler.sendEmptyMessage(GET_NETWORK_SUCCESS);
				} else {

				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	};
	/**
	 * 获取事项办理模式
	 */
	final Runnable GetPermWsfwsd = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", PERMID);
				String response = HTTP.excuteAndCache("getPermWsfwsd", "RestPermissionitemService", param.toString(),
						HistoreShare.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					blms = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), BLMSBean.class);
					handler.sendEmptyMessage(GET_BLMS_SUCCESS);

				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							parent_ll.setVisibility(View.GONE);
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	};

	/**
	 * 获取办理模式控制领证方式界面展示
	 */
	private void controllViewGetBLMS() {
		if (!TextUtils.isEmpty(blms.getLQSPJG())) {
			if (blms.getLQSPJG().contains("1")) {// 领证方式：含网点领取
				WDLQ.setVisibility(View.VISIBLE);
			} else {
				WDLQ.setVisibility(View.GONE);
			}
			if (blms.getLQSPJG().contains("2")) {// 领证方式：含EMS领取
				ChooseAddress.setVisibility(View.VISIBLE);
				if(STATUS==-1||STATUS==9){
					new Thread(GetUserPostInfo).start();
				}
			} else {
				ChooseAddress.setVisibility(View.GONE);
			}
			if (blms.getLQSPJG().contains("3")) {// 领证方式：含自行打印
				ZXDY.setVisibility(View.VISIBLE);
			} else {
				ZXDY.setVisibility(View.GONE);
			}
		} else {
			LZFSparent_ll.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(blms.getDJZZCL())) {
			if (blms.getDJZZCL().contains("1")) {// 递交材料：网点递交
				WDDJ.setVisibility(View.VISIBLE);
			} else {
				WDDJ.setVisibility(View.GONE);
			}
			if (blms.getDJZZCL().contains("2")) {// 递交材料：邮寄递交
				YJDJ.setVisibility(View.VISIBLE);
			} else {
				YJDJ.setVisibility(View.GONE);
			}
		} else {// 递交材料：没有递交材料方式递交材料布局不显示
			DJCLparent_ll.setVisibility(View.GONE);
		}
		if (blms.getBLMS() != null && !TextUtils.isEmpty(blms.getBLMS())) {
			if (blms.getBLMS().equals("1")) {
				blmsTitle.setText("办理模式:此事项全流程网上办理");
			} else if (blms.getBLMS().equals("2")) {
				blmsTitle.setText("办理模式:此事项线上申请、线上提交（受理环节递交纸质申请材料）");
			} else if (blms.getBLMS().equals("3")) {
				blmsTitle.setText("办理模式:此事项线上申请、线上提交（领证环节递交纸质申请材料） ");
			} else if (blms.getBLMS().equals("4")) {
				blmsTitle.setText("办理模式:此事项线上预约、线下提交 ");
			}
		} else {
			blmsTitle.setText("办理模式:暂无");
		}

	}
	
	/**
	 * 获取默认地址   默认第一条数据
	 */
	final Runnable GetUserPostInfo = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("USERID", Constants.user.getUSER_ID());

				String response = HTTP.excute("getUserPostInfo", "RestEMSService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					final List<PostInfo> postInfos = JSONUtil.getGson().fromJson(new JSONObject(json.getString("ReturnValue")).getString("Items"), new TypeToken<List<PostInfo>>() {
					}.getType());
					runOnUiThread(new Runnable() {
						public void run() {
							if (postInfos != null && postInfos.size() > 0) {
								address_detail.setVisibility(View.VISIBLE);
								address_Empty.setVisibility(View.GONE);
								RECEIVE.setText(postInfos.get(0).getRECEIVE());
								PHONE.setText(postInfos.get(0).getPHONE());
								ADDRESS.setText(postInfos.get(0).getADDRESS());
							} else {
								address_detail.setVisibility(View.GONE);
								address_Empty.setVisibility(View.VISIBLE);
							}
						}
					});

				} else {
//					DialogUtil.showUIToast(HistoreShare.this, "网络环境不稳定！");
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(HistoreShare.this, "网络环境不稳定");
				e.printStackTrace();
			}
		}
	};
	
	
	/**
	 * 获取业务归属地
	 */
	final Runnable getYWGSD = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", PERMID);
				if(STATUS!=9){
					param.put("BSNUM", BSNUM);
				}
				String response = HTTP.excute("getRegionByPermidBsnum", "RestRegionService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					YWGSDBean YWGSD = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), YWGSDBean.class);
					parentItems=YWGSD.getPARENTS();
					childItems=YWGSD.getCHILDREN();
					runOnUiThread(new Runnable() {
						public void run() {
							if(parentItems!=null&&parentItems.size()>0){
								for(int i=0;i<parentItems.size();i++){
									parentNames.add(parentItems.get(i).getAREANAME());
								}
								parentAdapter.notifyDataSetChanged();
							}else{
								parentNames.add("宝安区");
								parentAdapter.notifyDataSetChanged();
							}
							if(childItems!=null&&childItems.size()>0&&!parentItems.get(0).getAREAID().equals(childItems.get(0).getAREAID())){//如果业务归属地的子和父不一样，一样的话表示是同一个
								for(int i=0;i<childItems.size();i++){
									childNames.add(childItems.get(i).getAREANAME());
								}
								childAdapter.notifyDataSetChanged();
							}else{
								childSpinner.setVisibility(View.INVISIBLE);
							}
						}
					});

				} else {
//					DialogUtil.showUIToast(HistoreShare.this, "网络环境不稳定！");
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(HistoreShare.this, "网络环境不稳定");
				e.printStackTrace();
			}
		}
	};

	private void uploadFile(final ApplyBean applyBean, final int position) {

		new AlertDialog.Builder(HistoreShare.this).setTitle("请选择上传方式")
				.setItems(new String[] { "拍照上传", "选择文件" }, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int index) {
						switch (index) {
						case 0:// 拍照上传
							if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
								Intent intent = new Intent();
								intent.setClass(HistoreShare.this, TakePhotos.class);
								intent.putExtra("PERMID", PERMID);
								intent.putExtra("CLMC", applyBean.getCLMC());
								intent.putExtra("position", position);
								intent.putExtra("TYPE", 2 + "");
								intent.putExtra("FILE", applyBean);
								intent.putExtra("idString", "");
								intent.putExtra("startTimeString", "");
								intent.putExtra("endTimeString", "");

								startActivityForResult(intent, FILE_REQUEST);
							} else {
								DialogUtil.showUIToast(HistoreShare.this, getString(MSFWResource
										.getResourseIdByName(HistoreShare.this, "string", "tj_sdcard_unmonted_hint")));
							}

							break;
						case 1:// 选择文件上传

							if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
								Intent fileChooserIntent = new Intent();
								fileChooserIntent = new Intent(HistoreShare.this, FileChooserActivity.class);
								fileChooserIntent.putExtra("PERMID", PERMID);
								fileChooserIntent.putExtra("CLMC", applyBean.getCLMC());
								fileChooserIntent.putExtra("position", position);
								fileChooserIntent.putExtra("TYPE", 2 + "");
								fileChooserIntent.putExtra("FILE", applyBean);
								fileChooserIntent.putExtra("idString", "");
								fileChooserIntent.putExtra("startTimeString", "");
								fileChooserIntent.putExtra("endTimeString", "");

								startActivityForResult(fileChooserIntent, FILE_REQUEST);
							} else {
								DialogUtil.showUIToast(HistoreShare.this, getString(MSFWResource
										.getResourseIdByName(HistoreShare.this, "string", "tj_sdcard_unmonted_hint")));
							}

							break;

						default:
							break;
						}

					}
				}).setNegativeButton("取消", null).show();
	}

	public class ApplyBeanAdapter extends BaseAdapter {
		private Context mContext;
		private List<ApplyBean> data;
		private List<ATTBean> atts;// 文件集合
		private DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true)
				.build();

		public ApplyBeanAdapter(Context context, List<ApplyBean> data) {
			// TODO Auto-generated constructor stub
			this.mContext = context;
			this.data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ApplyBeanHodler hodler = null;
			// if(convertView==null){ 取消复用 复用导致att材料重复
			hodler = new ApplyBeanHodler();
			convertView = LayoutInflater.from(mContext).inflate(
					MSFWResource.getResourseIdByName(mContext, "layout", "item_applybean_list"), parent, false);
			hodler.nameTitle = (TextView) convertView
					.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "nameTitle"));
			hodler.imags_parent = (LinearLayout) convertView
					.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "imags_parent_ll"));
			hodler.status = (ImageView) convertView
					.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "status"));
			hodler.add_img = (ImageView) convertView
					.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "addImage"));
			hodler.SFBY=(TextView) convertView
					.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "SFBY"));
			hodler.moreTip_img = (ImageView) convertView
					.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "more_title_iv"));
			hodler.SHYJ=(TextView) convertView
					.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "SHYJ"));

			// convertView.setTag(hodler);

			// }else {
			// hodler = (ApplyBeanHodler) convertView.getTag();
			//
			// }
			final ApplyBean applyBean = data.get(position);
			hodler.nameTitle.setText(applyBean.getCLMC());
			// && (null == applyBean.getFILEID() ||
			// applyBean.getFILEID().equals(""))// 必要提交除窗口递交外
			if (applyBean.getSFBY().equals("1")) {
				hodler.SFBY.setVisibility(View.VISIBLE);
			} else {
				hodler.SFBY.setVisibility(View.GONE);
			}
			if (applyBean.getSTATUS().equals("1")) {
				hodler.status
						.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_right_yes"));
			} else {
				hodler.status
						.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "no_submit"));
			}
			hodler.moreTip_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showTitleTipDialog(applyBean.getCLMC());
				}
			});
			
			// 审核意见
			if (null != data && applyBean != null && applyBean.getSH() != null && applyBean.getSH().equals("0")) {
				hodler.SHYJ.setVisibility(View.VISIBLE);
				hodler.SHYJ.setText(applyBean.getSHYJ());
			} else {
				hodler.SHYJ.setVisibility(View.GONE);
			}
			Log.e("Histrore", "SHYJ==="+applyBean.getSHYJ()+"   SH==="+applyBean.getSH());
			atts = Constants.material.get(applyBean.getCLBH());

			if (atts != null && atts.size() > 0) {
				for (int i = 0; i < (atts.size() > 6 ? 6 : atts.size()); i++) {
					ImageView image = new ImageView(mContext);
					LayoutParams lp = new LayoutParams(DensityUtil.dip2px(HistoreShare.this, 40),
							DensityUtil.dip2px(HistoreShare.this, 40));
					lp.width = DensityUtil.dip2px(HistoreShare.this, 40 - 10);
					lp.height = DensityUtil.dip2px(HistoreShare.this, 40 - 10);
					lp.setMargins(DensityUtil.dip2px(HistoreShare.this, 5), DensityUtil.dip2px(HistoreShare.this, 3),
							DensityUtil.dip2px(HistoreShare.this, 5), DensityUtil.dip2px(HistoreShare.this, 3));
					image.setLayoutParams(lp);
					image.setScaleType(ScaleType.FIT_XY);
					final String end = atts.get(i).getATTACHNAME()
							.substring(atts.get(i).getATTACHNAME().lastIndexOf(".") + 1,
									atts.get(i).getATTACHNAME().length())
							.toLowerCase(Locale.US);

					if ("jpg".equals(end) || "png".equals(end) || "gif".equals(end) || "jpeg".equals(end)
							|| "bmp".equals(end)) {
						ImageLoader.getInstance().displayImage(
								Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + atts.get(i).getID(), image,
								options);
					} else if ("doc".equals(end) || "docx".equals(end) || "docm".equals(end) || "dotx".equals(end)
							|| "dotx".equals(end) || "dotm".equals(end)) {

						image.setImageResource(
								MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_ic_word")); // word文档文件
					} else if ("pdf".equals(end)) {
						image.setImageResource(
								MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_ic_pdf")); // pdf文件
					} else if ("xls".equals(end) || "xlsx".equals(end) || "xlsm".equals(end) || "xltx".equals(end)
							|| "xltm".equals(end) || "xlsb".equals(end) || "xlam".equals(end)) {
						image.setImageResource(
								MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_ic_excel")); // excel文件
					} else {
						image.setImageResource(
								MSFWResource.getResourseIdByName(HistoreShare.this, "drawable", "tj_ic_file_unknown"));
					}
					hodler.imags_parent.addView(image);
				}
			}

			hodler.add_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (STATUS == -1 || STATUS == 9 || STATUS == 4) {
						uploadFile(applyBean, position);
					}
				}
			});
			hodler.imags_parent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("applyBean", applyBean);
					intent.putExtra("PERMID", PERMID);
					intent.putExtra("position", position);
					intent.putExtra("TYPE", 2);
					intent.putExtra("from", "HistoreShare");
					intent.putExtra("STATUS", STATUS);
					intent.putExtra("flag", 1);
					intent.putExtra("mark", mark);
					intent.setClass(HistoreShare.this, MaterialManage.class);
					startActivityForResult(intent, FILE_REQUEST);
				}
			});
			return convertView;
		}

		class ApplyBeanHodler {
			private TextView nameTitle;
			private LinearLayout imags_parent;
			private ImageView status;
			private ImageView add_img;
			private TextView SFBY;
			private ImageView moreTip_img;
			private TextView SHYJ;
		}
	}
	
	private void showTitleTipDialog(String title){
		AlertDialog.Builder builder=new Builder(HistoreShare.this);
		builder.setMessage(title);
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create();
		builder.show();
	}
}
