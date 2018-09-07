package com.tjsoft.webhall.ui.work;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.CaptureActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.AuthUtil;
import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.ImageLoadUtils2;
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
import com.tjsoft.webhall.entity.CompanyBean;
import com.tjsoft.webhall.entity.CompanyList;
import com.tjsoft.webhall.entity.EMSBean;
import com.tjsoft.webhall.entity.EditCompanyBean;
import com.tjsoft.webhall.entity.Form;
import com.tjsoft.webhall.entity.FormItemBean;
import com.tjsoft.webhall.entity.InsIncExt;
import com.tjsoft.webhall.entity.NetWorkBean;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.PostInfo;
import com.tjsoft.webhall.entity.Region;
import com.tjsoft.webhall.entity.ShareMaterial;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.UploadStatus;
import com.tjsoft.webhall.entity.UserCacheDetail;
import com.tjsoft.webhall.entity.UserDetail;
import com.tjsoft.webhall.entity.WSFWSD;
import com.tjsoft.webhall.entity.YWGSDBean;
import com.tjsoft.webhall.entity.YWGSDV2Bean;
import com.tjsoft.webhall.imp.CompanyCallback;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.lib.NoTouchViewPage;
import com.tjsoft.webhall.ui.bsdt.WDBJ;
import com.tjsoft.webhall.ui.expressage.ExpressageChoose;
import com.tjsoft.webhall.ui.expressage.ExpressageList;
import com.tjsoft.webhall.ui.expressage.ExpressageProgress;
import com.tjsoft.webhall.ui.materialmanage.MaterialManageActivity;
import com.tjsoft.webhall.ui.search.SearchSchedule;
import com.tjsoft.webhall.ui.work.companylist.CompanyListActivity;
import com.tjsoft.webhall.ui.wsbs.YWGSDAdapter;
import com.tjsoft.webhall.widget.AddCompanyDialog;
import com.tjsoft.webhall.widget.EnsureApplyDialog;
import com.tjsoft.webhall.widget.RoundAngleImageView;

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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 历史共享、在线申报
 * 证照界面证照信息数据在这个界面没有用到了----licensePage  licenseDate  HistoreShare_v2Adapter
 *
 * @author Administrator
 */
@SuppressLint("SetJavaScriptEnabled")
public class HistoreShare_v2
        extends AutoDialogActivity
{

    private static final int REQUESTCODE_COMPANY = 102;
    public static final  int REQUESTCODE_DZZZK   = 103;
    private NoTouchViewPage viewPager;// 页卡内容
    private ImageView       imageView;// 动画图片
    private TextView        textView1, textView0, textView3;// 滑动头部
    private List<TextView> titles;
    private List<View>     views;// Tab页面列表
    private int offset    = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW      = 0;// 动画图片宽度
    private View userInfoPage, formPage, bigFilePage, LzfsPage;// 各个页卡 新添加领证方式
    private DisplayMetrics dm;// 设备管理
    private LayoutInflater inflater;
    private Button         save, goBack2, next;
    private RelativeLayout back;
    private Intent         intent;
    private WebView        webView;
    private Permission     permission;
    private String PERMID = "";
    private List<Form> forms;
    private int index = 0;
    private       List<FormItemBean> formItemBeans;// 表单实例list
    public static List<ApplyBean>    applyBeans, bigFileDate;
    private       Handler handler                = new MyHandler();
    private final int     GET_APPLY_BEAN_SUCCESS = 2;
    private final int     GET_BLMS_SUCCESS       = 4;
    private ListView bigFileList;
    private String mark = "6";
    private TextView     title;
    private LinearLayout applyLay, formBtn;
    private Button applySave, applySubmit;
    private String status = "0";//申报状态为暂存或者申报
    public  String STATE  = "5";// 表示手机申报
    private int    STATUS = -1;//0是已申报  9 是暂存  -1是新申报
    private JSONArray dataidArray;
    private String    MATERIALS;
    private String formsXML = "";// 表单信息

    private PostInfo POSTINFO;//邮寄信息
    private PostInfo SENDINFO;//寄件人信息
    private boolean isCompanyFirst = true;//第一次进来companybean要赋值

    private Region foreRegion = new Region();//上一个选择的业务归属地
    ApplyBeanAdapter applyBeanAdapter;

    ApplyBeanAdapterv2 applyBeanAdapterv2;


    public static final String EXTRA_FILE_CHOOSER = "file_chooser";

    protected LinearLayout main, applyLayNoPre, titleBar;
    protected Button exit, applySubmitNoPre, applySaveNoPre;
    private String  P_GROUP_ID;// 分组id
    private String  P_GROUP_NAME;// 分组名称
    private Context mContext;
    private boolean showCert  = false;
    private int     pageCount = 4;
    private Button ems, btnFlow;
    private TextView  tvIdea;
    private ImageView finish_userinfo, finish_form, finish_file, finish_lzfs;// 完成申报的箭头指向
    private boolean isFinishUserInfo, isFinishForm, isFinishFile;

    /*
     * 以下为新添加的领证方式的变量   根据4个接口界面展示。。。
     */
    private TextView       textView4;// 领证方式头部
    private RadioGroup     LZFS_RG;// 领证方式选择
    private RadioButton    WDLQ;// 网点领取
    private RadioButton    chooseAddress;// 邮寄领取
    private RadioButton    ZXDY;// 自行打印
    private RadioGroup     DJFS_RG;// 递交材料方式选择
    private RadioButton    WDDJ;// 网点递交
    private RadioButton    YJDJ;// 邮寄递交
    private RadioButton    WXDJZZCL;//无须递交纸质材料
    private RelativeLayout address_rl;// 邮寄选择后的整个布局
    private LinearLayout   wdlqParentRl;//网点领取父布局
    private LinearLayout   parent_ll;// 父布局 点击隐藏输入法
    private List<PostInfo> postInfos, postInfos2;// 从服务得到领证信息
    private BLMSBean     blmsBean;// 办理模式实体
    private TextView     blmsTitle;
    private LinearLayout DJCLparent_ll;// 递交材料整个布局
    private LinearLayout LZFSparent_ll;// 领证方式整个布局
    private String LzfsType = "";// 选择领证方式类别 D:网点领取，EMS:邮政速递，P：自行打印
    private EMSBean        emsBean;// 获取邮政速递地址及领证方式
    private TextView       chooseLZFS_tv;// 选择领证方式
    private TextView       chooseDJCL_tv;// 选择递交材料方式
    private TextView       RECEIVE;// 收件人
    private TextView       PHONE;// 手机号
    private TextView       ADDRESS;// 联系地址
    private TextView       addressEmpty;// 空地址
    private RelativeLayout addressDetail;// 领证邮寄地址详情
    private TextView       receiveTip;
    private final int ADD_ADDRESS_REQUEST      = 100;
    private final int ADD_ADDRESS_REQUEST_SEND = 101;
    private final int FILE_REQUEST             = 66;
    private Spinner              parentSpinner;
    private Spinner              childSpinner;
    private Spinner              thirdSpinner;
    private List<Region>         parentItems;
    private List<Region>         childItems;
    private ArrayAdapter<String> parentAdapter;
    private ArrayAdapter<String> childAdapter;
    private ArrayAdapter<String> thirdAdapter;
    private List<String>         parentNames;
    private List<String>         childNames;
    private List<String>         thirdNames;
    private String               YWGSDID, YWGSDName;//业务归属地名称和id
    private TextView wddjTip, yjdjTip, wxdjzzclTip, wdlqTip, djAddr, lqAddr;//网点递交  邮寄递交 无需递交纸质材料   网点领取提示  递交网点地址，领取地址
    private LinearLayout ywgsdParent;//业务归属地整个布局
    private TextView     zxdyTipTv;
    private TextView     tvGongzhonghao;//公众号提示

    private ImageView      home;
    private ImageView      help;
    private RelativeLayout baseForm_title_rl, fileUpload_title_rl, lzfs_title_rl, user_info_title_rl;
    private RelativeLayout rl_address_send;
    private TextView       SEND;
    private TextView       PHONE_SEND;
    private TextView       ADDRESS_SEND;
    private TextView       tv_temp_myaddress;
    private RelativeLayout rl_myaddresss_detail;
    private TextView       yzlqTip;
    private TextView       tvFuzatip;//复杂表单提示语，只有是新申报和暂存件显示这个，已经申报的不显示这个
    private ImageView      ivScan;//复杂表单扫描图标，只有是复杂件并且是暂存件时才显示这个


    private TextView        tvYwgsd;
    private TransportEntity transportEntity;
    String[] layers; //业务归属地层级
    int     level         = 0; //业务归属地第几层
    String  parentId      = ""; //业务归属地parent
    int     flag          = 0;//判断是不是第一次进来
    boolean isZanCunEmpty = false;
    private String            sffzbd;//是否复杂表单
    private List<NetWorkBean> networks;
    private String BSNUM = "";
    GloabDelegete gloabDelegete = Constants.getInstance()
                                           .getGloabDelegete();
    private ImageView      photo;
    private RelativeLayout rl_shiming;
    private LinearLayout   enterprise_info;
    private TextView       userName, phone, realName, idcardNum, INC_NAME, INC_ZZJGDM, INC_DEPUTY, INC_PID, realUserAuth, auth_notice;

    //企业列表相关
    private RecyclerView   rvCompanyList;
    private LinearLayout   llChooseType;
    private TextView       tvChooseType;
    private RelativeLayout rlAddCompany;
    private RadioGroup     rgChooseType;
    private RadioButton    rbGrbs;
    private RadioButton    rbQybs;
    private LinearLayout   ll_choose_type_out;
    private LinearLayout   ll_no_company;

    public  String            qymc;
    public  String            tyxydm;
    public  String            frdb;
    public  String            frsfzhm;
    private CompanyAdapter    companyAdapter;
    private List<CompanyBean> companyBeanList;
    private List<CompanyBean> mycompanyBeanList;
    private String            deleteId;
    private String            selectedCompanyId;
    private String            selectedCompanyIsreal;
    private String            selectedCompanyIsAuthFaren;
    private String            INC_EXT_ID;
    private CompanyBean       companyBean;
    private String AuthType        = "0";
    private String isselectCompany = "0";
    private String inc_zzjgdm;
    private String inc_jgmc;
    private String inc_gszch;
    private String inc_jycs;
    private String inc_deputy;
    private String mSaveinc_ext_id;
    private String ywgsd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatisticsTools.start();
        mContext = this;
        setContentView(R.layout.tj_history_share);
        //        EventBus.getDefault().register(this);
        Constants.getInstance()
                 .addActivity(this);
        Constants.getPostInfo = null;
        Constants.sendPostInfo = null;
        P_GROUP_ID = getIntent().getStringExtra("P_GROUP_ID");
        P_GROUP_NAME = getIntent().getStringExtra("P_GROUP_NAME");
        permission = (Permission) getIntent().getSerializableExtra("permission");

        PERMID = permission.getID();
        STATUS = getIntent().getIntExtra("STATUS", -1);
        // mark = getIntent().getStringExtra("mark");//暂时屏蔽  好像没有用到了。。。
        BSNUM = getIntent().getStringExtra("BSNUM");
        // init();
        //获取材料列表
        if (STATUS == -1) {// 新申报
            dialog = Background.Process(HistoreShare_v2.this,
                                        GetApplyList,
                                        getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                   "string",
                                                                                   "tj_loding")));
        } else {// 在办件
            dialog = Background.Process(HistoreShare_v2.this,
                                        GetApplyListByBS,
                                        getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                   "string",
                                                                                   "tj_loding")));


        }
        findViewById(MSFWResource.getResourseIdByName(this,
                                                      "id",
                                                      "version")).setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(HistoreShare_v2.this, "20170914", Toast.LENGTH_SHORT)
                     .show();
                return false;
            }
        });
        //        applyBeanAdapter = new ApplyBeanAdapter(HistoreShare_v2.this, bigFileDate);
        applyBeanAdapterv2 = new ApplyBeanAdapterv2(HistoreShare_v2.this, bigFileDate);


        //复杂表单的提示
        tvFuzatip = (TextView) findViewById(R.id.tv_fuzha_tip);
        ivScan = (ImageView) findViewById(R.id.iv_scan);
        ivScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HistoreShare_v2.this).setMessage(
                        "该事项包含复杂表单!请先前往电脑端打开（http://bsdt.baoan.gov.cn/appscan），然后扫描二维码继续填报相关信息")
                                                             .setTitle("提示")
                                                             .setCancelable(false)
                                                             .setPositiveButton("去扫描",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(
                                                                                            DialogInterface dialog,
                                                                                            int id)
                                                                                    {
                                                                                        intent = new Intent();
                                                                                        intent.putExtra(
                                                                                                "flag",
                                                                                                3);
                                                                                        intent.putExtra(
                                                                                                "SXID",
                                                                                                PERMID);
                                                                                        intent.putExtra(
                                                                                                "BSNUM",
                                                                                                BSNUM);
                                                                                        intent.setClass(
                                                                                                HistoreShare_v2.this,
                                                                                                CaptureActivity.class);
                                                                                        startActivityForResult(
                                                                                                intent,
                                                                                                100);// 二维码扫描;
                                                                                    }
                                                                                })
                                                             .setNegativeButton("取消",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(
                                                                                            DialogInterface dialog,
                                                                                            int which)
                                                                                    {

                                                                                        dialog.dismiss();

                                                                                    }
                                                                                })
                                                             .show();
            }
        });

        String                 smtip = "提示：该事项包含复杂表单，须先暂存，暂存成功之后前往电脑端打开http://bsdt.baoan.gov.cn/appscan并扫描二维码继续申报";
        SpannableStringBuilder ssb   = new SpannableStringBuilder(smtip);
        ssb.setSpan(new UnderlineSpan(), 31, 63, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(Color.BLACK),
                    31,
                    63,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvFuzatip.setText(ssb);
        //判断是否复杂表单,这里是初始化的时候
        //        dialog = Background.Process(HistoreShare_v2.this, checkFZBDv2,
        //                getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this, "string", "tj_loding")));
        new Thread(checkFZBDv2).start();
    }

    @Override
    public void onBackPressed() {
        //暂存和新申报
        if (STATUS == -1 || STATUS == 9) {
            new AlertDialog.Builder(this).setMessage("是否确定退出当前事项办理？退出将会丢失部分录入信息，您可以选择暂存功能保持当前申报进度。")
                                         .setTitle("提示")
                                         .setCancelable(false)
                                         .setPositiveButton("确认退出",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog,
                                                                                    int id)
                                                                {
                                                                    finish();
                                                                }
                                                            })
                                         .setNegativeButton("取消",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog,
                                                                                    int id)
                                                                {
                                                                    dialog.dismiss();
                                                                }
                                                            })
                                         .show();
        } else {
            finish();
        }

    }

    /**
     * 获取网上服务深度           没用到的代码     以前可能有用到
     */
    final Runnable GetWsfwsdByPermid = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("PERMID", permission.getID());
                String response = HTTP.excuteAndCache("getWsfwsdByPermid",
                                                      "RestEMSService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {

                    WSFWSD wsfwsd = JSONUtil.getGson()
                                            .fromJson(json.getString("ReturnValue"), WSFWSD.class);
                    if (null != wsfwsd.getDJZZCL() && wsfwsd.getDJZZCL()
                                                            .equals("2"))// 速递服务
                    {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ems.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                    if (null != wsfwsd.getLQSPJG() && wsfwsd.getLQSPJG()
                                                            .equals("2"))
                    {// 速递服务

                        runOnUiThread(new Runnable() {
                            public void run() {
                                ems.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this, json.getString("error"));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }
    };

    private void init() {

        InitImageView();//indicator初始化
        InitTextView();//初始化tab标题
        InitViewPager();
        InitView1();// 页面初始化
        InitApply();// 初始化在线申
        initSetOnListener();
        initUserInfo();
        if (STATUS != -1 && STATUS != 4 && STATUS != 9) {
            applyLay.setVisibility(View.GONE);
            formBtn.setVisibility(View.GONE);
            // webView.loadUrl("javascript:setreadonly()");//设置表单不可编辑

        }
        if (STATUS == 4 || STATUS == 8 || STATUS == 10) {// 是否显示意见

            new Thread(GetAdvice).start();

        }

        // new Thread(GetWsfwsdByPermid).start();
        dialog = Background.Process(HistoreShare_v2.this,
                                    GetFormByPermid,
                                    getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                               "string",
                                                                               "tj_loding")));// 获取表单
        if (STATUS != -1) {
            dialog = Background.Process(HistoreShare_v2.this,
                                        getInsIncExt,
                                        getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                   "string",
                                                                                   "tj_loding")));
        }

    }

    /*
    * 初始化用户信息
    * */
    private void initUserInfo() {
        photo = (ImageView) userInfoPage.findViewById(R.id.photo);
        enterprise_info = (LinearLayout) userInfoPage.findViewById(R.id.enterprise_info);
        rl_shiming = (RelativeLayout) userInfoPage.findViewById(R.id.rl_shiming);
        userName = (TextView) userInfoPage.findViewById(R.id.userName);
        phone = (TextView) userInfoPage.findViewById(R.id.phone);
        realName = (TextView) userInfoPage.findViewById(R.id.realName);
        idcardNum = (TextView) userInfoPage.findViewById(R.id.idcardNum);
        INC_NAME = (TextView) userInfoPage.findViewById(R.id.INC_NAME);
        INC_ZZJGDM = (TextView) userInfoPage.findViewById(R.id.INC_ZZJGDM);
        INC_DEPUTY = (TextView) userInfoPage.findViewById(R.id.INC_DEPUTY);
        INC_PID = (TextView) userInfoPage.findViewById(R.id.INC_PID);
        realUserAuth = (TextView) userInfoPage.findViewById(R.id.realUserAuth);
        auth_notice = (TextView) userInfoPage.findViewById(R.id.auth_notice);


        rvCompanyList = (RecyclerView) userInfoPage.findViewById(R.id.rv_company_list);
        rvCompanyList.setLayoutManager(new StaggeredGridLayoutManager(1,
                                                                      StaggeredGridLayoutManager.VERTICAL));
        companyAdapter = new CompanyAdapter(HistoreShare_v2.this,
                                            rvCompanyList,
                                            null,
                                            false,
                                            new CompanyCallback() {
                                                @Override
                                                public void deleteCompany(String id) {


                                                }

                                                @Override
                                                public void selectCompany(CompanyBean item,
                                                                          int position,
                                                                          boolean isSelected)
                                                {

                                                }

                                                @Override
                                                public void toAuth(final CompanyBean item) {
                                                    if (STATUS != 0) {

                                                        if (!transportEntity.isRealUserAuth()) {
                                                            DialogUtil.showToast(HistoreShare_v2.this,
                                                                                 "经办人必须先实名认证");

                                                        } else {
                                                            AddCompanyDialog addCompanyDialog = new AddCompanyDialog(
                                                                    HistoreShare_v2.this,
                                                                    item,
                                                                    new AddCompanyDialog.Confirm() {


                                                                        @Override
                                                                        public void onConfirm(String myqymc,
                                                                                              String mytyxydm,
                                                                                              String myfrdb,
                                                                                              String myfrsfzhm)
                                                                        {

                                                                            qymc = myqymc;
                                                                            tyxydm = mytyxydm;
                                                                            frdb = myfrdb;
                                                                            frsfzhm = myfrsfzhm;
                                                                            INC_EXT_ID = item.getINC_EXT_ID();
                                                                            AuthType = "1";
                                                                            dialog = Background.Process(
                                                                                    HistoreShare_v2.this,
                                                                                    editCompany,
                                                                                    getString(
                                                                                            MSFWResource.getResourseIdByName(
                                                                                                    HistoreShare_v2.this,
                                                                                                    "string",
                                                                                                    "tj_loding")));// 获取表单

                                                                        }
                                                                    });
                                                        }
                                                    }


                                                }

                                                @Override
                                                public void onClick(boolean isList,
                                                                    CompanyBean companyBean)
                                                {

                                                    if (STATUS != 0) {
                                                        if (!transportEntity.isRealUserAuth()) {
                                                            DialogUtil.showToast(HistoreShare_v2.this,
                                                                                 "经办人必须先实名认证");

                                                        } else {
                                                            Intent intent = new Intent(
                                                                    HistoreShare_v2.this,
                                                                    CompanyListActivity.class);
                                                            intent.putExtra("token",
                                                                            Constants.user.getTOKEN());
                                                            intent.putExtra("USERID",
                                                                            Constants.user.getUSER_ID());
                                                            intent.putExtra("userPid",
                                                                            transportEntity.getIdcardNum());
                                                            startActivityForResult(intent,
                                                                                   REQUESTCODE_COMPANY);
                                                        }
                                                    }


                                                }

                                                @Override
                                                public void toAuthFaren(CompanyBean item) {
                                                    if (STATUS != 0) {

                                                        AuthUtil.startHuaXunFaceAuthFaren(
                                                                HistoreShare_v2.this,
                                                                item.getINC_DEPUTY(),
                                                                item.getINC_PID());

                                                    }

                                                }
                                            });
        rvCompanyList.setAdapter(companyAdapter);


        llChooseType = (LinearLayout) userInfoPage.findViewById(R.id.ll_choose_type);
        tvChooseType = (TextView) userInfoPage.findViewById(R.id.tv_choose_type);
        rlAddCompany = (RelativeLayout) userInfoPage.findViewById(R.id.add_company);
        rgChooseType = (RadioGroup) userInfoPage.findViewById(R.id.rg_choose_type);
        rbGrbs = (RadioButton) userInfoPage.findViewById(R.id.rb_grbs);
        rbQybs = (RadioButton) userInfoPage.findViewById(R.id.rb_qybs);
        ll_choose_type_out = (LinearLayout) userInfoPage.findViewById(R.id.ll_choose_type_out);
        ll_no_company = (LinearLayout) userInfoPage.findViewById(R.id.ll_no_company);


        if (TextUtils.equals(permission.getUSERTYPE(), "0"))

        {


            rbGrbs.setChecked(true);
            rvCompanyList.setVisibility(View.GONE);
            ll_no_company.setVisibility(View.GONE);

        } else if (TextUtils.equals(permission.getUSERTYPE(), "1"))

        {//个人
            rbGrbs.setChecked(true);

            rbQybs.setVisibility(View.GONE);
            rlAddCompany.setVisibility(View.GONE);
            tvChooseType.setText("该事项支持个人办事");
            rvCompanyList.setVisibility(View.GONE);
            ll_no_company.setVisibility(View.GONE);


        } else if (TextUtils.equals(permission.getUSERTYPE(), "2"))

        {//企业
            rlAddCompany.setVisibility(View.GONE);

            rbGrbs.setVisibility(View.GONE);
            rbQybs.setChecked(true);
            tvChooseType.setText("该事项支持企业办事");
            rvCompanyList.setVisibility(View.VISIBLE);
            ll_no_company.setVisibility(View.VISIBLE);
        } else {
            rbGrbs.setChecked(true);

            rvCompanyList.setVisibility(View.GONE);
            ll_no_company.setVisibility(View.GONE);
        }


        if (gloabDelegete != null)

        {
            transportEntity = gloabDelegete.getUserInfo();
            System.out.println("transportEntity--------" + transportEntity.toString());
            userName.setText("账号：" + transportEntity.getUserId());
            phone.setText("手机号：" + transportEntity.getLoginPhone());
            realName.setText("姓名：" + transportEntity.getRealName());
            idcardNum.setText("身份证号：" + transportEntity.getIdcardNum());
            INC_NAME.setText("企业名称：" + transportEntity.getINC_NAME());
            INC_ZZJGDM.setText("统一社会信用代码：" + transportEntity.getINC_ZZJGDM());
            INC_DEPUTY.setText("法人代表：" + transportEntity.getINC_DEPUTY());
            INC_PID.setText("法人身份证号：" + transportEntity.getINC_PID());
            //            if (!TextUtils.isEmpty(transportEntity.getUserType()) && transportEntity.getUserType().equals("2")) {//用户类型  1.个人,2企业
            //                enterprise_info.setVisibility(View.VISIBLE);
            //            }

            rl_shiming.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthUtil.startHuaXunFaceAuth(HistoreShare_v2.this, true);
                }
            });
            if (transportEntity.isRealUserAuth()) {
                realUserAuth.setText("（已实名）");
                rl_shiming.setVisibility(View.GONE);
            } else {
                realUserAuth.setText("（未实名）");
                rl_shiming.setVisibility(View.VISIBLE);
            }
            Log.e("Constants.user", Constants.user.getTYPE() + "===vvv");
            Log.e("Constants.user", transportEntity.getUserType() + "===111");

            if (TextUtils.equals(Constants.user.getTYPE(), "1"))

            {

                //                if (STATUS == -1) {
                new Thread(getCompanyList).start();

                //                } else {
                //                    enterprise_info.setVisibility(View.VISIBLE);
                //                    ll_choose_type_out.setVisibility(View.GONE);
                //                    rgChooseType.setVisibility(View.GONE);
                //                    rvCompanyList.setVisibility(View.GONE);
                //                }


            } else if (TextUtils.equals(Constants.user.getTYPE(), "2")) {

                Log.e("Constants.user", Constants.user.getTYPE() + "===xxx");

                enterprise_info.setVisibility(View.VISIBLE);
                ll_choose_type_out.setVisibility(View.GONE);
                rgChooseType.setVisibility(View.GONE);
                rvCompanyList.setVisibility(View.GONE);
            }
        }
        if (!

                checkUserInfo())

        {
            auth_notice.setVisibility(View.VISIBLE);
        }

        rbGrbs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbGrbs.isChecked()) {
                    rvCompanyList.setVisibility(View.GONE);
                    ll_no_company.setVisibility(View.GONE);
                }

            }
        });
        rbQybs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbQybs.isChecked()) {
                    rvCompanyList.setVisibility(View.VISIBLE);
                    if (mycompanyBeanList != null && mycompanyBeanList.size() != 0) {
                        companyAdapter.showList(mycompanyBeanList);
                        ll_no_company.setVisibility(View.GONE);
                        rvCompanyList.setVisibility(View.VISIBLE);
                    } else {
                        ll_no_company.setVisibility(View.VISIBLE);
                        rvCompanyList.setVisibility(View.GONE);

                    }
                }

            }
        });
        //点击选择公司
        rvCompanyList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_no_company.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!transportEntity.isRealUserAuth()) {
                    DialogUtil.showToast(HistoreShare_v2.this, "经办人必须先实名认证");

                } else {
                    Intent intent = new Intent(HistoreShare_v2.this, CompanyListActivity.class);
                    intent.putExtra("token", Constants.user.getTOKEN());
                    intent.putExtra("USERID", Constants.user.getUSER_ID());
                    intent.putExtra("userPid", transportEntity.getIdcardNum());
                    startActivityForResult(intent, REQUESTCODE_COMPANY);
                }
            }
        });

        //添加企业
        rlAddCompany.setOnClickListener(new

                                                OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        AddCompanyDialog addCompanyDialog = new AddCompanyDialog(
                                                                HistoreShare_v2.this,
                                                                null,
                                                                new AddCompanyDialog.Confirm() {


                                                                    @Override
                                                                    public void onConfirm(String myqymc,
                                                                                          String mytyxydm,
                                                                                          String myfrdb,
                                                                                          String myfrsfzhm)
                                                                    {

                                                                        qymc = myqymc;
                                                                        tyxydm = mytyxydm;
                                                                        frdb = myfrdb;
                                                                        frsfzhm = myfrsfzhm;

                                                                        dialog = Background.Process(
                                                                                HistoreShare_v2.this,
                                                                                addCompany,
                                                                                getString(
                                                                                        MSFWResource.getResourseIdByName(
                                                                                                HistoreShare_v2.this,
                                                                                                "string",
                                                                                                "tj_loding")));// 获取表单

                                                                    }
                                                                });


                                                    }
                                                });


    }


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
                String response = HTTP.excuteAndCache("getAdvice",
                                                      "RestOnlineDeclareService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (("200".equals(code))) {
                    final String ADVICE = new JSONObject(json.getString("ReturnValue")).getString(
                            "ADVICE");
                    if (null == ADVICE || ADVICE.equals("") || ADVICE.equals("null")) {
                        // DialogUtil.showUIToast(HistoreShare_v2.this,
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
                    DialogUtil.showUIToast(HistoreShare_v2.this,
                                           getString(MSFWResource.getResourseIdByName(
                                                   HistoreShare_v2.this,
                                                   "string",
                                                   "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();
            }
        }
    };

    /**
     * 获取企业列表
     */
    final Runnable getCompanyList     = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("USERID", Constants.user.getUSER_ID());
                String response = HTTP.excuteAndCache("getINCAuthenticationByUserID",
                                                      "RestUserService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (("200".equals(code))) {
                    mycompanyBeanList = new ArrayList<>();

                    JSONObject companylistJson = new JSONObject(response);
                    companyBeanList = ((CompanyList) JSONUtil.getGson()
                                                             .fromJson(companylistJson.getString(
                                                                     "ReturnValue"),
                                                                       new TypeToken<CompanyList>() {}.getType())).getItems();
                    if (TextUtils.equals(isselectCompany, "2")) {
                        mycompanyBeanList.clear();
                        for (int i = 0; i < companyBeanList.size(); i++) {
                            if (TextUtils.equals(companyBeanList.get(i)
                                                                .getINC_EXT_ID(),
                                                 companyBean.getINC_EXT_ID()))
                            {
                                mycompanyBeanList.add(companyBeanList.get(i));
                                selectedCompanyId = companyBeanList.get(i)
                                                                   .getINC_EXT_ID();
                                inc_zzjgdm = companyBeanList.get(i)
                                                            .getINC_ZZJGDM();
                                inc_jgmc = companyBeanList.get(i)
                                                          .getINC_NAME();
                                inc_gszch = companyBeanList.get(i)
                                                           .getINC_TYSHXYDM();
                                inc_jycs = companyBeanList.get(i)
                                                          .getINC_ADDR();
                                inc_deputy = companyBeanList.get(i)
                                                          .getINC_DEPUTY();
                                selectedCompanyIsreal = companyBeanList.get(i)
                                                                       .getISREALNAME();
                                selectedCompanyIsAuthFaren = companyBeanList.get(i)
                                                                            .getDEPUTY_ISREALNAME();
                            }
                        }
                        if (mycompanyBeanList != null && mycompanyBeanList.size() != 0) {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    if (rbGrbs.isChecked()) {
                                        ll_no_company.setVisibility(View.GONE);
                                        rvCompanyList.setVisibility(View.GONE);
                                    } else {
                                        if (mycompanyBeanList != null && mycompanyBeanList.size() != 0) {

                                            companyAdapter.showList(mycompanyBeanList);
                                            ll_no_company.setVisibility(View.GONE);
                                            rvCompanyList.setVisibility(View.VISIBLE);
                                        }
                                    }


                                }
                            });
                        } else {
                            for (int i = 0; i < companyBeanList.size(); i++) {
                                if (TextUtils.equals(companyBeanList.get(i)
                                                                    .getISDEFAULT(), "1"))
                                {
                                    mycompanyBeanList.add(companyBeanList.get(i));
                                    selectedCompanyId = companyBeanList.get(i)
                                                                       .getINC_EXT_ID();
                                    inc_zzjgdm = companyBeanList.get(i)
                                                                .getINC_ZZJGDM();
                                    inc_jgmc = companyBeanList.get(i)
                                                              .getINC_NAME();
                                    inc_gszch = companyBeanList.get(i)
                                                               .getINC_TYSHXYDM();
                                    inc_jycs = companyBeanList.get(i)
                                                              .getINC_ADDR();
                                    inc_deputy = companyBeanList.get(i)
                                                              .getINC_DEPUTY();
                                    selectedCompanyIsreal = companyBeanList.get(i)
                                                                           .getISREALNAME();
                                    selectedCompanyIsAuthFaren = companyBeanList.get(i)
                                                                                .getDEPUTY_ISREALNAME();
                                }
                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (rbGrbs.isChecked()) {
                                        ll_no_company.setVisibility(View.GONE);
                                        rvCompanyList.setVisibility(View.GONE);
                                    } else {
                                        if (mycompanyBeanList != null && mycompanyBeanList.size() != 0) {

                                            companyAdapter.showList(mycompanyBeanList);
                                            ll_no_company.setVisibility(View.GONE);
                                            rvCompanyList.setVisibility(View.VISIBLE);
                                        } else {
                                            ll_no_company.setVisibility(View.VISIBLE);
                                            rvCompanyList.setVisibility(View.GONE);
                                        }
                                    }


                                }
                            });
                        }

                    } else {
                        mycompanyBeanList.clear();

                        for (int i = 0; i < companyBeanList.size(); i++) {
                            if (TextUtils.equals(companyBeanList.get(i)
                                                                .getISDEFAULT(), "1"))
                            {
                                mycompanyBeanList.add(companyBeanList.get(i));
                                selectedCompanyId = companyBeanList.get(i)
                                                                   .getINC_EXT_ID();

                                inc_zzjgdm = companyBeanList.get(i)
                                                            .getINC_ZZJGDM();
                                inc_jgmc = companyBeanList.get(i)
                                                          .getINC_NAME();
                                inc_gszch = companyBeanList.get(i)
                                                           .getINC_TYSHXYDM();
                                inc_jycs = companyBeanList.get(i)
                                                          .getINC_ADDR();
                                inc_deputy = companyBeanList.get(i)
                                                          .getINC_DEPUTY();


                                selectedCompanyIsreal = companyBeanList.get(i)
                                                                       .getISREALNAME();
                                selectedCompanyIsAuthFaren = companyBeanList.get(i)
                                                                            .getDEPUTY_ISREALNAME();
                                if (isCompanyFirst) {
                                    companyBean = companyBeanList.get(i);
                                }

                            }
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (rbGrbs.isChecked()) {
                                    ll_no_company.setVisibility(View.GONE);
                                    rvCompanyList.setVisibility(View.GONE);
                                } else {
                                    if (mycompanyBeanList != null && mycompanyBeanList.size() != 0) {

                                        companyAdapter.showList(mycompanyBeanList);
                                        ll_no_company.setVisibility(View.GONE);
                                        rvCompanyList.setVisibility(View.VISIBLE);
                                    } else {
                                        ll_no_company.setVisibility(View.VISIBLE);
                                        rvCompanyList.setVisibility(View.GONE);
                                    }
                                }


                            }
                        });
                    }


                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this,
                                           getString(MSFWResource.getResourseIdByName(
                                                   HistoreShare_v2.this,
                                                   "string",
                                                   "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();
            }
        }
    };
    /**
     * 获取企业列表
     */
    final Runnable getSaveCompanyList = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("USERID", Constants.user.getUSER_ID());
                String response = HTTP.excuteAndCache("getINCAuthenticationByUserID",
                                                      "RestUserService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (("200".equals(code))) {
                    mycompanyBeanList = new ArrayList<>();

                    JSONObject companylistJson = new JSONObject(response);
                    companyBeanList = ((CompanyList) JSONUtil.getGson()
                                                             .fromJson(companylistJson.getString(
                                                                     "ReturnValue"),
                                                                       new TypeToken<CompanyList>() {}.getType())).getItems();
                    if (companyBeanList == null || companyBeanList.size() == 0) {

                        if (rbGrbs.isChecked()) {
                            ll_no_company.setVisibility(View.GONE);
                            rvCompanyList.setVisibility(View.GONE);
                        } else {
                            rvCompanyList.setVisibility(View.GONE);
                            ll_no_company.setVisibility(View.VISIBLE);
                        }
                    } else {
                        for (int i = 0; i < companyBeanList.size(); i++) {
                            if (TextUtils.equals(mSaveinc_ext_id,
                                                 companyBeanList.get(i)
                                                                .getINC_EXT_ID()))
                            {
                                mycompanyBeanList.add(companyBeanList.get(i));
                                selectedCompanyId = companyBeanList.get(i)
                                                                   .getINC_EXT_ID();
                                inc_zzjgdm = companyBeanList.get(i)
                                                            .getINC_ZZJGDM();
                                inc_jgmc = companyBeanList.get(i)
                                                          .getINC_NAME();
                                inc_gszch = companyBeanList.get(i)
                                                           .getINC_TYSHXYDM();
                                inc_jycs = companyBeanList.get(i)
                                                          .getINC_ADDR();inc_deputy = companyBeanList.get(i)
                                                          .getINC_DEPUTY();
                                selectedCompanyIsreal = companyBeanList.get(i)
                                                                       .getISREALNAME();
                                selectedCompanyIsAuthFaren = companyBeanList.get(i)
                                                                            .getDEPUTY_ISREALNAME();
                            }
                        }
                        if (mycompanyBeanList == null || mycompanyBeanList.size() == 0) {
                            for (int i = 0; i < companyBeanList.size(); i++) {
                                if (TextUtils.equals(companyBeanList.get(i)
                                                                    .getISDEFAULT(), "1"))
                                {
                                    mycompanyBeanList.add(companyBeanList.get(i));
                                    selectedCompanyId = companyBeanList.get(i)
                                                                       .getINC_EXT_ID();
                                    inc_zzjgdm = companyBeanList.get(i)
                                                                .getINC_ZZJGDM();
                                    inc_jgmc = companyBeanList.get(i)
                                                              .getINC_NAME();
                                    inc_gszch = companyBeanList.get(i)
                                                               .getINC_TYSHXYDM();
                                    inc_jycs = companyBeanList.get(i)
                                                              .getINC_ADDR();inc_deputy = companyBeanList.get(i)
                                                              .getINC_DEPUTY();
                                    selectedCompanyIsreal = companyBeanList.get(i)
                                                                           .getISREALNAME();
                                    selectedCompanyIsAuthFaren = companyBeanList.get(i)
                                                                                .getDEPUTY_ISREALNAME();
                                }
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (rbGrbs.isChecked()) {
                                    ll_no_company.setVisibility(View.GONE);
                                    rvCompanyList.setVisibility(View.GONE);
                                } else {
                                    if (mycompanyBeanList != null && mycompanyBeanList.size() != 0) {

                                        companyAdapter.showList(mycompanyBeanList);
                                        ll_no_company.setVisibility(View.GONE);
                                        rvCompanyList.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                        });
                    }

                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this,
                                           getString(MSFWResource.getResourseIdByName(
                                                   HistoreShare_v2.this,
                                                   "string",
                                                   "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();
            }
        }
    };

    /**
     * 添加企业信息
     */
    final Runnable addCompany    = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("USERID", Constants.user.getUSER_ID());
                param.put("INC_NAME", qymc);
                param.put("INC_TYSHXYDM", tyxydm);
                param.put("INC_DEPUTY", frdb);
                param.put("INC_PID", frsfzhm);
                String response = HTTP.excuteAndCache("saveINCAuthentication",
                                                      "RestUserService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (("200".equals(code))) {

                    new Thread(getCompanyList).start();


                } else if (("500".equals(code))) {
                    DialogUtil.showUIToast(HistoreShare_v2.this, "已绑定该企业，不能重复绑定");
                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this,
                                           getString(MSFWResource.getResourseIdByName(
                                                   HistoreShare_v2.this,
                                                   "string",
                                                   "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();
            }
        }
    };
    /**
     * 编辑企业信息
     */
    final Runnable editCompany   = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("USERID", Constants.user.getUSER_ID());
                param.put("INC_EXT_ID", INC_EXT_ID);
                param.put("INC_NAME", qymc);
                param.put("INC_TYSHXYDM", tyxydm);
                param.put("INC_DEPUTY", frdb);
                param.put("INC_PID", frsfzhm);
                if (TextUtils.equals(AuthType, "1")) {//验证企业信息

                    if (TextUtils.equals(transportEntity.getIdcardNum(), frsfzhm)) {
                        param.put("DEPUTY_ISREALNAME", "1");
                    } else {
                        param.put("DEPUTY_ISREALNAME", "0");

                    }

                } else if (TextUtils.equals(AuthType, "2")) {//验证企业法人
                    param.put("DEPUTY_ISREALNAME", "1");

                }


                String response = HTTP.excuteAndCache("editINCAuthentication",
                                                      "RestUserService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json  = new JSONObject(response);
                String     code  = json.getString("code");
                String     error = json.getString("error");
                EditCompanyBean editCompanyBean = ((EditCompanyBean) JSONUtil.getGson()
                                                                             .fromJson(json.getString(
                                                                                     "ReturnValue"),
                                                                                       new TypeToken<EditCompanyBean>() {}.getType()));
                if (("200".equals(code))) {

                    //                    new Thread(getCompanyList).start();
                    //                    DialogUtil.showUIToast(HistoreShare_v2.this, editCompanyBean.getMsg());
                    DialogUtil.showUIToast(HistoreShare_v2.this, editCompanyBean.getMsg());
                    CompanyBean companyBean1 = new CompanyBean();
                    companyBean1.setINC_NAME(qymc);
                    companyBean1.setINC_TYSHXYDM(tyxydm);
                    companyBean1.setINC_DEPUTY(frdb);
                    companyBean1.setINC_PID(frsfzhm);

                    if (TextUtils.equals(AuthType, "1")) {//验证企业信息
                        mycompanyBeanList.clear();
                        selectedCompanyIsreal = "1";
                        companyBean1.setDEPUTY_ISREALNAME("1");
                        mycompanyBeanList.add(companyBean1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                companyAdapter.showList(mycompanyBeanList);

                            }
                        });


                    } else if (TextUtils.equals(AuthType, "2")) {//验证企业法人
                        mycompanyBeanList.clear();
                        selectedCompanyIsreal = "1";
                        companyBean1.setDEPUTY_ISREALNAME("1");
                        mycompanyBeanList.add(companyBean1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                companyAdapter.showList(mycompanyBeanList);

                            }
                        });

                    }

                } else if (("500".equals(code))) {
                    DialogUtil.showUIToast(HistoreShare_v2.this, error);
                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this,
                                           getString(MSFWResource.getResourseIdByName(
                                                   HistoreShare_v2.this,
                                                   "string",
                                                   "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();
            }
        }
    };
    final Runnable deleteCompany = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());

                param.put("INC_EXT_ID", deleteId);

                String response = HTTP.excuteAndCache("deleteINCAuthentication",
                                                      "RestUserService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (("200".equals(code))) {

                    new Thread(getCompanyList).start();
                    DialogUtil.showUIToast(HistoreShare_v2.this, "删除企业信息成功");

                } else if (("500".equals(code))) {
                    DialogUtil.showUIToast(HistoreShare_v2.this, "该企业已存在申报事项，不能删除");
                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this,
                                           getString(MSFWResource.getResourseIdByName(
                                                   HistoreShare_v2.this,
                                                   "string",
                                                   "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();
            }
        }
    };

    private void initSetOnListener() {
        applySave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                if (!checkUserInfo()) {
                    alertAuthDialog();
                    return;
                }

                if (checkUploading()) {
                    DialogUtil.showToast(HistoreShare_v2.this, "有文件正在上传，请上传完后提交暂存请求！");
                    return;
                }
                webView.loadUrl("javascript:androidSave()");
                status = "9";
                if (WDLQ.isChecked()) {
                    Constants.getPostInfo = null;
                } else if (chooseAddress.isChecked()) {
                    String RECEIVE_STR = RECEIVE.getText()
                                                .toString()
                                                .trim();
                    String PHONE_STR = PHONE.getText()
                                            .toString()
                                            .trim();
                    String ADDRESS_STR = ADDRESS.getText()
                                                .toString()
                                                .trim();
                    if (!TextUtils.isEmpty(RECEIVE_STR) && !TextUtils.isEmpty(PHONE_STR) && !TextUtils.isEmpty(
                            ADDRESS_STR))
                    {
                        PostInfo postInfo = new PostInfo();
                        postInfo.setRECEIVE(RECEIVE_STR);
                        postInfo.setADDRESS(ADDRESS_STR);
                        postInfo.setPHONE(PHONE_STR);
                        Constants.getPostInfo = postInfo;
                    } else {
                        Constants.getPostInfo = null;
                    }

                }
                dialog = Background.Process(HistoreShare_v2.this,
                                            checkFZBDsave,
                                            getString(MSFWResource.getResourseIdByName(
                                                    HistoreShare_v2.this,
                                                    "string",
                                                    "tj_loding")));
            }
        });
        applySubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (rbQybs.isChecked() && TextUtils.isEmpty(selectedCompanyId)) {
                    DialogUtil.showToast(HistoreShare_v2.this, "请选择申报企业");
                    return;
                }

                if (!checkUserInfo()) {
                    alertAuthDialog();
                    return;
                }
                if (checkUploading()) {
                    DialogUtil.showToast(HistoreShare_v2.this, "有文件正在上传，请上传完后提交申报请求！");
                    return;
                }
                if (Constants.CHECK_UPLOAD_FILE) {
                    if (!isFinishForm) {
                        DialogUtil.showUIToast(HistoreShare_v2.this, "基本表单信息未填写完整，请填写完整后再进行申报！");
                        return;
                    }
                    if (!checkBigFile()) {
                        DialogUtil.showUIToast(HistoreShare_v2.this, "有必交材料未提交，请提交完全后再进行申报！");
                        return;
                    }
                    if (!checkLzfsStaus()) {
                        if (TextUtils.isEmpty(YWGSDName)&&!TextUtils.isEmpty(ywgsd)) {
                            DialogUtil.showUIToast(HistoreShare_v2.this, "业务归属地还没选择，请选择");
                        } else {
                            DialogUtil.showUIToast(HistoreShare_v2.this, "有邮寄信息未提交，请提交完全后再进行申报！");

                        }



                        return;
                    }

                    if (WDLQ.isChecked()) {
                        Constants.getPostInfo = null;
                    } else if (chooseAddress.isChecked()) {
                        String RECEIVE_STR = RECEIVE.getText()
                                                    .toString()
                                                    .trim();
                        String PHONE_STR = PHONE.getText()
                                                .toString()
                                                .trim();
                        String ADDRESS_STR = ADDRESS.getText()
                                                    .toString()
                                                    .trim();
                        PostInfo postInfo = new PostInfo();
                        postInfo.setRECEIVE(RECEIVE_STR);
                        postInfo.setADDRESS(ADDRESS_STR);
                        postInfo.setPHONE(PHONE_STR);
                        postInfo.setPROVINCE(POSTINFO.getPROVINCE());
                        postInfo.setCITY(POSTINFO.getCITY());
                        postInfo.setCOUNTRY(POSTINFO.getCOUNTRY());
                        Constants.getPostInfo = postInfo;
                    }
                }

                EnsureApplyDialog ensureApplyDialog = new EnsureApplyDialog(HistoreShare_v2.this,
                                                                            new EnsureApplyDialog.EnsureConfirm() {


                                                                                @Override
                                                                                public void onConfirm() {
                                                                                    webView.loadUrl(
                                                                                            "javascript:androidSave()");
                                                                                    Background.Process(
                                                                                            HistoreShare_v2.this,
                                                                                            checkFZBD,
                                                                                            getString(
                                                                                                    MSFWResource.getResourseIdByName(
                                                                                                            HistoreShare_v2.this,
                                                                                                            "string",
                                                                                                            "tj_loding")));
                                                                                }
                                                                            });


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
                HistoreShare_v2.this.finish();
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
     * 判断大附件是否全部提交
     *
     * @return
     */
    private boolean checkBigFile() {
        for (int i = 0; i < bigFileDate.size(); i++) {// &&
            // !bigFileDate.get(i).getDZHYQ().equals("5")
            // 信息中心要求修改 只要是必要材料都必需提交
            if (bigFileDate.get(i)
                           .getSFBY()
                           .equals("1") && !bigFileDate.get(i)
                                                       .getSTATUS()
                                                       .equals("1"))
            {
                isFinishFile = false;
                return false;
            }
        }
        isFinishFile = true;
        return true;
    }

    /*
    * 检查是否有文件正在上传中
    * */
    private boolean checkUploading() {
        Iterator it = Constants.material.entrySet()
                                        .iterator();
        while (it.hasNext()) {
            Map.Entry     entry = (Map.Entry) it.next();
            List<ATTBean> atts  = (List<ATTBean>) entry.getValue();
            if (null != atts) {
                for (int j = 0; j < atts.size(); j++) {
                    if (atts.get(j)
                            .getUpStatus() == UploadStatus.UPLOADING)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 空间获取大附件并比对
     */
    final Runnable GetAttachShare = new Runnable() {
        @Override
        public void run() {
            try {

                JSONObject param      = new JSONObject();
                JSONArray  attachCode = new JSONArray();
                for (int i = 0; i < bigFileDate.size(); i++) {
                    attachCode.put(bigFileDate.get(i)
                                              .getCLBH());
                }
                param.put("USERCODE", Constants.user.getCODE());
                param.put("ATTACHCODE", attachCode);
                String response = HTTP.excuteShare("attachSearch",
                                                   "SpaceAttachInfoService",
                                                   param.toString());
                JSONObject json = new JSONObject(response);
                List<ShareMaterial> attachShares = JSONUtil.getGson()
                                                           .fromJson(json.getString("ReturnValue"),
                                                                     new TypeToken<List<ShareMaterial>>() {}.getType());
                Log.e("sps====", attachShares.size() + "===vvv");
                for (int i = 0; i < bigFileDate.size(); i++) {
                    for (int j = 0; j < attachShares.size(); j++) {
                        if (bigFileDate.get(i)
                                       .getCLBH()
                                       .equals(attachShares.get(j)
                                                           .getATTACHCODE()))
                        {
                            bigFileDate.get(i)
                                       .setSTATUS(attachShares.get(j)
                                                              .getCOMPRESULT());
                            bigFileDate.get(i)
                                       .setFILEID(attachShares.get(j)
                                                              .getID());
                            bigFileDate.get(i)
                                       .setFILEPATH(attachShares.get(j)
                                                                .getATTACHURL());
                            bigFileDate.get(i)
                                       .setFILENAME(attachShares.get(j)
                                                                .getATTACHNAME());
                            bigFileDate.get(i)
                                       .setFILEDEL("0");
                            bigFileDate.get(i)
                                       .setATTS(attachShares.get(j)
                                                            .getATTS());
                            Constants.material.put(attachShares.get(j)
                                                               .getATTACHCODE(),
                                                   attachShares.get(j)
                                                               .getATTBeans());// 将共享结果放入map中

                        }

                    }
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        Collections.sort(bigFileDate, new ApplyBeanComparator());
                        // bigFileList.setAdapter(new
                        // HistoreShare_v2Adapter(bigFileDate, 1));
                        //                        applyBeanAdapter = new ApplyBeanAdapter(HistoreShare_v2.this, bigFileDate);
                        applyBeanAdapterv2 = new ApplyBeanAdapterv2(HistoreShare_v2.this,
                                                                    bigFileDate);
                        bigFileList.setAdapter(applyBeanAdapterv2);

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
        //        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


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
        // intent.setClass(HistoreShare_v2.this, CaptureActivity.class);
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
            finish_form.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_yes"));
            finish_file.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_yes"));
            finish_lzfs.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_yes"));
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
                    // DialogUtil.showUIToast(HistoreShare_v2.this,
                    // "有必交材料未提交，请提交完全后再进行下一步操作！");
                    // return;
                    // }
                    viewPager.setCurrentItem(2);
                } else if (currIndex == 2) {
                    viewPager.setCurrentItem(3);
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
                } else if (currIndex == 0) {
                    onBackPressed();
                }

            }
        });

        main = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "main"));
        applyLayNoPre = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                     "id",
                                                                                     "applyLayNoPre"));
        titleBar = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                "id",
                                                                                "titleBar"));
        exit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "exit"));
        applySubmitNoPre = (Button) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                  "id",
                                                                                  "applySubmitNoPre"));
        applySaveNoPre = (Button) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                "id",
                                                                                "applySaveNoPre"));
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
                Constants.getInstance()
                         .exit();
            }
        });
        help.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ArrayList<ApplyBean> applyBeans = new ArrayList<>();
                for (int i = 0; i < bigFileDate.size(); i++) {
                    applyBeans.addAll(bigFileDate);
                }


                ShowPopupMoreUtil.showPopupMore(v,
                                                HistoreShare_v2.this,
                                                PERMID,
                                                "网上申报",
                                                permission.getSXZXNAME(),
                                                permission.getDEPTNAME(),
                                                applyBeans);
            }
        });
        btnFlow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HistoreShare_v2.this, SearchSchedule.class);
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
                    intent.setClass(HistoreShare_v2.this, ExpressageChoose.class);
                    startActivity(intent);
                } else {// 已申报
                    Intent intent = new Intent();
                    intent.setClass(HistoreShare_v2.this, ExpressageProgress.class);
                    intent.putExtra("BSNUM", BSNUM);
                    startActivity(intent);
                }

            }
        });

        ems.getPaint()
           .setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        btnFlow.getPaint()
               .setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        title = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "title"));
        applyLay = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                "id",
                                                                                "applyLay"));
        formBtn = (LinearLayout) formPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                        "id",
                                                                                        "formBtn"));
        applySave = (Button) findViewById(MSFWResource.getResourseIdByName(this,
                                                                           "id",
                                                                           "applySave"));
        applySubmit = (Button) findViewById(MSFWResource.getResourseIdByName(this,
                                                                             "id",
                                                                             "applySubmit"));

        save = (Button) formPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "save"));
        webView = (WebView) formPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                   "id",
                                                                                   "webView"));

        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new JSI(), "android");
        webView.setWebViewClient(new WebViewClient() {//mate9出现空白页的解决方法
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

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
            public boolean onJsAlert(WebView view,
                                     String url,
                                     String message,
                                     final JsResult result)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(HistoreShare_v2.this).setTitle(
                        getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                   "string",
                                                                   "tj_notify")))
                                                                                         .setMessage(
                                                                                                 message)
                                                                                         .setPositiveButton(
                                                                                                 getString(
                                                                                                         MSFWResource.getResourseIdByName(
                                                                                                                 HistoreShare_v2.this,
                                                                                                                 "string",
                                                                                                                 "tj_OK")),
                                                                                                 new AlertDialog.OnClickListener() {
                                                                                                     @Override
                                                                                                     public void onClick(
                                                                                                             DialogInterface dialog,
                                                                                                             int which)
                                                                                                     {
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

    private void alertAuthDialog() {

        new Builder(HistoreShare_v2.this).setMessage("您申报的事项需要完成实人制认证才能申报，是否现在去认证！")
                                         .setTitle("温馨提示")
                                         .setPositiveButton("现在认证",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface mdialog,
                                                                                    int which)
                                                                {
                                                                    AuthUtil.startHuaXunFaceAuth(
                                                                            HistoreShare_v2.this,
                                                                            true);
                                                                }
                                                            })
                                         .setNegativeButton("取消",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog,
                                                                                    int which)
                                                                {
                                                                    return;
                                                                }
                                                            })
                                         .show();

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
                String response = HTTP.excuteAndCache("getInsMaterialInfo",
                                                      "RestOnlineDeclareService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    MATERIALS = new String(Base64.decode(new JSONObject(json.getString("ReturnValue")).getString(
                            "MATERIALS"), 1));
                    System.out.println("fuchl    MATERIALS:" + MATERIALS);
                    applyBeans = XMLUtil.parseMaterials(MATERIALS);
                    if (Constants.material.size() == 0) {// 只填充一次
                        XMLUtil.material2Map(MATERIALS);
                    }
                    if (null != applyBeans) {
                        bigFileDate = new ArrayList<ApplyBean>();
                        for (int i = 0; i < applyBeans.size(); i++) {
                            if (applyBeans.get(i)
                                          .getDZHYQ()
                                          .contains("1") || applyBeans.get(i)
                                                                      .getDZHYQ()
                                                                      .contains("3") || (applyBeans.get(
                                    i)
                                                                                                   .getDZHYQ()
                                                                                                   .contains(
                                                                                                           "5")))
                            {
                                bigFileDate.add(applyBeans.get(i));

                            }
                        }

                    }
                    handler.sendEmptyMessage(GET_APPLY_BEAN_SUCCESS);
                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this, json.getString("error"));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();
            }
        }
    };

    /**
     * 暂存的企业个人信息
     */
    final Runnable getInsIncExt = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("BSNUM", BSNUM);
                String response = HTTP.excuteAndCache("getInsIncExt",
                                                      "RestOnlineDeclareService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                final InsIncExt insIncExt = JSONUtil.getGson()
                                                    .fromJson(json.getString("ReturnValue"),
                                                              new TypeToken<InsIncExt>() {}.getType());
                if (code.equals("200")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mSaveinc_ext_id = insIncExt.getINC_EXT_ID();
                            if (STATUS == 9||STATUS==0) {

                                if (TextUtils.equals(permission.getUSERTYPE(), "0"))

                                {
                                    if (TextUtils.equals(insIncExt.getTYPE(), "1")) {

                                        rbGrbs.setChecked(true);
                                        rvCompanyList.setVisibility(View.GONE);
                                        ll_no_company.setVisibility(View.GONE);


                                    } else {

                                        rbQybs.setChecked(true);
                                        rbGrbs.setChecked(false);
                                    }


                                } else if (TextUtils.equals(permission.getUSERTYPE(), "1"))

                                {//个人
                                    rbGrbs.setChecked(true);

                                    rbQybs.setVisibility(View.GONE);
                                    rlAddCompany.setVisibility(View.GONE);
                                    tvChooseType.setText("该事项支持个人办事");
                                    rvCompanyList.setVisibility(View.GONE);
                                    ll_no_company.setVisibility(View.GONE);


                                } else if (TextUtils.equals(permission.getUSERTYPE(), "2"))

                                {//企业
                                    rlAddCompany.setVisibility(View.GONE);

                                    rbGrbs.setVisibility(View.GONE);
                                    rbQybs.setChecked(true);
                                    tvChooseType.setText("该事项支持企业办事");
                                    rvCompanyList.setVisibility(View.VISIBLE);
                                    ll_no_company.setVisibility(View.VISIBLE);
                                } else {
                                    rbGrbs.setChecked(true);

                                    rvCompanyList.setVisibility(View.GONE);
                                    ll_no_company.setVisibility(View.GONE);
                                }
                            } else {
                                if (TextUtils.equals(insIncExt.getTYPE(), "1")) {

                                    rbGrbs.setChecked(true);
                                    rbQybs.setVisibility(View.GONE);
                                    rvCompanyList.setVisibility(View.GONE);
                                    ll_no_company.setVisibility(View.GONE);

                                } else {
                                    rbGrbs.setVisibility(View.GONE);
                                    rbQybs.setChecked(true);
                                }
                            }
                            if (STATUS == 0) {
                                rbGrbs.setFocusable(false);
                                rbQybs.setFocusable(false);
                                rbGrbs.setClickable(false);
                                rbQybs.setClickable(false);
                            }

                        }
                    });
                    new Thread(getSaveCompanyList).run();



                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this, json.getString("error"));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();
            }
        }
    };

    private class ApplyBeanComparator
            implements Comparator<ApplyBean>
    {
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
                String response = HTTP.excuteAndCache("getClxxByPermid",
                                                      "RestPermissionitemService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    applyBeans = JSONUtil.getGson()
                                         .fromJson(json.getString("ReturnValue"),
                                                   new TypeToken<List<ApplyBean>>() {}.getType());
                    if (null != applyBeans) {
                        bigFileDate = new ArrayList<ApplyBean>();
                        for (int i = 0; i < applyBeans.size(); i++) {
                            if (applyBeans.get(i)
                                          .getDZHYQ()
                                          .contains("1") || applyBeans.get(i)
                                                                      .getDZHYQ()
                                                                      .contains("3") || (applyBeans.get(
                                    i)
                                                                                                   .getDZHYQ()
                                                                                                   .contains(
                                                                                                           "5")))
                            {
                                bigFileDate.add(applyBeans.get(i));

                            }
                        }
                    }
                    handler.sendEmptyMessage(GET_APPLY_BEAN_SUCCESS);
                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this, json.getString("error"));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
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
                String response = HTTP.excuteAndCache("getFormByPermidV2",
                                                      "RestPermissionitemService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    forms = JSONUtil.getGson()
                                    .fromJson(json.getString("ReturnValue"),
                                              new TypeToken<List<Form>>() {}.getType());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (null != forms) {// 去掉1
                                for (int i = 0; i < forms.size(); i++) {
                                    if (null != forms.get(i)
                                                     .getFORMTYPE() && forms.get(i)
                                                                            .getFORMTYPE()
                                                                            .equals("1"))
                                    {
                                        forms.remove(i);
                                    }
                                }
                                webView.loadUrl(getUrlById(forms.get(0)
                                                                .getID()));
                            }
                        }
                    });

                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this, json.getString("error"));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }
    };
    final Runnable GetFormByBsNo   = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("BSNUM", BSNUM);
                String response = HTTP.excuteAndCache("getInsFormData",
                                                      "RestOnlineDeclareService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    String returnValue = json.getString("ReturnValue");
                    String FORMS       = new JSONObject(returnValue).getString("FORMS");
                    String formXml     = new String(Base64.decode(FORMS.getBytes(), 1));
                    Log.e("sps====", "formXml  fcl====" + formXml.replaceAll("[\n\r]", ""));
                    final String jsonData = XMLUtil.toJsonForJS(formXml.replaceAll("[\n\r]", ""));
                    dataidArray = new JSONObject(jsonData).getJSONArray("dataid");

                    runOnUiThread(new Runnable() {
                        public void run() {
                            webView.loadUrl("javascript:shareformvalue('" + jsonData + "','form1')");
                        }
                    });

                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this, json.getString("error"));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }
    };
    final Runnable GetCorInfo      = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("USERCODE", Constants.user.getCODE());
                param.put("USERTYPE", Constants.user.getTYPE());
                String response = HTTP.excuteShare("baseInfoShare",
                                                   "SpaceDataInfoService",
                                                   param.toString());
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
                String response = HTTP.excuteAndCache("getInfoByUserid",
                                                      "RestUserService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    final String userDetail = json.getString("ReturnValue");
                    Log.e("sps", "执行了…userDetail…====" + userDetail);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (null != FileUtil.Load(HistoreShare_v2.this,
                                                      Constants.user.getUSER_ID() + "_" + PERMID) && !FileUtil.Load(
                                    HistoreShare_v2.this,
                                    Constants.user.getUSER_ID() + "_" + PERMID)
                                                                                                              .equals(""))
                            {
                                //{"jydz":"深圳","zczb":"1亿","zlmj":"123","TextInput42":"12","TextInput55":"123","frdbxm":"查总","fzrxm":"测试","sqr_mc":"测试121","sqr_gddh":"155784071","inc_jgmc":"深圳太极云软技术股份有限公司","inc_gszch":"91440300192195622F","jbr_mc":"测试121","jbr_sfzjhm":"350524166603041596","jbr_yddh":"18113142222","inc_zzjgdm":"192195622","inc_jycs":"深圳市福田区百花二路百花公寓2栋26G"}
                                // 从缓存中读取
                                UserCacheDetail userCacheDetail= JSONUtil.getGson()
                                                   .fromJson(FileUtil.Load(HistoreShare_v2.this,
                                                                           Constants.user.getUSER_ID() + "_" + PERMID),
                                                             new TypeToken<UserCacheDetail>() {}.getType());
                                if (rbQybs.isChecked()) {
                                    userCacheDetail.setSqr_mc(inc_deputy);
                                    userCacheDetail.setInc_jgmc(inc_jgmc);
                                } else {
                                    transportEntity = gloabDelegete.getUserInfo();
                                    userCacheDetail.setSqr_mc(transportEntity.getRealName());
//                                    userCacheDetail.setInc_jgmc(transportEntity.getINC_NAME());
                                    userCacheDetail.setInc_jgmc("");

                                }
                                String json = new Gson().toJson(userCacheDetail);

                                webView.loadUrl("javascript:shareformvalue('" +json + "')");
                            } else {
                                // 从个人信息读取

                                UserDetail userDetailjson= JSONUtil.getGson()
                                                                    .fromJson(userDetail,
                                                                                   new TypeToken<UserDetail>() {}.getType());
                                if (rbQybs.isChecked()) {
                                    userDetailjson.setUSER_NAME(inc_deputy);
                                    userDetailjson.setINC_NAME(inc_jgmc);
                                } else {
                                    transportEntity = gloabDelegete.getUserInfo();
                                    userDetailjson.setUSER_NAME(transportEntity.getRealName());
//                                    userDetailjson.setINC_NAME(transportEntity.getINC_NAME());
                                    userDetailjson.setINC_NAME("");
                                    userDetailjson.setUSER_PID(transportEntity.getIdcardNum());
                                }
                                String myuserdetail = new Gson().toJson(userDetailjson);
                                webView.loadUrl("javascript:shareformvalue('" + myuserdetail + "')");
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
                    if (s.toString()
                         .length() < 10)
                    {
                        finish_form.setImageResource(MSFWResource.getResourseIdByName(
                                HistoreShare_v2.this,
                                "drawable",
                                "tj_right_no"));
                        isFinishForm = false;
                        return;
                    } else {
                        finish_form.setImageResource(MSFWResource.getResourseIdByName(
                                HistoreShare_v2.this,
                                "drawable",
                                "tj_right_yes"));
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
            StringWriter  writer     = new StringWriter();
            try {
                JSONObject json = new JSONObject(s);
                formItemBeans = JSONUtil.getGson()
                                        .fromJson(json.getString("data"),
                                                  new TypeToken<List<FormItemBean>>() {}.getType());
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
                serializer.cdsect(forms.get(index)
                                       .getID());
                serializer.endTag("", "formid");
                serializer.startTag("", "version");
                serializer.cdsect(forms.get(index)
                                       .getFORMVER());
                serializer.endTag("", "version");
                serializer.startTag("", "formtype");
                serializer.cdsect(forms.get(index)
                                       .getFORMTYPE());
                serializer.endTag("", "formtype");


                //新增加的三条字段
                //(申请人是否实名)
                serializer.startTag("", "sqr_isrealname");
                if (TextUtils.equals("1", Constants.user.getTYPE())) {
                    if (rbGrbs.isChecked()) {
                        serializer.cdsect(transportEntity.isRealUserAuth()
                                          ? "1"
                                          : "0");
                    } else {
                        Log.e("Constants.user",
                              selectedCompanyIsAuthFaren + "selectedCompanyIsAuthFaren");

                        serializer.cdsect(null == selectedCompanyIsAuthFaren
                                          ? ""
                                          : selectedCompanyIsAuthFaren);

                    }
                } else {
                    serializer.cdsect("");
                }

                serializer.endTag("", "sqr_isrealname");
                //(企业是否实名)
                serializer.startTag("", "inc_isrealname");
                if (TextUtils.equals("1", Constants.user.getTYPE())) {
                    if (rbGrbs.isChecked()) {
                        serializer.cdsect("");
                    } else {
                        Log.e("Constants.user", selectedCompanyIsreal + "selectedCompanyIsreal");

                        serializer.cdsect(null == selectedCompanyIsreal
                                          ? ""
                                          : selectedCompanyIsreal);
                    }
                } else {
                    serializer.cdsect("");
                }
                serializer.endTag("", "inc_isrealname");
                //(经办人是否实名)
                serializer.startTag("", "jbr_isrealname");
                if (TextUtils.equals("1", Constants.user.getTYPE())) {
                    if (rbGrbs.isChecked()) {
                        serializer.cdsect("");
                    } else {
                        serializer.cdsect(transportEntity.isRealUserAuth()
                                          ? "1"
                                          : "0");
                    }
                } else {
                    serializer.cdsect(transportEntity.isRealUserAuth()
                                      ? "1"
                                      : "0");
                }
                serializer.endTag("", "jbr_isrealname");


                JSONObject formDataCache;
                if (FileUtil.Load(HistoreShare_v2.this, Constants.user.getUSER_ID() + "_" + PERMID)
                            .equals(""))
                {
                    formDataCache = new JSONObject();
                } else {
                    formDataCache = new JSONObject(FileUtil.Load(HistoreShare_v2.this,
                                                                 Constants.user.getUSER_ID() + "_" + PERMID));
                }
                boolean hasInc_zzjgdm = false;
                boolean hasInc_jgmc   = false;
                boolean hasInc_gszch  = false;
                boolean hasInc_jycs   = false;


                if (TextUtils.equals("1", Constants.user.getTYPE()) && rbQybs.isChecked()) {
                    ArrayList<FormItemBean> myFormItemBeans = new ArrayList<>();
                    for (int j = 0; j < formItemBeans.size(); j++) {

                        if (!TextUtils.equals(formItemBeans.get(j)
                                                           .getName(),
                                              "inc_zzjgdm") && !TextUtils.equals(formItemBeans.get(j)
                                                                                              .getName(),
                                                                                 "inc_jgmc") && !TextUtils.equals(
                                formItemBeans.get(j)
                                             .getName(),
                                "inc_gszch") && !TextUtils.equals(formItemBeans.get(j)
                                                                               .getName(),
                                                                  "inc_jycs") && !TextUtils.equals(
                                formItemBeans.get(j)
                                             .getName(),
                                "jbr_mc") && !TextUtils.equals(formItemBeans.get(j)
                                                                            .getName(),
                                                               "jbr_sfzjhm") && !TextUtils.equals(
                                formItemBeans.get(j)
                                             .getName(),
                                "jbr_yddh"))
                        {
                            myFormItemBeans.add(formItemBeans.get(j));
                        }
                    }
                    FormItemBean formItemBean = new FormItemBean();
                    formItemBean.setName("inc_zzjgdm");
                    formItemBean.setType("TEXTINPUT");
                    formItemBean.setValue(inc_zzjgdm);
                    myFormItemBeans.add(formItemBean);
                    FormItemBean formItemBean1 = new FormItemBean();
                    formItemBean1.setName("inc_jgmc");
                    formItemBean1.setType("TEXTINPUT");
                    formItemBean1.setValue(inc_jgmc);
                    myFormItemBeans.add(formItemBean1);

                    FormItemBean formItemBean2 = new FormItemBean();
                    formItemBean2.setName("inc_gszch");
                    formItemBean2.setType("TEXTINPUT");
                    formItemBean2.setValue(inc_gszch);
                    myFormItemBeans.add(formItemBean2);
                    FormItemBean formItemBean3 = new FormItemBean();
                    formItemBean3.setName("inc_jycs");
                    formItemBean3.setType("TEXTINPUT");
                    formItemBean3.setValue(inc_jycs);
                    myFormItemBeans.add(formItemBean3);

                    FormItemBean formItemBean4 = new FormItemBean();
                    formItemBean4.setName("jbr_mc");
                    formItemBean4.setType("TEXTINPUT");
                    formItemBean4.setValue(Constants.user.getREALNAME());
                    myFormItemBeans.add(formItemBean4);

                    FormItemBean formItemBean5 = new FormItemBean();
                    formItemBean5.setName("jbr_sfzjhm");
                    formItemBean5.setType("TEXTINPUT");
                    formItemBean5.setValue(Constants.user.getCODE());
                    myFormItemBeans.add(formItemBean5);

                    FormItemBean formItemBean6 = new FormItemBean();
                    formItemBean6.setName("jbr_yddh");
                    formItemBean6.setType("TEXTINPUT");
                    formItemBean6.setValue(Constants.user.getMOBILE());
                    myFormItemBeans.add(formItemBean6);


                    //                        if (formItemBeans.get(j)
                    //                                         .getName() == "inc_zzjgdm")
                    //                        {
                    //                            hasInc_zzjgdm = true;
                    //                        }
                    //                        if (formItemBeans.get(j)
                    //                                         .getName() == "inc_jgmc")
                    //                        {
                    //                            hasInc_jgmc = true;
                    //                        }
                    //                        if (formItemBeans.get(j)
                    //                                         .getName() == "inc_gszch")
                    //                        {
                    //                            hasInc_gszch = true;
                    //                        }
                    //                        if (formItemBeans.get(j)
                    //                                         .getName() == "inc_jycs")
                    //                        {
                    //                            hasInc_jycs = true;
                    //                        }
                    //                    }
                    //                    if (!hasInc_zzjgdm) {
                    //                        FormItemBean formItemBean = new FormItemBean();
                    //                        formItemBean.setName();
                    //                    }
                    formItemBeans = myFormItemBeans;
                }


                for (int j = 0; j < formItemBeans.size(); j++) {
                    serializer.startTag("",
                                        formItemBeans.get(j)
                                                     .getName());
                    serializer.attribute("",
                                         "type",
                                         formItemBeans.get(j)
                                                      .getType());
                    serializer.cdsect(formItemBeans.get(j)
                                                   .getValue() == null
                                      ? ""
                                      : formItemBeans.get(j)
                                                     .getValue());
                    serializer.endTag("",
                                      formItemBeans.get(j)
                                                   .getName());
                    formDataCache.put(formItemBeans.get(j)
                                                   .getName(),
                                      formItemBeans.get(j)
                                                   .getValue());
                }
                FileUtil.Write(HistoreShare_v2.this,
                               Constants.user.getUSER_ID() + "_" + PERMID,
                               formDataCache.toString());

                serializer.endTag("", "data");
                serializer.endDocument();
                forms.get(index)
                     .setXML(writer.toString());
                System.out.println("fuchl  " + forms.get(index)
                                                    .getXML());
                if (index == forms.size() - 1) {// 最后一张表单时将xml组装
                    for (int i = 0; i < forms.size(); i++) {
                        formsXML += forms.get(i)
                                         .getXML();
                        formsXML = formsXML.replaceAll("<\\?xml.*.\\?>", "");
                    }
                    formsXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><forms>" + formsXML + "</forms>";
                    Log.e("sps===", "formsXML==" + formsXML);
                    FileUtil.Write(HistoreShare_v2.this,
                                   Constants.user.getUSER_ID() + "_" + PERMID + "XML",
                                   formsXML);
                } else {
                    index++;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            webView.loadUrl(getUrlById(forms.get(index)
                                                            .getID()));
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
                HistoreShare_v2.this.finish();
            } else {
                index--;
                webView.goBack();
            }
        }

    }

    private void InitViewPager() {
        back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
        viewPager = (NoTouchViewPage) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "vPager"));
        viewPager.setScrollble(true);
        views = new ArrayList<View>();
        inflater = getLayoutInflater();
        userInfoPage = inflater.inflate(R.layout.tj_user_info, null);
        formPage = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_forms"),
                                    null);
        bigFilePage = inflater.inflate(MSFWResource.getResourseIdByName(this,
                                                                        "layout",
                                                                        "tj_share_big_file"), null);
        LzfsPage = inflater.inflate(MSFWResource.getResourseIdByName(this,
                                                                     "layout",
                                                                     "tj_lzfs_page_v2"), null);

        views.add(userInfoPage);
        views.add(formPage);
        views.add(bigFilePage);
        views.add(LzfsPage);

        viewPager.setAdapter(new MyViewPagerAdapter(views));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        bigFileList = (ListView) bigFilePage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                           "id",
                                                                                           "listView"));
        ImageView empty = (ImageView) bigFilePage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                                "id",
                                                                                                "empty"));
        bigFileList.setEmptyView(empty);

        initLZFS();
    }

    /**
     * 初始化头标
     */

    private void InitTextView() {

        textView0 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text0"));
        textView1 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text1"));
        // textView2 = (TextView)
        // findViewById(MSFWResource.getResourseIdByName(this, "id", "text2"));
        textView3 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text3"));
        textView4 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text4"));
        titles = new ArrayList<TextView>();
        user_info_title_rl = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                            "id",
                                                                                            "user_info_title_rl"));
        baseForm_title_rl = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                           "id",
                                                                                           "base_form_title_rl"));
        fileUpload_title_rl = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                             "id",
                                                                                             "upload_file_title_rl"));
        lzfs_title_rl = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                       "id",
                                                                                       "lzfs_title_rl"));

        // if (showCert) {
        // textView2.setVisibility(View.VISIBLE);
        // }

        titles.add(textView0);
        titles.add(textView1);
        // if (showCert) {
        // titles.add(textView2);
        // }
        titles.add(textView3);
        titles.add(textView4);
        user_info_title_rl.setOnClickListener(new MyOnClickListener(0));
        baseForm_title_rl.setOnClickListener(new MyOnClickListener(1));
        // textView2.setOnClickListener(new MyOnClickListener(1));
        fileUpload_title_rl.setOnClickListener(new MyOnClickListener(2));
        lzfs_title_rl.setOnClickListener(new MyOnClickListener(3));
    }

    /**
     * 头标点击监听 3
     */
    private class MyOnClickListener
            implements OnClickListener
    {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        /**
         * 控制点击标题只能一步步往下走，不能直接到第三个标题
         */
        public void onClick(View v) {
            viewPager.setCurrentItem(index);

        }

    }

    /**
     * 2 * 初始化动画 3
     */

    private void InitImageView() {
        finish_userinfo = (ImageView) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "finish_userinfo"));
        finish_form = (ImageView) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                "id",
                                                                                "finish_form"));
        finish_file = (ImageView) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                "id",
                                                                                "finish_file"));
        finish_lzfs = (ImageView) findViewById(MSFWResource.getResourseIdByName(this,
                                                                                "id",
                                                                                "finish_lzfs"));

        imageView = (ImageView) findViewById(MSFWResource.getResourseIdByName(this,
                                                                              "id",
                                                                              "cursor"));
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                          .getMetrics(dm);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dm.widthPixels / 4,
                                                                             DensityUtil.dip2px(this,
                                                                                                2));
        imageView.setLayoutParams(params);
        bmpW = dm.widthPixels / 4;
        int screenW = dm.widthPixels;// 获取分辨率宽度 dm.widthPixels * 4 / 25
        if (showCert) {
            pageCount = 5;
        }

        offset = (screenW / pageCount - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        Animation animation = new TranslateAnimation(0, offset, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(1);
        imageView.startAnimation(animation);
    }

    public class MyViewPagerAdapter
            extends PagerAdapter
    {
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

    public class MyOnPageChangeListener
            implements OnPageChangeListener
    {

        int one = offset * 2 + bmpW;
        int two = one * 2;

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageSelected(int arg0) {
            updateTittleStatus();
            if (arg0 == 0) {
                applySubmit.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                goBack2.setVisibility(View.VISIBLE);

            }
            if (arg0 == 1) {
                applySubmit.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                goBack2.setVisibility(View.VISIBLE);

                //根据企业选择变化，变化表单数据


//                if (null != BSNUM && !BSNUM.equals("")) {
//                    new Thread(GetFormByBsNo).start();// 已申报的
//                } else {
                    new Thread(GetInfoByUserid).start();// 新申报的
//                }

            }
            if (arg0 == 2) {
                webView.loadUrl("javascript:androidSave()");
                if (showCert) {
                    next.setVisibility(View.VISIBLE);
                    applySubmit.setVisibility(View.GONE);
                } else {
                    next.setVisibility(View.VISIBLE);
                    applySubmit.setVisibility(View.GONE);
                }
                goBack2.setVisibility(View.VISIBLE);
            } else if (arg0 == 3 && showCert) {// 证照信息
                applySubmit.setVisibility(View.VISIBLE);
                goBack2.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
            }
            if (arg0 == 3 && !showCert) {
                finish_lzfs.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                              "drawable",
                                                                              "tj_right_yes"));
                webView.loadUrl("javascript:androidSave()");
                checkBigFile();

                if (STATUS != -1 && TextUtils.isEmpty(YWGSDName)) {
                    dialog = Background.Process(HistoreShare_v2.this,
                                                getYWGSD,
                                                getString(MSFWResource.getResourseIdByName(
                                                        HistoreShare_v2.this,
                                                        "string",
                                                        "tj_loding")));
                }
                //                if (TextUtils.equals("9",status)&&TextUtils.isEmpty(YWGSDName))
                if (flag == 0) {
                    dialog = Background.Process(mContext,
                                                GetPermWsfwsd,
                                                getString(MSFWResource.getResourseIdByName(
                                                        HistoreShare_v2.this,
                                                        "string",
                                                        "tj_loding")));
                }
                applySubmit.setVisibility(View.VISIBLE);
                goBack2.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(YWGSDName)) {
                    tvYwgsd.setText(YWGSDName);//beizhu
                }
                checkApplyBtn();
            }

            Animation animation = new TranslateAnimation(one * currIndex + offset,
                                                         one * arg0 + offset,
                                                         0,
                                                         0);
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            imageView.startAnimation(animation);
            for (int i = 0; i < titles.size(); i++) {
                titles.get(i)
                      .setTextColor(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                     "color",
                                                                     "tj_tab_text"));
            }
            titles.get(arg0)
                  .setTextColor(HistoreShare_v2.this.getResources()
                                                    .getColor(MSFWResource.getResourseIdByName(
                                                            HistoreShare_v2.this,
                                                            "color",
                                                            "tj_my_green")));
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == AuthUtil.HUAXUN_AUTH) {
            String result = data.getStringExtra("result");

            if (TextUtils.equals(result, "success")) {
                //                DialogUtil.showUIToast(mContext, "认证成功");
                transportEntity.setRealUserAuth(true);
                updateTittleStatus();
                rl_shiming.setVisibility(View.GONE);
                transportEntity = gloabDelegete.getUserInfo();

                realName.setText("姓名：" + transportEntity.getRealName());
                idcardNum.setText("身份证号：" + transportEntity.getIdcardNum());
                realUserAuth.setText("（已实名）");

            } else {
                //                DialogUtil.showUIToast(mContext, "认证失败");
            }


        }
        if (requestCode == ADD_ADDRESS_REQUEST && resultCode == 101) {//邮寄地址选择之后界面展示
            //先判断一下，寄件人信息是否完整
            PostInfo postInfo = (PostInfo) data.getSerializableExtra("postInfo");
            setReceivePostInfo(postInfo);
        } else if (requestCode == ADD_ADDRESS_REQUEST_SEND && resultCode == 101) {//寄件人地址选择
            PostInfo postInfo = (PostInfo) data.getSerializableExtra("postInfo");
            setSendPostInfo(postInfo);
        } else if (requestCode == FILE_REQUEST && data != null) {//添加完图片之后界面更新adapter
            ApplyBean applyBean = (ApplyBean) data.getSerializableExtra("apply");
            Collections.sort(bigFileDate, new ApplyBeanComparator());
            applyBeanAdapterv2 = new ApplyBeanAdapterv2(HistoreShare_v2.this, bigFileDate);
            bigFileList.setAdapter(applyBeanAdapterv2);
        } else if (resultCode == RESULT_OK && requestCode == REQUESTCODE_COMPANY) {


            isselectCompany = (String) data.getStringExtra("isselectCompany");
            if (TextUtils.equals(isselectCompany, "1")) {
                companyBean = (CompanyBean) data.getSerializableExtra("selectCompanyResult");
                isCompanyFirst = false;
                if (mycompanyBeanList != null && mycompanyBeanList.size() != 0) {
                    mycompanyBeanList.clear();

                }
                selectedCompanyId = companyBean.getINC_EXT_ID();
/*<inc_zzjgdm>组织机构代码</inc_zzjgdm>
<inc_jgmc>企业名称</inc_jgmc>
<inc_gszch>统一信用代码</inc_gszch>
<inc_jycs>企业名称</inc_jycs>*/
                inc_zzjgdm = companyBean.getINC_ZZJGDM();
                inc_jgmc = companyBean.getINC_NAME();
                inc_gszch = companyBean.getINC_TYSHXYDM();
                inc_jycs = companyBean.getINC_ADDR();
                inc_deputy = companyBean.getINC_DEPUTY();


                selectedCompanyIsreal = companyBean.getISREALNAME();
                selectedCompanyIsAuthFaren = companyBean.getDEPUTY_ISREALNAME();
                mycompanyBeanList.add(companyBean);
                companyAdapter.showList(mycompanyBeanList);
                ll_no_company.setVisibility(View.GONE);
                rvCompanyList.setVisibility(View.VISIBLE);
            } else {
                new Thread(getCompanyList).start();
            }


        } else if (resultCode == RESULT_OK && requestCode == AuthUtil.HUAXUN_AUTH_FAREN) {

            String result = data.getStringExtra("result");

            if (TextUtils.equals(result, "success")) {
                //                DialogUtil.showUIToast(mContext, "认证成功");
                qymc = companyBean.getINC_NAME();
                tyxydm = companyBean.getINC_TYSHXYDM();
                frdb = companyBean.getINC_DEPUTY();
                frsfzhm = companyBean.getINC_PID();
                INC_EXT_ID = companyBean.getINC_EXT_ID();
                AuthType = "2";
                dialog = Background.Process(HistoreShare_v2.this,
                                            editCompany,
                                            getString(MSFWResource.getResourseIdByName(
                                                    HistoreShare_v2.this,
                                                    "string",
                                                    "tj_loding")));// 获取表单

            } else {
                //                DialogUtil.showUIToast(mContext, "认证失败");
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUESTCODE_DZZZK) {

            ApplyBean applyBean    = (ApplyBean) data.getSerializableExtra("applyBean");
            String    absolutePath = data.getStringExtra("absolutePath");


        }
        //改变lzfs状态
        checkApplyBtn();
        super.onActivityResult(requestCode, resultCode, data);

    }

    class MyHandler
            extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case GET_APPLY_BEAN_SUCCESS:


                    init();

                    Collections.sort(bigFileDate, new ApplyBeanComparator());
                    //                    applyBeanAdapter = new ApplyBeanAdapter(HistoreShare_v2.this, bigFileDate);
                    applyBeanAdapterv2 = new ApplyBeanAdapterv2(HistoreShare_v2.this, bigFileDate);
                    bigFileList.setAdapter(applyBeanAdapterv2);
                    // 获取空间大附件和证照信息
                    if (Constants.isShare && (STATUS == -1 || STATUS == 9)) {// ||
                        dialog = Background.Process(HistoreShare_v2.this,
                                                    GetAttachShare,
                                                    getString(MSFWResource.getResourseIdByName(
                                                            HistoreShare_v2.this,
                                                            "string",
                                                            "tj_loding")));
                    }
                    break;
                case GET_BLMS_SUCCESS:
                    if (blmsBean != null) {
                        if (STATUS != -1 && flag == 0) {
                            dialog = Background.Process(HistoreShare_v2.this,
                                                        GetBusiPostInfo2,
                                                        getString(MSFWResource.getResourseIdByName(
                                                                HistoreShare_v2.this,
                                                                "string",
                                                                "tj_loding")));
                            dialog = Background.Process(HistoreShare_v2.this,
                                                        GetBusiPostInfo,
                                                        getString(MSFWResource.getResourseIdByName(
                                                                HistoreShare_v2.this,
                                                                "string",
                                                                "tj_loding")));
                        }
                    } else {
                        blmsBean = new BLMSBean();
                        blmsBean.setDJZZCL("1");
                        blmsBean.setLQSPJG("1");
                    }
                    parent_ll.setVisibility(View.VISIBLE);
                    controllViewGetBLMS();
                    if (TextUtils.isEmpty(YWGSDName) && STATUS == -1) {
                        dialog = Background.Process(HistoreShare_v2.this,
                                                    getYWGSDV2,
                                                    getString(MSFWResource.getResourseIdByName(
                                                            HistoreShare_v2.this,
                                                            "string",
                                                            "tj_loding")));
                    } else {
                        if (!TextUtils.equals("6", ywgsd)) {
                            tvYwgsd.setText(YWGSDName);

                        }
                        flag++;
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
    final Runnable checkFZBD     = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject object = new JSONObject();
                object.put("PERMID", PERMID);
                String str = HTTP.excuteAndCache("checkComplexForm",
                                                 "RestPermissionitemService",
                                                 object.toString(),
                                                 HistoreShare_v2.this);
                JSONObject jb = new JSONObject(str);
                String     cd = jb.getString("code");
                if (cd.equals("200")) {
                    sffzbd = new JSONObject(jb.getString("ReturnValue")).getString("SFFZBD");
                    if (null != sffzbd && (sffzbd.equals("1") || cheackFormbyPC())) {// 包含复杂表单
                        runOnUiThread(new Runnable() {
                            public void run() {
                                new AlertDialog.Builder(HistoreShare_v2.this).setMessage(
                                        MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                         "string",
                                                                         "tj_notice"))
                                                                             .setTitle("温馨提示")
                                                                             .setPositiveButton("暂存",
                                                                                                new DialogInterface.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(
                                                                                                            DialogInterface mdialog,
                                                                                                            int which)
                                                                                                    {
                                                                                                        status = "9";
                                                                                                        dialog = Background.Process(
                                                                                                                HistoreShare_v2.this,
                                                                                                                ApplySubmit,
                                                                                                                getString(
                                                                                                                        MSFWResource.getResourseIdByName(
                                                                                                                                HistoreShare_v2.this,
                                                                                                                                "string",
                                                                                                                                "tj_loding")));
                                                                                                    }
                                                                                                })
                                                                             .setNegativeButton("取消",
                                                                                                new DialogInterface.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(
                                                                                                            DialogInterface dialog,
                                                                                                            int which)
                                                                                                    {
                                                                                                        return;
                                                                                                    }
                                                                                                })
                                                                             .show();
                            }
                        });


                    } else {
                        status = "0";
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dialog = Background.Process(HistoreShare_v2.this,
                                                            ApplySubmit,
                                                            getString(MSFWResource.getResourseIdByName(
                                                                    HistoreShare_v2.this,
                                                                    "string",
                                                                    "tj_loding")));
                            }
                        });
                    }


                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }

    };
    final Runnable checkFZBDv2   = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject object = new JSONObject();
                object.put("PERMID", PERMID);
                String str = HTTP.excuteAndCache("checkComplexForm",
                                                 "RestPermissionitemService",
                                                 object.toString(),
                                                 HistoreShare_v2.this);
                JSONObject jb = new JSONObject(str);
                String     cd = jb.getString("code");
                if (cd.equals("200")) {
                    String mysffzbd = new JSONObject(jb.getString("ReturnValue")).getString("SFFZBD");
                    if (null != mysffzbd && (mysffzbd.equals("1") || cheackFormbyPC())) {// 包含复杂表单
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //判断底部那块的显示隐藏的,现在是包含复杂表单的情况
                                //0是已申报  9 是暂存  -1是新申报
                                if (STATUS == 9 || STATUS == -1) {
                                    //                                    tvFuzatip.setVisibility(View.VISIBLE);
                                } else {
                                    tvFuzatip.setVisibility(View.GONE);
                                }
                                if (STATUS == 9) {
                                    ivScan.setVisibility(View.VISIBLE);
                                } else {
                                    ivScan.setVisibility(View.GONE);
                                }

                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ivScan.setVisibility(View.GONE);
                                tvFuzatip.setVisibility(View.GONE);

                            }
                        });


                    }


                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }

    };
    /**
     * 检测复杂表单跳转暂存的
     */
    final Runnable checkFZBDsave = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject object = new JSONObject();
                object.put("PERMID", PERMID);
                String str = HTTP.excuteAndCache("checkComplexForm",
                                                 "RestPermissionitemService",
                                                 object.toString(),
                                                 HistoreShare_v2.this);
                JSONObject jb = new JSONObject(str);
                String     cd = jb.getString("code");
                if (cd.equals("200")) {
                    sffzbd = new JSONObject(jb.getString("ReturnValue")).getString("SFFZBD");
                    if (null != sffzbd && (sffzbd.equals("1") || cheackFormbyPC())) {// 包含复杂表单
                        runOnUiThread(new Runnable() {
                            public void run() {
                                status = "9";
                                dialog = Background.Process(HistoreShare_v2.this,
                                                            ApplySubmit,
                                                            getString(MSFWResource.getResourseIdByName(
                                                                    HistoreShare_v2.this,
                                                                    "string",
                                                                    "tj_loding")));

                            }
                        });

                    } else {
                        status = "9";
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dialog = Background.Process(HistoreShare_v2.this,
                                                            ApplySubmit,
                                                            getString(MSFWResource.getResourseIdByName(
                                                                    HistoreShare_v2.this,
                                                                    "string",
                                                                    "tj_loding")));
                            }
                        });
                    }

                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
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
                String businessXML  = getBusinessXML();
                String materialsXML = getMaterialsXML();
                String postXML      = getPostXML();
                System.out.println("fuchl#####:businessXML" + businessXML);
                System.out.println("fuchl############:postXML" + postXML);
                System.out.println("------------------------------materialsXML=" + materialsXML);

                if (null == formsXML || formsXML.equals("")) {
                    DialogUtil.showUIToast(HistoreShare_v2.this, "请先完善表单信息");
                    return;
                }
                if (null == businessXML || businessXML.equals("")) {
                    DialogUtil.showUIToast(HistoreShare_v2.this, "业务信息异常");
                    return;
                }
                if (null == materialsXML || materialsXML.equals("")) {
                    DialogUtil.showUIToast(HistoreShare_v2.this, "材料信息异常");
                    return;
                }

                // 非新申请formXML 处理
                if (null != BSNUM) {
                    InputStream is = new ByteArrayInputStream(formsXML.getBytes());
                    Document doc = DocumentBuilderFactory.newInstance()
                                                         .newDocumentBuilder()
                                                         .parse(is);
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
                    DOMSource          domSource   = new DOMSource(doc);
                    StringWriter       writer      = new StringWriter();
                    StreamResult       result      = new StreamResult(writer);
                    TransformerFactory tf          = TransformerFactory.newInstance();
                    Transformer        transformer = tf.newTransformer();
                    transformer.transform(domSource, result);
                    formsXML = writer.toString();

                }

                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("FORMS", new String(Base64.encode(formsXML.getBytes(), 0), "UTF-8"));
                param.put("BUSINESS",
                          new String(Base64.encode(businessXML.getBytes(), 0), "UTF-8"));
                param.put("MATERIALS",
                          new String(Base64.encode(materialsXML.getBytes(), 0), "UTF-8"));
                param.put("POSTXML", new String(Base64.encode(postXML.getBytes(), 0), "UTF-8"));

                String response = HTTP.excuteAndCache("submit",
                                                      "RestOnlineDeclareService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                final String bsnum = json.getJSONObject("ReturnValue")
                                         .getString("bsnum");
                String code = json.getString("code");
                if (code.equals("200")) {
                    if (status.equals("0")) {
                        // DialogUtil.showUIToast(HistoreShare_v2.this,
                        // "温馨提示：您的业务已经申报成功，请根据手机短信提醒，到现场递交纸质材料或领取证照。"+ "\n"
                        // +"服务网点：宝安区政务服务中心，地址：深圳市宝安区体育中心综合训练馆一楼");
                        DialogUtil.showUIToast(HistoreShare_v2.this, "申报成功！");
                        if (STATUS == -1) {
                            intent = new Intent();
                            intent.setClass(HistoreShare_v2.this, WDBJ.class);
                            startActivity(intent);
                            finish();
                        } else {
                            setResult(100);
                            finish();
                        }
                    } else {
                        if (null != sffzbd && (sffzbd.equals("1") || cheackFormbyPC())) {// 包含复杂表单

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    new AlertDialog.Builder(HistoreShare_v2.this).setMessage(
                                            "暂存成功!请前往电脑端登录（http://bsdt.baoan.gov.cn/appscan）继续填报相关信息")
                                                                                 .setTitle("提示")
                                                                                 .setCancelable(
                                                                                         false)
                                                                                 .setPositiveButton(
                                                                                         "去扫描",
                                                                                         new DialogInterface.OnClickListener() {
                                                                                             public void onClick(
                                                                                                     DialogInterface dialog,
                                                                                                     int id)
                                                                                             {
                                                                                                 intent = new Intent();
                                                                                                 intent.putExtra(
                                                                                                         "flag",
                                                                                                         3);
                                                                                                 intent.putExtra(
                                                                                                         "SXID",
                                                                                                         PERMID);
                                                                                                 intent.putExtra(
                                                                                                         "BSNUM",
                                                                                                         bsnum);
                                                                                                 intent.setClass(
                                                                                                         HistoreShare_v2.this,
                                                                                                         CaptureActivity.class);
                                                                                                 startActivityForResult(
                                                                                                         intent,
                                                                                                         100);// 二维码扫描;
                                                                                             }
                                                                                         })
                                                                                 .setNegativeButton(
                                                                                         "取消",
                                                                                         new DialogInterface.OnClickListener() {
                                                                                             @Override
                                                                                             public void onClick(
                                                                                                     DialogInterface dialog,
                                                                                                     int which)
                                                                                             {
                                                                                                 dialog.dismiss();
                                                                                                 if (STATUS == -1) {
                                                                                                     intent = new Intent();
                                                                                                     intent.setClass(
                                                                                                             HistoreShare_v2.this,
                                                                                                             WDBJ.class);
                                                                                                     startActivity(
                                                                                                             intent);
                                                                                                     finish();
                                                                                                 } else {
                                                                                                     setResult(
                                                                                                             100);
                                                                                                     finish();
                                                                                                 }
                                                                                             }
                                                                                         })
                                                                                 .show();
                                }
                            });
                        } else {
                            if (STATUS == -1) {
                                intent = new Intent();
                                intent.setClass(HistoreShare_v2.this, WDBJ.class);
                                startActivity(intent);
                                finish();
                            } else {
                                setResult(100);
                                finish();
                            }
                        }
                    }


                } else {
                    DialogUtil.showUIToast(HistoreShare_v2.this, json.getString("error"));
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
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

    private String getMaterialsXML()
            throws Exception
    {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter  writer     = new StringWriter();
        serializer.setOutput(writer);
        serializer.startDocument("utf-8", null);
        serializer.startTag("", "materials");
        for (int i = 0; i < applyBeans.size(); i++) {
            serializer.startTag("", "materialinfo");
            serializer.startTag("", "id");
            if (null != applyBeans.get(i)
                                  .getCASEID())
            {
                serializer.cdsect(applyBeans.get(i)
                                            .getCASEID());
            }
            serializer.endTag("", "id");
            serializer.startTag("", "materialid");
            serializer.cdsect(applyBeans.get(i)
                                        .getID());
            serializer.endTag("", "materialid");
            serializer.startTag("", "materialname");
            serializer.cdsect(applyBeans.get(i)
                                        .getCLMC());
            serializer.endTag("", "materialname");
            serializer.startTag("", "materialcode");
            serializer.cdsect(applyBeans.get(i)
                                        .getCLBH());
            serializer.endTag("", "materialcode");
            serializer.startTag("", "submittype");
            serializer.cdsect(applyBeans.get(i)
                                        .getDZHYQ());
            serializer.endTag("", "submittype");
            serializer.startTag("", "orinum");
            serializer.cdsect(applyBeans.get(i)
                                        .getORINUM());
            serializer.endTag("", "orinum");
            serializer.startTag("", "copynum");
            serializer.cdsect(applyBeans.get(i)
                                        .getCOPYNUM());
            serializer.endTag("", "copynum");
            serializer.startTag("", "isneed");
            serializer.cdsect(applyBeans.get(i)
                                        .getSFBY());
            serializer.endTag("", "isneed");
            serializer.startTag("", "status");

            if (null != Constants.material.get(applyBeans.get(i)
                                                         .getCLBH()) && Constants.material.get(
                    applyBeans.get(i)
                              .getCLBH())
                                                                                          .size() > 0)
            {// 设置提交状态
                serializer.cdsect("1");
            } else {
                serializer.cdsect("0");
            }

            serializer.endTag("", "status");
            serializer.startTag("", "formid");
            if (null != applyBeans.get(i)
                                  .getFORMID())
            {
                serializer.cdsect(applyBeans.get(i)
                                            .getFORMID());
            }
            serializer.endTag("", "formid");
            serializer.startTag("", "formver");
            if (null != applyBeans.get(i)
                                  .getFORMVER())
            {
                serializer.cdsect(applyBeans.get(i)
                                            .getFORMVER());
            }
            serializer.endTag("", "formver");
            serializer.startTag("", "dataid");
            serializer.cdsect("");
            serializer.endTag("", "dataid");
            if (!TextUtils.isEmpty(applyBeans.get(i)
                                             .getCERTIFICATEID()))
            {
                serializer.startTag("", "certificateid");
                serializer.cdsect(applyBeans.get(i)
                                            .getCERTIFICATEID());
                serializer.endTag("", "certificateid");
            } else {
                serializer.startTag("", "certificateid");
                serializer.cdsect("");
                serializer.endTag("", "certificateid");
            }
            if (!TextUtils.isEmpty(applyBeans.get(i)
                                             .getCERTIFICATESTARTDATE()))
            {
                serializer.startTag("", "certificatestartdate");
                serializer.cdsect(applyBeans.get(i)
                                            .getCERTIFICATESTARTDATE());
                serializer.endTag("", "certificatestartdate");
            } else {
                serializer.startTag("", "certificatestartdate");
                serializer.cdsect("");
                serializer.endTag("", "certificatestartdate");
            }
            if (!TextUtils.isEmpty(applyBeans.get(i)
                                             .getCERTIFICATEENDDATE()))
            {
                serializer.startTag("", "certificateenddate");
                serializer.cdsect(applyBeans.get(i)
                                            .getCERTIFICATEENDDATE());
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

            if (null != Constants.material.get(applyBeans.get(i)
                                                         .getCLBH()))
            {// 设置文件节点

                for (int j = 0; j < Constants.material.get(applyBeans.get(i)
                                                                     .getCLBH())
                                                      .size(); j++) {
                    serializer.startTag("", "file");

                    serializer.startTag("", "fileid");
                    serializer.cdsect(Constants.material.get(applyBeans.get(i)
                                                                       .getCLBH())
                                                        .get(j)
                                                        .getID());
                    serializer.endTag("", "fileid");
                    serializer.startTag("", "filename");
                    serializer.cdsect(Constants.material.get(applyBeans.get(i)
                                                                       .getCLBH())
                                                        .get(j)
                                                        .getATTACHNAME());
                    serializer.endTag("", "filename");
                    serializer.startTag("", "filepath");
                    serializer.cdsect(Constants.material.get(applyBeans.get(i)
                                                                       .getCLBH())
                                                        .get(j)
                                                        .getATTACHURL());
                    serializer.endTag("", "filepath");
                    serializer.startTag("", "id");
                    serializer.cdsect(null == Constants.material.get(applyBeans.get(i)
                                                                               .getCLBH())
                                                                .get(j)
                                                                .getATTACHUID()
                                      ? ""
                                      : Constants.material.get(applyBeans.get(i)
                                                                         .getCLBH())
                                                          .get(j)
                                                          .getATTACHUID());
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
        return writer.toString()
                     .replaceAll("<\\?xml.*.\\?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

    }

    private String getBusinessXML()
            throws Exception
    {
        Log.e("sps====", "YWGSDID===" + YWGSDID);
        Log.e("sps====", "YWGSDName===" + YWGSDName);

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter  writer     = new StringWriter();
        serializer.setOutput(writer);
        serializer.startDocument("utf-8", null);
        serializer.startTag("", "business");

        serializer.startTag("", "cbusinessid");// 协同业务流水号
        serializer.cdsect(null == permission.getCBUSINESSID()
                          ? ""
                          : permission.getCBUSINESSID());
        serializer.endTag("", "cbusinessid");
        serializer.startTag("", "citemid");// 协同事项编号
        serializer.cdsect(null == permission.getCITEMID()
                          ? ""
                          : permission.getCITEMID());
        serializer.endTag("", "citemid");
        serializer.startTag("", "citemversion"); // 协同事项版本
        serializer.cdsect(null == permission.getCITEMVERSION()
                          ? ""
                          : permission.getCITEMVERSION());
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
        serializer.cdsect(null == P_GROUP_ID
                          ? ""
                          : P_GROUP_ID);
        serializer.endTag("", "groupid");
        serializer.startTag("", "groupname");
        serializer.cdsect(null == P_GROUP_NAME
                          ? ""
                          : P_GROUP_NAME);
        serializer.endTag("", "groupname");
        serializer.startTag("", "lzfs");
        serializer.cdsect(LzfsType);
        serializer.endTag("", "lzfs");

        serializer.startTag("", "ywgsd");
        serializer.cdsect(null == YWGSDID
                          ? ""
                          : YWGSDID);
        serializer.endTag("", "ywgsd");
        serializer.startTag("", "ywgsdmc");
        serializer.cdsect(null == YWGSDName
                          ? ""
                          : YWGSDName);
        serializer.endTag("", "ywgsdmc");
        if (rbQybs.isChecked()) {

            serializer.startTag("", "inc_ext_id");

            serializer.cdsect(null == selectedCompanyId
                              ? ""
                              : selectedCompanyId);

            serializer.endTag("", "inc_ext_id");
        } else {
            serializer.startTag("", "inc_ext_id");

            serializer.cdsect("");

            serializer.endTag("", "inc_ext_id");
        }

        serializer.endTag("", "business");
        serializer.endDocument();
        String xml = writer.toString();
        return xml;

    }

    /**
     * 获取邮政速递xml
     */
    private String getPostXML()
            throws Exception
    {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter  writer     = new StringWriter();
        serializer.setOutput(writer);
        serializer.startDocument("utf-8", null);
        serializer.startTag("", "emsinfos");
        if (null != Constants.sendPostInfo && DJFS_RG.getCheckedRadioButtonId() == YJDJ.getId()) {
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

            serializer.startTag("", "province");
            serializer.cdsect(Constants.sendPostInfo.getPROVINCE());
            serializer.endTag("", "province");
            serializer.startTag("", "city");
            serializer.cdsect(Constants.sendPostInfo.getCITY());
            serializer.endTag("", "city");
            serializer.startTag("", "country");
            serializer.cdsect(Constants.sendPostInfo.getCOUNTRY());
            serializer.endTag("", "country");

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
            serializer.cdsect(TextUtils.isEmpty(Constants.getPostInfo.getRECEIVE())
                              ? ""
                              : Constants.getPostInfo.getRECEIVE());
            serializer.endTag("", "xm");
            serializer.startTag("", "dw");
            serializer.cdsect("");
            serializer.endTag("", "dw");
            serializer.startTag("", "province");
            serializer.cdsect(TextUtils.isEmpty(Constants.getPostInfo.getPROVINCE())
                              ? ""
                              : Constants.getPostInfo.getPROVINCE());
            serializer.endTag("", "province");
            serializer.startTag("", "city");
            serializer.cdsect(TextUtils.isEmpty(Constants.getPostInfo.getCITY())
                              ? ""
                              : Constants.getPostInfo.getCITY());
            serializer.endTag("", "city");
            serializer.startTag("", "country");
            serializer.cdsect(TextUtils.isEmpty(Constants.getPostInfo.getCOUNTRY())
                              ? ""
                              : Constants.getPostInfo.getCOUNTRY());
            serializer.endTag("", "country");
            serializer.startTag("", "dz");
            serializer.cdsect(TextUtils.isEmpty(Constants.getPostInfo.getADDRESS())
                              ? ""
                              : Constants.getPostInfo.getADDRESS());
            serializer.endTag("", "dz");
            serializer.startTag("", "dh");
            serializer.cdsect(TextUtils.isEmpty(Constants.getPostInfo.getPHONE())
                              ? ""
                              : Constants.getPostInfo.getPHONE());
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
     * @return 是否包含复杂表单
     */
    private boolean cheackFormbyPC() {
        if (null != applyBeans) {
            for (int i = 0; i < applyBeans.size(); i++) {
                if (applyBeans.get(i)
                              .getDZHYQ()
                              .contains("1"))
                {
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
        LZFS_RG = (RadioGroup) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                      "id",
                                                                                      "radioGroup"));
        WDLQ = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "WDLQ"));
        chooseAddress = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                             "id",
                                                                                             "YJLQ"));
        ZXDY = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "ZXDY"));
        wddjTip = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "wddj_tv"));
        yjdjTip = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "yjdj_tv"));
        wdlqTip = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "wdlq_tv"));
        yzlqTip = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "yzlq_tv"));
        wxdjzzclTip = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                        "id",
                                                                                        "wxdjzzcl_tv"));
        djAddr = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                   "id",
                                                                                   "dj_address"));
        lqAddr = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                   "id",
                                                                                   "lq_address"));


        tvGongzhonghao = (TextView) LzfsPage.findViewById(R.id.tv_gongzhonghao);

        String                 sm  = "提示：敬请关注《掌上宝安》微信公众号，实时查看办理进度。";
        SpannableStringBuilder ssb = new SpannableStringBuilder(sm);
        ssb.setSpan(new ForegroundColorSpan(Color.RED), 8, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        tvGongzhonghao.setText(ssb);

        zxdyTipTv = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                      "id",
                                                                                      "zxdy_tv"));
        //添加邮寄地址
        address_rl = (RelativeLayout) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                             "id",
                                                                                             "addAddress_ll"));

        address_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(HistoreShare_v2.this, ExpressageList.class);
                startActivityForResult(intent, ADD_ADDRESS_REQUEST);
            }
        });
        //添加寄件人地址
        rl_address_send = (RelativeLayout) LzfsPage.findViewById(MSFWResource.getResourseIdByName(
                this,
                "id",
                "rl_address_send"));

        rl_address_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("flag", 1);
                intent.setClass(HistoreShare_v2.this, ExpressageList.class);
                startActivityForResult(intent, ADD_ADDRESS_REQUEST_SEND);
            }
        });
        wdlqParentRl = (LinearLayout) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                             "id",
                                                                                             "wdlq_rl"));
        parent_ll = (LinearLayout) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                          "id",
                                                                                          "parent_ll"));
        parent_ll.setVisibility(View.GONE);
        DJFS_RG = (RadioGroup) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                      "id",
                                                                                      "DJRidaoGroup"));
        WDDJ = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "WDDJ"));
        YJDJ = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "YJDJ"));
        WXDJZZCL = (RadioButton) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                        "id",
                                                                                        "WXDJZZCL"));

        blmsTitle = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                      "id",
                                                                                      "blms_title"));
        DJCLparent_ll = (LinearLayout) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                              "id",
                                                                                              "DJCLparent_ll"));
        LZFSparent_ll = (LinearLayout) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                              "id",
                                                                                              "LZFSparent_ll"));
        chooseDJCL_tv = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                          "id",
                                                                                          "chooseDJCL_tv"));
        chooseLZFS_tv = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                          "id",
                                                                                          "chooseLZFS_tv"));

        RECEIVE = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "RECEIVE"));
        PHONE = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                  "id",
                                                                                  "PHONE"));
        ADDRESS = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "ADDRESS"));
        addressEmpty = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                         "id",
                                                                                         "address_emptp_tip"));
        addressDetail = (RelativeLayout) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                                "id",
                                                                                                "address_info_detail"));
        SEND = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                 "id",
                                                                                 "SEND"));
        PHONE_SEND = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                       "id",
                                                                                       "PHONE_SEND"));
        ADDRESS_SEND = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                         "id",
                                                                                         "ADDRESS_MY"));
        tv_temp_myaddress = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                              "id",
                                                                                              "tv_temp_myaddress"));
        rl_myaddresss_detail = (RelativeLayout) LzfsPage.findViewById(MSFWResource.getResourseIdByName(
                this,
                "id",
                "rl_myaddresss_detail"));
        receiveTip = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                       "id",
                                                                                       "recever_tip"));
        receiveTip.setText("承诺办理时限:" + permission.getITEMLIMITTIME() + "个工作日，具体结果以短信通知为主");
        if (STATUS != -1 && STATUS != 9) {
            chooseDJCL_tv.setText("递交材料方式：");
            chooseLZFS_tv.setText("领取证照方式：");
            address_rl.setClickable(false);
            rl_address_send.setClickable(false);
        }
        ywgsdParent = (LinearLayout) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                            "id",
                                                                                            "ywgsd_parent_ll"));
        parentSpinner = (Spinner) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                         "id",
                                                                                         "parent_spinner"));
        childSpinner = (Spinner) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                        "id",
                                                                                        "child_spinner"));
        thirdSpinner = (Spinner) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                        "id",
                                                                                        "third_spinner"));
        tvYwgsd = (TextView) LzfsPage.findViewById(MSFWResource.getResourseIdByName(this,
                                                                                    "id",
                                                                                    "tv_ywgsd"));
        parentNames = new ArrayList<String>();
        parentAdapter = new ArrayAdapter<String>(HistoreShare_v2.this,
                                                 MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                  "layout",
                                                                                  "left_spinner"),
                                                 parentNames);
        parentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parentSpinner.setAdapter(parentAdapter);
        //        parentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        //            @Override
        //            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //                // TODO Auto-generated method stub
        //                if (parentItems != null && parentItems.size() > 0) {
        //                    if (level >= layers.length-1) {
        //                        YWGSDID = parentItems.get(position).getAREAID();
        //                        YWGSDName = parentItems.get(position).getAREANAME();
        //                        parentId = "";
        //                        level = 0;
        //                        dialog = Background.Process(HistoreShare_v2.this, GetNetwork, getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this, "string", "tj_loding")));
        //                    } else {
        //                       selectNextDialog(parentItems.get(position));
        //                    }
        //
        //                }
        //
        //            }
        //
        //            @Override
        //            public void onNothingSelected(AdapterView<?> parent) {
        //                // TODO Auto-generated method stub
        //
        //            }
        //        });
        tvYwgsd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STATUS == 9 || STATUS == -1) {
                    level = 0;
                    foreRegion = null;
                    dialog = Background.Process(HistoreShare_v2.this,
                                                getYWGSDV2,
                                                getString(MSFWResource.getResourseIdByName(
                                                        HistoreShare_v2.this,
                                                        "string",
                                                        "tj_loding")));
                }

            }
        });
        childNames = new ArrayList<String>();
        childAdapter = new ArrayAdapter<String>(HistoreShare_v2.this,
                                                MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                 "layout",
                                                                                 "left_spinner"),
                                                childNames);
        childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childSpinner.setAdapter(childAdapter);
        childSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                YWGSDID = childItems.get(position)
                                    .getAREAID();
                YWGSDName = childItems.get(position)
                                      .getAREANAME();
                dialog = Background.Process(HistoreShare_v2.this,
                                            GetNetwork,
                                            getString(MSFWResource.getResourseIdByName(
                                                    HistoreShare_v2.this,
                                                    "string",
                                                    "tj_loding")));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        //这里调用业务归属地的方法
        //老版的
        //        dialog = Background.Process(HistoreShare_v2.this, getYWGSD, getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this, "string", "tj_loding")));
        //新版的
        //获取业务归属地的第一级
        //        dialog = Background.Process(HistoreShare_v2.this, getYWGSDV2,
        //                getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this, "string", "tj_loding")));
        /**
         * 递交材料选择
         */
        DJFS_RG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == WDDJ.getId()) {
                    //					wddjTip.setVisibility(View.VISIBLE);
                    djAddr.setVisibility(View.VISIBLE);
                    yjdjTip.setVisibility(View.GONE);
                    rl_address_send.setVisibility(View.GONE);
                    wxdjzzclTip.setVisibility(View.GONE);

                } else if (checkedId == YJDJ.getId()) {
                    djAddr.setVisibility(View.GONE);
                    wddjTip.setVisibility(View.GONE);
                    yjdjTip.setVisibility(View.VISIBLE);
                    rl_address_send.setVisibility(View.VISIBLE);
                    wxdjzzclTip.setVisibility(View.GONE);
                    setSendPostInfo(Constants.sendPostInfo);
                    //					PostInfo sendPost = new PostInfo();
                    //					sendPost.setRECEIVE("邮寄方式");
                    //					sendPost.setPHONE("邮寄电话");
                    //					sendPost.setPOSTCODE("邮寄编码");
                    //					sendPost.setADDRESS("邮寄地址");
                    //					Constants.sendPostInfo = sendPost;
                } else {
                    wxdjzzclTip.setVisibility(View.VISIBLE);
                    djAddr.setVisibility(View.GONE);
                    wddjTip.setVisibility(View.GONE);
                    yjdjTip.setVisibility(View.GONE);
                    rl_address_send.setVisibility(View.GONE);
                }
                checkApplyBtn();
            }
        });
        // 领证方式选择
        LZFS_RG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == WDLQ.getId()) {// 网点领取
                    LzfsType = "D";
                    lqAddr.setVisibility(View.VISIBLE);
                    address_rl.setVisibility(View.GONE);
                    //					wdlqTip.setVisibility(View.VISIBLE);
                    yzlqTip.setVisibility(View.GONE);
                    receiveTip.setVisibility(View.VISIBLE);
                    zxdyTipTv.setVisibility(View.GONE);
                } else if (checkedId == chooseAddress.getId()) {// 邮寄领取
                    LzfsType = "EMS";
                    lqAddr.setVisibility(View.GONE);
                    address_rl.setVisibility(View.VISIBLE);
                    wdlqTip.setVisibility(View.GONE);
                    yzlqTip.setVisibility(View.VISIBLE);
                    zxdyTipTv.setVisibility(View.GONE);
                    receiveTip.setVisibility(View.GONE);
                    setReceivePostInfo(Constants.getPostInfo);
                } else if (checkedId == ZXDY.getId()) {// 自行打印
                    LzfsType = "P";
                    lqAddr.setVisibility(View.GONE);
                    address_rl.setVisibility(View.GONE);
                    wdlqTip.setVisibility(View.GONE);
                    yzlqTip.setVisibility(View.GONE);
                    receiveTip.setVisibility(View.GONE);
                    zxdyTipTv.setVisibility(View.VISIBLE);
                    zxdyTipTv.setText("说明：请申请人于业务办结登陆PC网厅“我的办事”点击“我的证照”栏目下载电子证照并打印\n");
                    Constants.getPostInfo = null;
                }
                checkApplyBtn();
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
                chooseAddress.setEnabled(true);
                WDLQ.setEnabled(true);
                ZXDY.setEnabled(true);
                WDDJ.setEnabled(true);
                YJDJ.setEnabled(true);
            } else {
                chooseAddress.setEnabled(false);
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

    @SuppressLint("ResourceAsColor")
    private void selectNameDialog() {

        ArrayAdapter<String> adapter = null;
        if (foreRegion == null) {

            adapter = new YWGSDAdapter(true, this, R.layout.simple_list_item_1, parentNames);
        } else {
            adapter = new YWGSDAdapter(false, this, R.layout.simple_list_item_1, parentNames);

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoreShare_v2.this);
        builder.setTitle("请选择业务归属地");

        String[] nameArray = new String[parentNames.size()];
        // List转换成数组
        for (int i = 0; i < parentNames.size(); i++) {
            nameArray[i] = parentNames.get(i);
        }

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface mdialog, int position) {
                if (parentItems != null && parentItems.size() > 0) {
                    YWGSDID = parentItems.get(position)
                                         .getAREAID();
                    YWGSDName = parentItems.get(position)
                                           .getAREANAME();
                    if (level + 1 >= layers.length) {

                        parentId = "";
                        level = 0;
                        dialog = Background.Process(HistoreShare_v2.this,
                                                    GetNetwork,
                                                    getString(MSFWResource.getResourseIdByName(
                                                            HistoreShare_v2.this,
                                                            "string",
                                                            "tj_loding")));
                    } else {
                        //                        if (foreRegion==null) {
                        //                            selectNextDialog(parentItems.get(position));

                        //                        } else {
                        //                            if (position == 0) {
                        YWGSDID = parentItems.get(position)
                                             .getAREAID();
                        YWGSDName = parentItems.get(position)
                                               .getAREANAME();
                        parentId = "";
                        level = 0;
                        dialog = Background.Process(HistoreShare_v2.this,
                                                    GetNetwork,
                                                    getString(MSFWResource.getResourseIdByName(
                                                            HistoreShare_v2.this,
                                                            "string",
                                                            "tj_loding")));
                        mdialog.dismiss();
                        //                            } else {
                        //                                selectNextDialog(parentItems.get(position));
                        //
                        //                            }


                        //                        }
                    }
                    tvYwgsd.setText(YWGSDName);
                    checkApplyBtn();

                }
            }
        });
        builder.create()
               .show();
    }

    //提示是否选择下一级别
    private void selectNextDialog(final Region region) {
        //        new Builder(this)
        //                .setTitle("提示")
        //                .setMessage("是否选择下一层级")
        //                .setPositiveButton("继续选择", new DialogInterface.OnClickListener() {
        //                    @Override
        //                    public void onClick(DialogInterface mdialog, int which) {
        parentId = region.getAREAID();
        level++;
        foreRegion = region;
        // 选择下一层级
        dialog = Background.Process(HistoreShare_v2.this,
                                    getYWGSDV2,
                                    getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                               "string",
                                                                               "tj_loding")));
        //                    }
        //                })
        //                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
        //                    @Override
        //                    public void onClick(DialogInterface mdialog, int which) {
        //                        YWGSDID = region.getAREAID();
        //                        YWGSDName = region.getAREANAME();
        //                        parentId = "";
        //                        level = 0;
        //                        dialog = Background.Process(HistoreShare_v2.this, GetNetwork, getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this, "string", "tj_loding")));
        //                        mdialog.dismiss();
        //
        //                    }
        //                }).setCancelable(false).show();
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
                String response = HTTP.excuteAndCache("getBusiPostInfo",
                                                      "RestEMSService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    emsBean = JSONUtil.getGson()
                                      .fromJson(json.getString("ReturnValue"), EMSBean.class);
                    postInfos2 = emsBean.getItems();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!TextUtils.isEmpty(emsBean.getLZFS())) {// 在10号之前暂存和申报没有lzfs传到服务器，兼容以前版本。
                                if (STATUS == 9) {// 暂存件
                                    if (emsBean.getLZFS()
                                               .equals("D"))
                                    {
                                        WDLQ.setChecked(true);
                                        address_rl.setVisibility(View.GONE);
                                        zxdyTipTv.setVisibility(View.GONE);

                                    } else if (emsBean.getLZFS()
                                                      .equals("EMS"))
                                    {
                                        chooseAddress.setChecked(true);
                                        address_rl.setVisibility(View.VISIBLE);
                                        zxdyTipTv.setVisibility(View.GONE);
                                        if (postInfos2 != null && postInfos2.size() > 0) {
                                            setReceivePostInfo(postInfos2.get(0));
                                        } else {
                                            addressDetail.setVisibility(View.GONE);
                                            addressEmpty.setVisibility(View.VISIBLE);
                                        }

                                    } else if (emsBean.getLZFS()
                                                      .equals("P"))
                                    {
                                        address_rl.setVisibility(View.GONE);
                                        ZXDY.setVisibility(View.VISIBLE);
                                        ZXDY.setChecked(true);
                                    } else {
                                        LZFSparent_ll.setVisibility(View.GONE);
                                    }
                                } else {// 非暂存件
                                    if (emsBean.getLZFS()
                                               .equals("D"))
                                    {
                                        address_rl.setVisibility(View.GONE);
                                        WDLQ.setChecked(true);
                                        WDLQ.setVisibility(View.VISIBLE);
                                        chooseAddress.setVisibility(View.GONE);
                                        ZXDY.setVisibility(View.GONE);
                                    } else if (emsBean.getLZFS()
                                                      .equals("EMS"))
                                    {
                                        ZXDY.setVisibility(View.GONE);
                                        WDLQ.setVisibility(View.GONE);
                                        chooseAddress.setChecked(true);
                                        chooseAddress.setVisibility(View.VISIBLE);
                                        address_rl.setVisibility(View.VISIBLE);
                                        zxdyTipTv.setVisibility(View.GONE);
                                        setReceivePostInfo(postInfos2.get(0));
                                    } else if (emsBean.getLZFS()
                                                      .equals("P"))
                                    {
                                        chooseAddress.setVisibility(View.GONE);
                                        address_rl.setVisibility(View.GONE);
                                        ZXDY.setVisibility(View.VISIBLE);
                                        ZXDY.setChecked(true);
                                        WDLQ.setVisibility(View.GONE);
                                        //										zxdyTipTv.setVisibility(View.VISIBLE);
                                    } else {
                                        LZFSparent_ll.setVisibility(View.GONE);
                                    }
                                }
                            } else if (null != postInfos2 && postInfos2.size() > 0) {// 兼容以前没有拼接领证方式到后台的处理
                                if (STATUS == 9) {
                                    chooseAddress.setChecked(true);
                                    chooseAddress.setVisibility(View.VISIBLE);
                                    address_rl.setVisibility(View.VISIBLE);
                                    setReceivePostInfo(postInfos2.get(0));
                                } else {
                                    ZXDY.setVisibility(View.GONE);
                                    WDLQ.setVisibility(View.GONE);
                                    chooseAddress.setChecked(true);
                                    chooseAddress.setVisibility(View.VISIBLE);
                                    setReceivePostInfo(postInfos2.get(0));
                                    address_rl.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (STATUS == 9) {
                                    address_rl.setVisibility(View.GONE);
                                    WDLQ.setChecked(true);
                                } else {
                                    chooseAddress.setVisibility(View.GONE);
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
    final Runnable GetBusiPostInfo  = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("TYPE", "1");// 速递类型（1递交纸质材料，2领取结果）
                param.put("BSNUM", BSNUM);
                String response = HTTP.excuteAndCache("getBusiPostInfo",
                                                      "RestEMSService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    postInfos = JSONUtil.getGson()
                                        .fromJson(new JSONObject(json.getString("ReturnValue")).getString(
                                                "Items"),
                                                  new TypeToken<List<PostInfo>>() {}.getType());
                    if (null != postInfos && postInfos.size() > 0) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                YJDJ.setChecked(true);
                                if (STATUS != -1 && STATUS != 9) {
                                    WDDJ.setVisibility(View.GONE);
                                    WXDJZZCL.setVisibility(View.GONE);
                                }
                                yjdjTip.setVisibility(View.VISIBLE);
                                wddjTip.setVisibility(View.GONE);
                                setSendPostInfo(postInfos.get(0));
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (blmsBean.getDJZZCL()
                                            .equals("3"))
                                {//无须递交纸质材料
                                    WXDJZZCL.setChecked(true);
                                    if (STATUS != -1 && STATUS != 9) {
                                        YJDJ.setVisibility(View.GONE);
                                        WDDJ.setVisibility(View.GONE);
                                    }
                                } else {
                                    yjdjTip.setVisibility(View.GONE);
                                    WDDJ.setChecked(true);
                                    if (STATUS != -1 && STATUS != 9) {
                                        YJDJ.setVisibility(View.GONE);
                                        WXDJZZCL.setVisibility(View.GONE);
                                    }
                                    //									wddjTip.setVisibility(View.VISIBLE);
                                }
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
     * 获取事项办理模式
     */
    final Runnable GetPermWsfwsd    = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                JSONObject param = new JSONObject();
                param.put("PERMID", PERMID);
                String response = HTTP.excuteAndCache("getPermWsfwsd",
                                                      "RestPermissionitemService",
                                                      param.toString(),
                                                      HistoreShare_v2.this);
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    blmsBean = JSONUtil.getGson()
                                       .fromJson(json.getString("ReturnValue"), BLMSBean.class);
                    //                    if (STATUS == -1 && TextUtils.isEmpty(YWGSDName)) {
                    //业务归属地
                    ywgsd = blmsBean.getYWGSD();
                    if (!TextUtils.isEmpty(ywgsd)) {
                        if (ywgsd.contains(",")) {
                            layers = ywgsd.split(",");

                        } else {
                            layers = new String[]{ywgsd};
                        }
                    }

                    //                    }
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
        if (!TextUtils.isEmpty(blmsBean.getLQSPJG())) {
            if (blmsBean.getLQSPJG()
                        .contains("1"))
            {// 领证方式：含网点领取
                WDLQ.setVisibility(View.VISIBLE);
                Log.e("GetNetwork", "controllViewGetBLMS1");
                //                new Thread(GetNetwork).start();
                dialog = Background.Process(HistoreShare_v2.this,
                                            GetNetwork,
                                            getString(MSFWResource.getResourseIdByName(
                                                    HistoreShare_v2.this,
                                                    "string",
                                                    "tj_loding")));

            } else {
                WDLQ.setVisibility(View.GONE);
            }
            if (blmsBean.getLQSPJG()
                        .contains("2"))
            {// 领证方式：含EMS领取
                chooseAddress.setVisibility(View.VISIBLE);
                if (STATUS == -1 || STATUS == 9 && flag == 0) {
                    new Thread(GetUserPostInfo).start();
                }
            } else {
                chooseAddress.setVisibility(View.GONE);
            }
            if (blmsBean.getLQSPJG()
                        .contains("3"))
            {// 领证方式：含自行打印
                ZXDY.setVisibility(View.VISIBLE);
            } else {
                ZXDY.setVisibility(View.GONE);
            }
            if (!blmsBean.getLQSPJG()
                         .contains("1") && blmsBean.getLQSPJG()
                                                   .contains("2"))
            {
                //领取方式不包含邮寄，包含了EMS
                chooseAddress.setChecked(true);
                wdlqParentRl.setVisibility(View.GONE);
                address_rl.setVisibility(View.VISIBLE);
                zxdyTipTv.setVisibility(View.GONE);
            } else if (!blmsBean.getLQSPJG()
                                .contains("1") && !blmsBean.getLQSPJG()
                                                           .contains("2") && blmsBean.getLQSPJG()
                                                                                     .contains("3"))
            {
                //领取方式不包含邮寄,EMS 含自行打印
                ZXDY.setChecked(true);
                wdlqParentRl.setVisibility(View.GONE);
                address_rl.setVisibility(View.GONE);
                //				zxdyTipTv.setVisibility(View.VISIBLE);
            }
        } else {
            LZFSparent_ll.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(blmsBean.getDJZZCL())) {
            if (blmsBean.getDJZZCL()
                        .contains("1"))
            {// 递交材料：网点递交
                WDDJ.setVisibility(View.VISIBLE);
                if (!blmsBean.getLQSPJG()
                             .contains("1"))
                {
                    Log.e("GetNetwork", "controllViewGetBLMS1");

                    //                    new Thread(GetNetwork).start();
                    dialog = Background.Process(HistoreShare_v2.this,
                                                GetNetwork,
                                                getString(MSFWResource.getResourseIdByName(
                                                        HistoreShare_v2.this,
                                                        "string",
                                                        "tj_loding")));

                }
            } else {
                WDDJ.setVisibility(View.GONE);
            }
            if (blmsBean.getDJZZCL()
                        .contains("2"))
            {// 递交材料：邮寄递交
                //如果领取为邮寄，则用户地址已获取了
                if (!blmsBean.getLQSPJG()
                             .contains("2"))
                {
                    if (STATUS == -1 || STATUS == 9 && flag == 0) {
                        new Thread(GetUserPostInfo).start();
                    }
                }
                YJDJ.setVisibility(View.VISIBLE);
            } else {
                YJDJ.setVisibility(View.GONE);
            }
            if (blmsBean.getDJZZCL()
                        .equals("3"))
            {//无需递交纸质材料 住建局  1,建设工程招标公告（投标邀请书）和招标组织形式登记
                //				2,建设工程招标文件登记
                //				3,建设工程招标投标情况报告登记   有配置
                WXDJZZCL.setVisibility(View.VISIBLE);
                WXDJZZCL.setChecked(true);
                wddjTip.setVisibility(View.GONE);
                yjdjTip.setVisibility(View.GONE);
                //				wxdjzzclTip.setVisibility(View.VISIBLE);
            } else {
                WXDJZZCL.setVisibility(View.GONE);
            }
        } else {// 递交材料：没有递交材料方式递交材料布局不显示
            DJCLparent_ll.setVisibility(View.GONE);
        }
        if (blmsBean.getBLMS() != null && !TextUtils.isEmpty(blmsBean.getBLMS())) {
            if (blmsBean.getBLMS()
                        .equals("1"))
            {
                blmsTitle.setText("办理模式:此事项零次到现场全流程网上办理");
            } else if (blmsBean.getBLMS()
                               .equals("2"))
            {
                blmsTitle.setText("办理模式:此事项线上申请、线上提交(受理环节递交纸质申请材料)");
            } else if (blmsBean.getBLMS()
                               .equals("3"))
            {
                blmsTitle.setText("办理模式:此事项线上申请、线上提交(领证环节递交纸质申请材料)");
            } else if (blmsBean.getBLMS()
                               .equals("4"))
            {
                blmsTitle.setText("办理模式:此事项线上申请、线上提交(现场审查环节递交纸质申请材料)");
            } else if (blmsBean.getBLMS()
                               .equals("5"))
            {
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

                String response = HTTP.excute("getUserPostInfo",
                                              "RestEMSService",
                                              param.toString());
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    final List<PostInfo> postInfos = JSONUtil.getGson()
                                                             .fromJson(new JSONObject(json.getString(
                                                                     "ReturnValue")).getString(
                                                                     "Items"),
                                                                       new TypeToken<List<PostInfo>>() {}.getType());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (postInfos != null && postInfos.size() > 0) {
                                if (blmsBean.getLQSPJG()
                                            .contains("2") && (STATUS == -1))
                                {
                                    setReceivePostInfo(postInfos.get(0));
                                }
                                if (blmsBean.getDJZZCL()
                                            .contains("2") && (STATUS == -1))
                                {
                                    setSendPostInfo(postInfos.get(0));
                                }
                            } else {
                                if (Constants.getPostInfo == null) {
                                    addressDetail.setVisibility(View.GONE);
                                    addressEmpty.setVisibility(View.VISIBLE);
                                }
                                if (Constants.sendPostInfo == null) {
                                    rl_myaddresss_detail.setVisibility(View.GONE);
                                    tv_temp_myaddress.setVisibility(View.VISIBLE);
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

    /**
     * 获取网点地址   默认第一条数据（后台数据库做过处理，只有一条数据）
     */
    final Runnable GetNetwork = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("PERMID", PERMID);
                param.put("AREAID", YWGSDID);
                if (TextUtils.isEmpty(YWGSDID)) {
                    return;
                }
                Log.e("GetNetwork", "111");

                String response = HTTP.excute("getNetworkByPermid",
                                              "RestNetworkService",
                                              param.toString());
                Log.e("GetNetwork", "ywgsd=" + YWGSDID);

                Log.e("GetNetwork", "response=" + response);

                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                Log.e("GetNetwork", "code" + code);

                if (code.equals("200")) {
                    networks = JSONUtil.getGson()
                                       .fromJson(new JSONObject(json.getString("ReturnValue")).getString(
                                               "Items"),
                                                 new TypeToken<List<NetWorkBean>>() {}.getType());
                    Log.e("GetNetwork", networks.toString());

                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (networks != null && networks.size() > 0 && !TextUtils.isEmpty(
                                    networks.get(0)
                                            .getNETWORKADDRESS()) && !TextUtils.isEmpty(networks.get(
                                    0)
                                                                                                .getNETWORKNAME()))
                            {

                                //                                djAddr.setVisibility(View.VISIBLE);
                                //                                lqAddr.setVisibility(View.VISIBLE);
                                if (!TextUtils.equals("6",ywgsd)) {
                                    tvYwgsd.setText(YWGSDName);//beizhu

                                }
                                djAddr.setText(networks.get(0)
                                                       .getNETWORKNAME() + ": " + networks.get(0)
                                                                                          .getNETWORKADDRESS());
                                lqAddr.setText(networks.get(0)
                                                       .getNETWORKNAME() + ": " + networks.get(0)
                                                                                          .getNETWORKADDRESS());
                                Log.e("GetNetwork",
                                      networks.get(0)
                                              .getNETWORKNAME() + ": " + networks.get(0)
                                                                                 .getNETWORKADDRESS());
                                //                                lqAddr.setText("说明：请申请人于业务办结登陆PC网厅“我的办事”点击“我的证照”栏目下载电子证照并打印\n");
                            } else {
                                addressDetail.setVisibility(View.GONE);
                                addressEmpty.setVisibility(View.VISIBLE);
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
    /**
     * 获取业务归属地
     */
    final Runnable getYWGSD   = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("PERMID", PERMID);
                if (STATUS != -1) {
                    param.put("BSNUM", BSNUM);
                }
                String response = HTTP.excute("getRegionByPermidBsnum",
                                              "RestRegionService",
                                              param.toString());
                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    YWGSDBean YWGSD = JSONUtil.getGson()
                                              .fromJson(json.getString("ReturnValue"),
                                                        YWGSDBean.class);
                    parentItems = YWGSD.getPARENTS();
                    childItems = YWGSD.getCHILDREN();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (childItems != null && childItems.size() != 0) {
                                YWGSDID = childItems.get(0)
                                                    .getAREAID();
                                YWGSDName = childItems.get(0)
                                                      .getAREANAME();
                            } else {
                                if (!TextUtils.equals(ywgsd, "6")) {
                                    YWGSDID = parentItems.get(0)
                                            .getAREAID();
                                    YWGSDName = parentItems.get(0)
                                            .getAREANAME();
                                }

                            }
                            //                            YWGSDID = permission.getREGIONID();
                            //
                            //                  YWGSDName = permission.getREGIONNAME();
                            if (!TextUtils.equals(ywgsd, "6")) {
                                tvYwgsd.setText(YWGSDName);

                            }
                            if (TextUtils.isEmpty(YWGSDID)) {
                                if (STATUS == 9 || STATUS == -1) {
                                    level = 0;
                                    isZanCunEmpty = true;
                                    dialog = Background.Process(HistoreShare_v2.this,
                                                                getYWGSDV2,
                                                                getString(MSFWResource.getResourseIdByName(
                                                                        HistoreShare_v2.this,
                                                                        "string",
                                                                        "tj_loding")));
                                }
                            } else {
                                dialog = Background.Process(HistoreShare_v2.this,
                                                            GetNetwork,
                                                            getString(MSFWResource.getResourseIdByName(
                                                                    HistoreShare_v2.this,
                                                                    "string",
                                                                    "tj_loding")));
                            }
                            //                            if (parentItems != null && parentItems.size() > 0) {
                            //                                YWGSDID = parentItems.get(0).getAREAID();
                            //                                YWGSDName = parentItems.get(0).getAREANAME();
                            //                                for (int i = 0; i < parentItems.size(); i++) {
                            //                                    parentNames.add(parentItems.get(i).getAREANAME());
                            //                                }
                            //                                parentAdapter.notifyDataSetChanged();
                        }/* else {
//                                ywgsdParent.setVisibility(View.GONE);
                            }
                            if (childItems != null && childItems.size() > 0 && !parentItems.get(0).getAREAID().equals(childItems.get(0).getAREAID())) {//如果业务归属地的子和父不一样，一样的话表示是同一个
                                for (int i = 0; i < childItems.size(); i++) {
                                    childNames.add(childItems.get(i).getAREANAME());
                                }
                                childAdapter.notifyDataSetChanged();
                            } else {
                                childSpinner.setVisibility(View.INVISIBLE);
                            }*/

                    });

                } else {
                    //					DialogUtil.showUIToast(HistoreShare_v2.this, "网络环境不稳定！");
                }

            } catch (Exception e)

            {
                //				DialogUtil.showUIToast(HistoreShare_v2.this, "网络环境不稳定");
                e.printStackTrace();
            }
        }
    };
    final Runnable getYWGSDV2 = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                //                param.put("LAYER", (Integer.parseInt(layers[level]) - 3) + "");
                //                if (STATUS != 9) {
                //                if (level == 0) {
                //
                //                    if (TextUtils.equals("4", layers[0])) {
                //
                //                        param.put("PARENTID", "");
                //                    } else if (TextUtils.equals("5", layers[level])) {
                //                        param.put("PARENTID", "440306");
                //                    } else if (layers.length == 1 && TextUtils.equals(layers[0], "6") && permission != null && permission.getREGIONID() != null) {
                //                        param.put("PARENTID", permission.getREGIONID());
                //
                //                    }
                //                } else {
                //                    param.put("PARENTID", parentId);
                //
                //                }
                param.put("PERMID", permission.getID());

                //                }
                String response = HTTP.excute("getRegionlistByPemid",
                                              "RestRegionService",
                                              param.toString());
                Log.e("GetNetwork", "responsegetregionlistByLayer2=" + response);

                JSONObject json = new JSONObject(response);
                String     code = json.getString("code");
                if (code.equals("200")) {
                    YWGSDV2Bean YWGSD = JSONUtil.getGson()
                                                .fromJson(json.getString("ReturnValue"),
                                                          YWGSDV2Bean.class);
                    parentItems = YWGSD.getItems();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (parentItems != null && parentItems.size() > 0) {
                                if (flag == 0) {
                                    if (!TextUtils.equals("6", ywgsd)) {
                                        YWGSDID = permission.getREGIONID();
                                        YWGSDName = permission.getREGIONNAME();//beizhu
                                    }

                                }
                                if (isZanCunEmpty) {
                                    if (!TextUtils.equals(ywgsd, "6")) {
                                        YWGSDID = permission.getREGIONID();
                                        YWGSDName = permission.getREGIONNAME();
                                        tvYwgsd.setText(YWGSDName);
                                    }

                                    dialog = Background.Process(HistoreShare_v2.this,
                                                                GetNetwork,
                                                                getString(MSFWResource.getResourseIdByName(
                                                                        HistoreShare_v2.this,
                                                                        "string",
                                                                        "tj_loding")));
                                    isZanCunEmpty = false;
                                    return;
                                }
                                parentNames.clear();

                                //                                //把上面选择的放在第一条
                                //                                if (foreRegion != null) {
                                //                                    parentItems.add(0, foreRegion);
                                //                                }

                                for (int i = 0; i < parentItems.size(); i++) {
                                    parentNames.add(parentItems.get(i)
                                                               .getAREANAME());
                                }
                                //                                parentAdapter.notifyDataSetChanged();
                                if (flag != 0) {
                                    selectNameDialog();
                                } else {
                                    flag++;
                                    if (!TextUtils.equals("6", ywgsd)) {
                                        tvYwgsd.setText(YWGSDName);//beizhu
                                    }
                                    Log.e("GetNetwork", "init");
                                    dialog = Background.Process(HistoreShare_v2.this,
                                                                GetNetwork,
                                                                getString(MSFWResource.getResourseIdByName(
                                                                        HistoreShare_v2.this,
                                                                        "string",
                                                                        "tj_loding")));

                                }
                            } else {
                                //                                ywgsdParent.setVisibility(View.GONE);
                            }
                            //
                        }
                    });

                } else {
                    //					DialogUtil.showUIToast(HistoreShare_v2.this, "网络环境不稳定！");
                }
                //                level++;
            } catch (Exception e) {
                //				DialogUtil.showUIToast(HistoreShare_v2.this, "网络环境不稳定");
                e.printStackTrace();
            }
        }
    };

    private void uploadFile(final ApplyBean applyBean, final int position) {

        new AlertDialog.Builder(HistoreShare_v2.this).setTitle("请选择上传方式")
                                                     .setItems(new String[]{"拍照上传",
                                                                            "选择图片",
                                                                            "选择文件"},
                                                               new DialogInterface.OnClickListener() {

                                                                   @Override
                                                                   public void onClick(
                                                                           DialogInterface dialog,
                                                                           int index)
                                                                   {
                                                                       switch (index) {
                                                                           case 0:// 拍照上传
                                                                               if (Environment.getExternalStorageState()
                                                                                              .equals(Environment.MEDIA_MOUNTED))
                                                                               {
                                                                                   //								Intent intent = new Intent();
                                                                                   //								intent.setClass(HistoreShare_v2.this, TakePhotos.class);
                                                                                   //								intent.putExtra("PERMID", PERMID);
                                                                                   //								intent.putExtra("CLMC", applyBean.getCLMC());
                                                                                   //								intent.putExtra("position", position);
                                                                                   //								intent.putExtra("TYPE", 2 + "");
                                                                                   //								intent.putExtra("FILE", applyBean);
                                                                                   //								intent.putExtra("idString", "");
                                                                                   //								intent.putExtra("startTimeString", "");
                                                                                   //								intent.putExtra("endTimeString", "");
                                                                                   //
                                                                                   //								startActivityForResult(intent, FILE_REQUEST);
                                                                                   Intent intent = new Intent();
                                                                                   intent.putExtra(
                                                                                           "applyBean",
                                                                                           applyBean);
                                                                                   intent.putExtra(
                                                                                           "PERMID",
                                                                                           PERMID);
                                                                                   intent.putExtra(
                                                                                           "position",
                                                                                           position);
                                                                                   intent.putExtra(
                                                                                           "TYPE",
                                                                                           2);
                                                                                   intent.putExtra(
                                                                                           "from",
                                                                                           "HistoreShare_v2");
                                                                                   intent.putExtra(
                                                                                           "STATUS",
                                                                                           STATUS);
                                                                                   intent.putExtra(
                                                                                           "flag",
                                                                                           1);
                                                                                   intent.putExtra(
                                                                                           "mark",
                                                                                           mark);
                                                                                   intent.putExtra(
                                                                                           "uploadType",
                                                                                           "camera");
                                                                                   intent.setClass(
                                                                                           HistoreShare_v2.this,
                                                                                           MaterialManageActivity.class);
                                                                                   startActivityForResult(
                                                                                           intent,
                                                                                           FILE_REQUEST);
                                                                               } else {
                                                                                   DialogUtil.showUIToast(
                                                                                           HistoreShare_v2.this,
                                                                                           getString(
                                                                                                   MSFWResource.getResourseIdByName(
                                                                                                           HistoreShare_v2.this,
                                                                                                           "string",
                                                                                                           "tj_sdcard_unmonted_hint")));
                                                                               }

                                                                               break;
                                                                           case 1:// 选择图片上传

                                                                               if (Environment.getExternalStorageState()
                                                                                              .equals(Environment.MEDIA_MOUNTED))
                                                                               {
                                                                                   //								Intent fileChooserIntent = new Intent();
                                                                                   //								fileChooserIntent = new Intent(HistoreShare_v2.this, FileChooserActivity.class);
                                                                                   //								fileChooserIntent.putExtra("PERMID", PERMID);
                                                                                   //								fileChooserIntent.putExtra("CLMC", applyBean.getCLMC());
                                                                                   //								fileChooserIntent.putExtra("position", position);
                                                                                   //								fileChooserIntent.putExtra("TYPE", 2 + "");
                                                                                   //								fileChooserIntent.putExtra("FILE", applyBean);
                                                                                   //								fileChooserIntent.putExtra("idString", "");
                                                                                   //								fileChooserIntent.putExtra("startTimeString", "");
                                                                                   //								fileChooserIntent.putExtra("endTimeString", "");
                                                                                   //
                                                                                   //								startActivityForResult(fileChooserIntent, FILE_REQUEST);
                                                                                   Intent intent = new Intent();
                                                                                   intent.putExtra(
                                                                                           "applyBean",
                                                                                           applyBean);
                                                                                   intent.putExtra(
                                                                                           "PERMID",
                                                                                           PERMID);
                                                                                   intent.putExtra(
                                                                                           "position",
                                                                                           position);
                                                                                   intent.putExtra(
                                                                                           "TYPE",
                                                                                           2);
                                                                                   intent.putExtra(
                                                                                           "from",
                                                                                           "HistoreShare_v2");
                                                                                   intent.putExtra(
                                                                                           "STATUS",
                                                                                           STATUS);
                                                                                   intent.putExtra(
                                                                                           "flag",
                                                                                           1);
                                                                                   intent.putExtra(
                                                                                           "mark",
                                                                                           mark);
                                                                                   intent.putExtra(
                                                                                           "uploadType",
                                                                                           "choosephoto");
                                                                                   intent.setClass(
                                                                                           HistoreShare_v2.this,
                                                                                           MaterialManageActivity.class);
                                                                                   startActivityForResult(
                                                                                           intent,
                                                                                           FILE_REQUEST);
                                                                               } else {
                                                                                   DialogUtil.showUIToast(
                                                                                           HistoreShare_v2.this,
                                                                                           getString(
                                                                                                   MSFWResource.getResourseIdByName(
                                                                                                           HistoreShare_v2.this,
                                                                                                           "string",
                                                                                                           "tj_sdcard_unmonted_hint")));
                                                                               }

                                                                               break;
                                                                           case 2:// 选择文件上传

                                                                               if (Environment.getExternalStorageState()
                                                                                              .equals(Environment.MEDIA_MOUNTED))
                                                                               {
                                                                                   //								Intent fileChooserIntent = new Intent();
                                                                                   //								fileChooserIntent = new Intent(HistoreShare_v2.this, FileChooserActivity.class);
                                                                                   //								fileChooserIntent.putExtra("PERMID", PERMID);
                                                                                   //								fileChooserIntent.putExtra("CLMC", applyBean.getCLMC());
                                                                                   //								fileChooserIntent.putExtra("position", position);
                                                                                   //								fileChooserIntent.putExtra("TYPE", 2 + "");
                                                                                   //								fileChooserIntent.putExtra("FILE", applyBean);
                                                                                   //								fileChooserIntent.putExtra("idString", "");
                                                                                   //								fileChooserIntent.putExtra("startTimeString", "");
                                                                                   //								fileChooserIntent.putExtra("endTimeString", "");
                                                                                   //
                                                                                   //								startActivityForResult(fileChooserIntent, FILE_REQUEST);
                                                                                   Intent intent = new Intent();
                                                                                   intent.putExtra(
                                                                                           "applyBean",
                                                                                           applyBean);
                                                                                   intent.putExtra(
                                                                                           "PERMID",
                                                                                           PERMID);
                                                                                   intent.putExtra(
                                                                                           "position",
                                                                                           position);
                                                                                   intent.putExtra(
                                                                                           "TYPE",
                                                                                           2);
                                                                                   intent.putExtra(
                                                                                           "from",
                                                                                           "HistoreShare_v2");
                                                                                   intent.putExtra(
                                                                                           "STATUS",
                                                                                           STATUS);
                                                                                   intent.putExtra(
                                                                                           "flag",
                                                                                           1);
                                                                                   intent.putExtra(
                                                                                           "mark",
                                                                                           mark);
                                                                                   intent.putExtra(
                                                                                           "uploadType",
                                                                                           "choosefile");
                                                                                   intent.setClass(
                                                                                           HistoreShare_v2.this,
                                                                                           MaterialManageActivity.class);
                                                                                   startActivityForResult(
                                                                                           intent,
                                                                                           FILE_REQUEST);
                                                                               } else {
                                                                                   DialogUtil.showUIToast(
                                                                                           HistoreShare_v2.this,
                                                                                           getString(
                                                                                                   MSFWResource.getResourseIdByName(
                                                                                                           HistoreShare_v2.this,
                                                                                                           "string",
                                                                                                           "tj_sdcard_unmonted_hint")));
                                                                               }

                                                                               break;

                                                                           default:
                                                                               break;
                                                                       }

                                                                   }
                                                               })
                                                     .setNegativeButton("取消", null)
                                                     .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyBeanAdapterv2.notifyDataSetChanged();
    }

    public class ApplyBeanAdapter
            extends BaseAdapter
    {
        private Context         mContext;
        private List<ApplyBean> data;
        private List<ATTBean>   atts;// 文件集合
        private DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                                                                               .cacheInMemory(true)
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
            convertView = LayoutInflater.from(mContext)
                                        .inflate(MSFWResource.getResourseIdByName(mContext,
                                                                                  "layout",
                                                                                  "item_applybean_list"),
                                                 parent,
                                                 false);
            hodler.nameTitle = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(
                    mContext,
                    "id",
                    "nameTitle"));
            hodler.imags_parent = (LinearLayout) convertView.findViewById(MSFWResource.getResourseIdByName(
                    mContext,
                    "id",
                    "imags_parent_ll"));
            hodler.status = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(
                    mContext,
                    "id",
                    "status"));
            hodler.add_img = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(
                    mContext,
                    "id",
                    "addImage"));
            hodler.SFBY = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(
                    mContext,
                    "id",
                    "SFBY"));
            hodler.moreTip_img = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(
                    mContext,
                    "id",
                    "more_title_iv"));
            hodler.SHYJ = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(
                    mContext,
                    "id",
                    "SHYJ"));
            // convertView.setTag(hodler);

            // }else {
            // hodler = (ApplyBeanHodler) convertView.getTag();
            //
            // }
            final ApplyBean applyBean = data.get(position);

            hodler.nameTitle.setText(applyBean.getCLMC());
            // && (null == applyBean.getFILEID() ||
            // applyBean.getFILEID().equals(""))// 必要提交除窗口递交外
            if (applyBean.getSFBY()
                         .equals("1"))
            {
                hodler.SFBY.setVisibility(View.VISIBLE);
            } else {
                hodler.SFBY.setVisibility(View.GONE);
            }
            if (applyBean.getSTATUS()
                         .equals("1"))
            {
                hodler.status.setBackgroundResource(MSFWResource.getResourseIdByName(mContext,
                                                                                     "drawable",
                                                                                     "tj_right_yes"));
            } else {
                hodler.status.setBackgroundResource(MSFWResource.getResourseIdByName(mContext,
                                                                                     "drawable",
                                                                                     "no_submit"));
            }
            hodler.moreTip_img.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (applyBean != null && !TextUtils.isEmpty(applyBean.getYQ())) {
                        showTitleTipDialog(applyBean.getCLMC() + "\n要求：" + applyBean.getYQ());
                    } else {
                        showTitleTipDialog(applyBean.getCLMC());
                    }
                }
            });

            // 审核意见
            if (null != data && applyBean != null && applyBean.getSH() != null && applyBean.getSH()
                                                                                           .equals("0"))
            {
                hodler.SHYJ.setVisibility(View.VISIBLE);
                hodler.SHYJ.setText(applyBean.getSHYJ());
            } else {
                hodler.SHYJ.setVisibility(View.GONE);
            }
            Log.e("Histrore", "SHYJ===" + applyBean.getSHYJ() + "   SH===" + applyBean.getSH());
            atts = Constants.material.get(applyBean.getCLBH());

            if (atts != null && atts.size() > 0) {
                for (int i = 0; i < (atts.size() > 6
                                     ? 6
                                     : atts.size()); i++) {
                    ImageView image = new ImageView(mContext);
                    LayoutParams lp = new LayoutParams(DensityUtil.dip2px(HistoreShare_v2.this, 40),
                                                       DensityUtil.dip2px(HistoreShare_v2.this,
                                                                          40));
                    lp.width = DensityUtil.dip2px(HistoreShare_v2.this, 40 - 10);
                    lp.height = DensityUtil.dip2px(HistoreShare_v2.this, 40 - 10);
                    lp.setMargins(DensityUtil.dip2px(HistoreShare_v2.this, 5),
                                  DensityUtil.dip2px(HistoreShare_v2.this, 3),
                                  DensityUtil.dip2px(HistoreShare_v2.this, 5),
                                  DensityUtil.dip2px(HistoreShare_v2.this, 3));
                    image.setLayoutParams(lp);
                    image.setScaleType(ScaleType.FIT_XY);
                    final String end = atts.get(i)
                                           .getATTACHNAME()
                                           .substring(atts.get(i)
                                                          .getATTACHNAME()
                                                          .lastIndexOf(".") + 1,
                                                      atts.get(i)
                                                          .getATTACHNAME()
                                                          .length())
                                           .toLowerCase(Locale.US);

                    if ("jpg".equals(end) || "png".equals(end) || "gif".equals(end) || "jpeg".equals(
                            end) || "bmp".equals(end))
                    {
                        //						ImageLoader.getInstance().displayImage(
                        //								Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + atts.get(i).getID(), image,
                        //								options);
                        ImageLoadUtils2.loadImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + atts.get(
                                i)
                                                                                                                 .getID(),
                                                  image);
                    } else if ("doc".equals(end) || "docx".equals(end) || "docm".equals(end) || "dotx".equals(
                            end) || "dotx".equals(end) || "dotm".equals(end))
                    {

                        image.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                "drawable",
                                                                                "tj_ic_word")); // word文档文件
                    } else if ("pdf".equals(end)) {
                        image.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                "drawable",
                                                                                "tj_ic_pdf")); // pdf文件
                    } else if ("xls".equals(end) || "xlsx".equals(end) || "xlsm".equals(end) || "xltx".equals(
                            end) || "xltm".equals(end) || "xlsb".equals(end) || "xlam".equals(end))
                    {
                        image.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                "drawable",
                                                                                "tj_ic_excel")); // excel文件
                    } else {
                        image.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                "drawable",
                                                                                "tj_ic_file_unknown"));
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
                    gotoMaterialManage(applyBean, position);
                }
            });
            return convertView;
        }

        class ApplyBeanHodler {
            private TextView     nameTitle;
            private LinearLayout imags_parent;
            private ImageView    status;
            private ImageView    add_img;
            private TextView     SFBY;
            private ImageView    moreTip_img;
            private TextView     SHYJ;
        }

    }

    public class ApplyBeanAdapterv2
            extends BaseAdapter
    {
        private Context         mContext;
        private List<ApplyBean> data;
        private List<ATTBean>   atts;// 文件集合
        private DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                                                                               .cacheInMemory(true)
                                                                               .build();

        public ApplyBeanAdapterv2(Context context, List<ApplyBean> data) {
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
            convertView = LayoutInflater.from(mContext)
                                        .inflate(R.layout.item_applybean_listv2, parent, false);

            hodler.ivUpload = (RoundAngleImageView) convertView.findViewById(R.id.iv_upload);
            hodler.tvUploadCount = (TextView) convertView.findViewById(R.id.tv_upload_count);
            hodler.tvApplyTitle = (TextView) convertView.findViewById(R.id.tv_apply_title);
            hodler.tvDzhyq = (TextView) convertView.findViewById(R.id.tv_dzhyq);
            hodler.tvApplyYaoqiu = (TextView) convertView.findViewById(R.id.tv_apply_yaoqiu);
            hodler.tvKongbiao = (RelativeLayout) convertView.findViewById(R.id.rl_kongbiao);
            hodler.tvYangbiao = (RelativeLayout) convertView.findViewById(R.id.rl_yangbiao);
            hodler.rlShare = (RelativeLayout) convertView.findViewById(R.id.rl_share);
            //            hodler.ivNext = (ImageView) convertView
            //                    .findViewById(R.id.iv_next);
            hodler.ivStatus = (ImageView) convertView.findViewById(R.id.iv_status);
            final ApplyBean applyBean = data.get(position);

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoMaterialManage(applyBean, position);
                }
            });
            if (TextUtils.isEmpty(applyBean.getFILE_URL())) {
                hodler.tvKongbiao.setVisibility(View.GONE);
            } else {
                hodler.tvKongbiao.setVisibility(View.VISIBLE);

            }
            if (TextUtils.isEmpty(applyBean.getSAMPLE_FILE())) {
                hodler.tvYangbiao.setVisibility(View.GONE);
            } else {
                hodler.tvYangbiao.setVisibility(View.VISIBLE);

            }

            hodler.tvKongbiao.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri    uri    = Uri.parse("http://sxsl.sz.gov.cn/qzapi/qzqd/attach/upload/" + applyBean.getFILE_URL());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            hodler.tvYangbiao.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri    uri    = Uri.parse("http://sxsl.sz.gov.cn/qzapi/qzqd/attach/upload/" + applyBean.getSAMPLE_FILE());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });


            hodler.tvApplyTitle.
                                       setText(applyBean.getCLMC());
            hodler.tvApplyYaoqiu.setText("材料要求：" + applyBean.getYQ());
            if (null == applyBean.getYQ()) {
                hodler.tvApplyYaoqiu.setVisibility(View.INVISIBLE);
            }
            if (applyBean.getDZHYQ()
                         .contains("4"))
            {
                hodler.tvDzhyq.setVisibility(View.VISIBLE);
                hodler.tvDzhyq.setText("证");
            } else if (applyBean.getDZHYQ()
                                .contains("5"))
            {
                hodler.tvDzhyq.setVisibility(View.VISIBLE);
                hodler.tvDzhyq.setText("纸");
            } else if (applyBean.getDZHYQ()
                                .contains("3"))
            {
                hodler.tvDzhyq.setVisibility(View.VISIBLE);
                hodler.tvDzhyq.setText("电");
            }
            hodler.rlShare.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    // 比如发送文本形式的数据内容

                    // 指定发送的内容

                    String fileurl   = "";
                    String sampleurl = "";
                    if (!TextUtils.isEmpty(applyBean.getFILE_URL())) {
                        fileurl = applyBean.getCLMC() + "空表：http://sxsl.sz.gov.cn/qzapi/qzqd/attach/upload/" + applyBean.getFILE_URL();
                    }
                    if (!TextUtils.isEmpty(applyBean.getSAMPLE_FILE())) {
                        sampleurl = applyBean.getCLMC() + "样表：http://sxsl.sz.gov.cn/qzapi/qzqd/attach/upload/" + applyBean.getSAMPLE_FILE();
                    }

                    sendIntent.putExtra(Intent.EXTRA_TEXT, fileurl + "\n" + sampleurl + "，来源：宝安通。");
                    // 指定发送内容的类型
                    sendIntent.setType("text/plain");
                    // 比如发送二进制文件数据流内容（比如图片、视频、音频文件等等）
                    // 指定发送的内容 (EXTRA_STREAM 对于文件 Uri )
                    // 指定发送内容的类型 (MIME type)

                    startActivity(Intent.createChooser(sendIntent, ""));


                }
            });
            if (applyBean.getSTATUS()
                         .equals("1"))
            {
                hodler.ivStatus.setBackgroundResource(R.mipmap.taiji_upload_succss);
            } else {
                hodler.ivStatus.setBackgroundResource(R.mipmap.taiji_upload_none);
            }
            final ApplyBeanHodler finalHodler = hodler;

            //判断是否超过三行
            final ApplyBeanHodler finalHodler1 = hodler;
            hodler.tvApplyYaoqiu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHodler1.tvApplyYaoqiu.post(new Runnable() {
                        @Override
                        public void run() {
                            Layout l = finalHodler.tvApplyYaoqiu.getLayout();
                            if (l != null) {
                                int lines = l.getLineCount();
                                if (lines > 0) {
                                    if (l.getEllipsisCount(lines - 1) > 0) {

                                        AlertDialog.Builder builder = new Builder(HistoreShare_v2.this);

                                        builder.setTitle(applyBean.getCLMC());
                                        builder.setMessage(applyBean.getYQ());
                                        builder.setPositiveButton("确定",
                                                                  new android.content.DialogInterface.OnClickListener() {

                                                                      @Override
                                                                      public void onClick(
                                                                              DialogInterface dialog,
                                                                              int which)
                                                                      {
                                                                          // TODO Auto-generated method stub
                                                                          dialog.dismiss();
                                                                      }
                                                                  });
                                        builder.create();
                                        builder.show();


                                    } else {
                                        gotoMaterialManage(applyBean, position);
                                    }
                                } else {
                                    gotoMaterialManage(applyBean, position);
                                }
                            } else {
                                gotoMaterialManage(applyBean, position);
                            }
                        }
                    });
                }
            });


            atts = Constants.material.get(applyBean.getCLBH());

            if (atts != null && atts.size() > 0) {
                hodler.tvUploadCount.setText(atts.size() + "");
                final String end = atts.get(atts.size() - 1)
                                       .getATTACHNAME()
                                       .substring(atts.get(atts.size() - 1)
                                                      .getATTACHNAME()
                                                      .lastIndexOf(".") + 1,
                                                  atts.get(atts.size() - 1)
                                                      .getATTACHNAME()
                                                      .length())
                                       .toLowerCase(Locale.US);

                if ("jpg".equals(end) || "png".equals(end) || "gif".equals(end) || "jpeg".equals(end) || "bmp".equals(
                        end))
                {
                    //						ImageLoader.getInstance().displayImage(
                    //								Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + atts.get(i).getID(), image,
                    //								options);
                    ImageLoadUtils2.loadImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + atts.get(
                            atts.size() - 1)
                                                                                                             .getID(),
                                              hodler.ivUpload);
                } else if ("doc".equals(end) || "docx".equals(end) || "docm".equals(end) || "dotx".equals(
                        end) || "dotx".equals(end) || "dotm".equals(end))
                {

                    hodler.ivUpload.setImageResource(MSFWResource.getResourseIdByName(
                            HistoreShare_v2.this,
                            "drawable",
                            "tj_ic_word")); // word文档文件
                } else if ("pdf".equals(end)) {
                    hodler.ivUpload.setImageResource(MSFWResource.getResourseIdByName(
                            HistoreShare_v2.this,
                            "drawable",
                            "tj_ic_pdf")); // pdf文件
                } else if ("xls".equals(end) || "xlsx".equals(end) || "xlsm".equals(end) || "xltx".equals(
                        end) || "xltm".equals(end) || "xlsb".equals(end) || "xlam".equals(end))
                {
                    hodler.ivUpload.setImageResource(MSFWResource.getResourseIdByName(
                            HistoreShare_v2.this,
                            "drawable",
                            "tj_ic_excel")); // excel文件
                } else {
                    hodler.ivUpload.setImageResource(MSFWResource.getResourseIdByName(
                            HistoreShare_v2.this,
                            "drawable",
                            "tj_ic_file_unknown"));
                }


            } else {
                hodler.tvUploadCount.setText("0");
            }

            return convertView;
        }

        class ApplyBeanHodler {

            private RoundAngleImageView ivUpload;
            private TextView            tvUploadCount;
            private TextView            tvApplyTitle;
            private TextView            tvDzhyq;
            private TextView            tvApplyYaoqiu;
            private RelativeLayout      tvKongbiao;
            private RelativeLayout      tvYangbiao;
            private RelativeLayout      rlShare;
            private ImageView           ivNext;
            private ImageView           ivStatus;

        }

    }

    private void gotoMaterialManage(ApplyBean applyBean, int position) {
        Intent intent = new Intent();
        intent.putExtra("applyBean", applyBean);
        intent.putExtra("PERMID", PERMID);
        intent.putExtra("position", position);
        intent.putExtra("TYPE", 2);
        intent.putExtra("from", "HistoreShare_v2");
        intent.putExtra("STATUS", STATUS);
        intent.putExtra("flag", 1);
        intent.putExtra("mark", mark);
        intent.setClass(HistoreShare_v2.this, MaterialManageActivity.class);
        startActivityForResult(intent, FILE_REQUEST);
    }

    private void showTitleTipDialog(String title) {
        AlertDialog.Builder builder = new Builder(HistoreShare_v2.this);
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

    //设置邮寄信息
    private void setReceivePostInfo(PostInfo postInfo) {
        if (postInfo == null) {
            return;
        }
        Constants.getPostInfo = postInfo;
        addressDetail.setVisibility(View.VISIBLE);
        addressEmpty.setVisibility(View.GONE);
        RECEIVE.setText(postInfo.getRECEIVE());
        //		ADDRESS.setText(checkText(postInfo.getPROVINCE()) +
        //				checkText(postInfo.getCITY())+
        //				checkText(postInfo.getCOUNTRY())+
        //				postInfo.getADDRESS());
        ADDRESS.setText(checkText(postInfo.getADDRESS()));
        POSTINFO = postInfo;
        PHONE.setText(postInfo.getPHONE());
    }

    //设置寄件人信息
    private void setSendPostInfo(PostInfo postInfo) {
        if (postInfo == null) {
            return;
        }
        Constants.sendPostInfo = postInfo;
        rl_myaddresss_detail.setVisibility(View.VISIBLE);
        tv_temp_myaddress.setVisibility(View.GONE);
        SEND.setText(postInfo.getRECEIVE());
        ADDRESS_SEND.setText(checkText(postInfo.getPROVINCE()) + checkText(postInfo.getCITY()) + checkText(
                postInfo.getCOUNTRY()) + postInfo.getADDRESS());
        SENDINFO = postInfo;
        PHONE_SEND.setText(postInfo.getPHONE());
    }

    private boolean checkLzfsStaus() {
        boolean lqStatus = false, djStatus = false;
        if ((chooseAddress.isChecked() && !TextUtils.isEmpty(ADDRESS.getText()
                                                                    .toString()
                                                                    .trim()) && Constants.getPostInfo != null) || !chooseAddress.isChecked())
        {
            lqStatus = true;
        } else {
            lqStatus = false;
        }
        if ((YJDJ.isChecked() && !TextUtils.isEmpty(ADDRESS_SEND.getText()
                                                                .toString()
                                                                .trim()) && Constants.sendPostInfo != null) || !YJDJ.isChecked())
        {
            djStatus = true;
        } else {
            djStatus = false;
        }
        if (lqStatus && djStatus) {
            finish_lzfs.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_yes"));
        } else {
            finish_lzfs.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_no"));
        }
        if (TextUtils.isEmpty(ywgsd)) {
            return true;
        }


        return lqStatus && djStatus&&!TextUtils.isEmpty(YWGSDName);
    }

    private void checkApplyBtn() {
        if (applySubmit != null) {
            if (isFinishFile && isFinishForm && checkLzfsStaus()) {
                applySubmit.setBackgroundResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                   "drawable",
                                                                                   "tj_blue_btn_shape"));
            } else {
                applySubmit.setBackgroundResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                   "drawable",
                                                                                   "tj_gray_btn_shape"));
            }
        }
    }

    private String checkText(String text) {
        return TextUtils.isEmpty(text)
               ? ""
               : text;
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void getMsgEvent(String s) {
    //
    //    }

    /*
    * 更新顶部图标状态
    * */
    private void updateTittleStatus() {
        if (checkUserInfo()) {


            if (rbQybs.isChecked()) {
                if (TextUtils.isEmpty(selectedCompanyId)) {
                    finish_userinfo.setImageResource(MSFWResource.getResourseIdByName(
                            HistoreShare_v2.this,
                            "drawable",
                            "tj_right_no"));
                } else {
                    finish_userinfo.setImageResource(MSFWResource.getResourseIdByName(
                            HistoreShare_v2.this,
                            "drawable",
                            "tj_right_yes"));
                }
            } else {
                finish_userinfo.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                                  "drawable",
                                                                                  "tj_right_yes"));
            }


        } else {
            finish_userinfo.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                              "drawable",
                                                                              "tj_right_no"));
        }
        if (isFinishForm) {
            finish_form.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_yes"));
        } else {
            finish_form.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_no"));
        }
        if (checkBigFile()) {
            finish_file.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_yes"));
        } else {
            finish_file.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_no"));
        }
        if (checkLzfsStaus()) {
            finish_lzfs.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_yes"));
        } else {
            finish_lzfs.setImageResource(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                                                                          "drawable",
                                                                          "tj_right_no"));
        }
    }

    /*
    *
    * 验证用户信息是否符合申报条件
    * */
    public boolean checkUserInfo() {
        String authlevel = permission.getAUTHLEVEL();
        if (!TextUtils.isEmpty(authlevel) && authlevel.equals("3") && !transportEntity.isRealUserAuth()) {//事项要求等级为3并且华讯没有完成认证
            return false;
        }
        return true;
    }

}
