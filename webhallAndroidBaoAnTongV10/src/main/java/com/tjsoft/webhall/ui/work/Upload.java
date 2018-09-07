package com.tjsoft.webhall.ui.work;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.XMLUtil;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.Form;
import com.tjsoft.webhall.entity.FormItemBean;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.ShareMaterial;
import com.tjsoft.webhall.fileChoose.FileChooserActivity;
import com.tjsoft.webhall.ui.bsdt.WDBJ;
import com.tjsoft.webhall.ui.materialmanage.MaterialManageActivity;
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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 材料上传
 *
 * @author Administrator
 */
@SuppressLint("SetJavaScriptEnabled")
public class Upload extends AutoDialogActivity {

    private ViewPager viewPager;// 页卡内容
    private ImageView imageView;// 动画图片
    private TextView textView1, textView2, textView3;
    private List<TextView> titles;
    private List<View> views;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int TYPE = 2;// 当前页卡编号
    private int bmpW = 0;// 动画图片宽度
    private View formPage, bigFilePage, licensePage;// 各个页卡
    private DisplayMetrics dm;// 设备管理
    private LayoutInflater inflater;
    private Button back, save, goBack;
    private Intent intent;
    private WebView webView;
    private Permission permission;
    private String PERMID = "";
    private List<Form> forms;
    private int index = 0;
    private List<FormItemBean> formItemBeans;// 表单实例list
    private String userDetail;
    public static List<ApplyBean> applyBeans, bigFileDate, licenseDate;
    private Handler handler = new MyHandler();
    private final int GET_APPLY_BEAN_SUCCESS = 2;
    private ListView bigFileList, licenseList;
    private String mark = "";
    private TextView title;
    private LinearLayout applyLay, formBtn;
    private Button applySave, applySubmit;
    private String status = "0";
    private int STATUS = -1;
    private String BSNUM;
    private JSONArray dataidArray;
    private Intent fileChooserIntent;
    private String MATERIALS;
    private String P_GROUP_ID;
    private String formsXML = "";// 表单信息

    private static final int REQUEST_FILE_CODE = 1;
    private static final int REQUEST_PHOTO_CODE = 2;
    public static final String EXTRA_FILE_CHOOSER = "file_chooser";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_history_share"));
        Constants.getInstance().addActivity(this);
        mContext = this;
        permission = (Permission) getIntent().getSerializableExtra("permission");
        PERMID = permission.getID();
        STATUS = getIntent().getIntExtra("STATUS", -1);
        mark = getIntent().getStringExtra("mark");
        BSNUM = getIntent().getStringExtra("BSNUM");
        P_GROUP_ID = getIntent().getStringExtra("P_GROUP_ID");
        InitImageView();
        InitTextView();
        InitViewPager();
        InitView1();
        InitApply();// 初始化在线申报
        initSetOnListener();
        if (STATUS != -1 && STATUS != 4 && STATUS != 9) {
            applyLay.setVisibility(View.GONE);
            formBtn.setVisibility(View.GONE);
        }

        dialog = Background.Process(Upload.this, GetFormByPermid, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_loding")));
        if (STATUS == -1) {
            dialog = Background.Process(Upload.this, GetApplyList, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_loding")));
        } else {
            dialog = Background.Process(Upload.this, GetApplyListByBS, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_loding")));
        }

        title.setText("材料上传");// 修改标题

    }

    private void initSetOnListener() {
        applySave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                status = "9";
                formsXML = getFormsXML();
                dialog = Background.Process(Upload.this, ApplySubmit, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_loding")));

            }
        });
        applySubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                status = "0";
                formsXML = getFormsXML();
                dialog = Background.Process(Upload.this, ApplySubmit, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_loding")));
            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Upload.this.finish();
            }
        });
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:androidSave()");
            }
        });
        goBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (index == 1) {
                    goBack.setVisibility(View.GONE);
                }
                index--;
                webView.goBack();
            }
        });

    }

    /**
     * 空间获取大附件并比对
     */
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
                List<ShareMaterial> attachShares = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<ShareMaterial>>() {
                }.getType());

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
                        bigFileList.setAdapter(new UploadAdapter(bigFileDate));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * 空间获取证照并比对
     */
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
                param.put("CERTCODE", certCode);
                String response = HTTP.excuteShare("certInfoSearch", "SpaceCertInfoService", param.toString());
                JSONObject json = new JSONObject(response);
                List<ShareMaterial> certShares = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<ShareMaterial>>() {
                }.getType());
                for (int i = 0; i < licenseDate.size(); i++) {
                    for (int j = 0; j < certShares.size(); j++) {
                        if (licenseDate.get(i).getCLBH().equals(certShares.get(j).getCERTCODE())) {// 有共享
                            licenseDate.get(i).setSTATUS(certShares.get(j).getCOMPRESULT());
                            licenseDate.get(i).setFILEID(certShares.get(j).getID());
                            licenseDate.get(i).setFILEPATH(certShares.get(j).getATTACHURL());
                            licenseDate.get(i).setFILENAME(certShares.get(j).getATTACHNAME());
                            licenseDate.get(i).setFILEDEL("0");
                            licenseDate.get(i).setATTS(certShares.get(j).getATTS());
                            Constants.material.put(certShares.get(j).getCERTCODE(), certShares.get(j).getATTBeans());// 将共享结果放入map中
                        }

                    }
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        Collections.sort(licenseDate, new ApplyBeanComparator());
                        licenseList.setAdapter(new UploadAdapter(licenseDate));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void InitApply() {
        formBtn.setVisibility(View.GONE);
        if (mark.equals("6")) {
            title.setText("在线申报");
            applyLay.setVisibility(View.VISIBLE);
        }

    }

    public String getUrlById(String id) {
        String url = Constants.DOMAIN + "u/forms/" + id + "/" + id + ".html";
        // String url = "http://192.9.207.124:8081/u/forms/" + id + "/" + id +
        // ".html";
        // String url = "file:///android_asset/forms/" + id + "/" + id +
        // ".html";
        return url;
    }

    private void InitView1() {
        title = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "title"));
        applyLay = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "applyLay"));
        formBtn = (LinearLayout) formPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "formBtn"));
        applySave = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "applySave"));
        applySubmit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "applySubmit"));

        save = (Button) formPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "save"));
        goBack = (Button) formPage.findViewById(MSFWResource.getResourseIdByName(this, "id", "goBack"));
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
                super.onPageFinished(view, url);
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Upload.this).setTitle(getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_notify"))).setMessage(message).setPositiveButton(getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_OK")), new AlertDialog.OnClickListener() {
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

    final Runnable GetApplyListByBS = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("BSNUM", BSNUM);
                String response = HTTP.excute("getInsMaterialInfo", "RestOnlineDeclareService", param.toString());
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    MATERIALS = new String(Base64.decode(new JSONObject(json.getString("ReturnValue")).getString("MATERIALS"), 1));
                    System.out.println("fuchl    MATERIALS:" + MATERIALS);
                    applyBeans = XMLUtil.parseMaterials(MATERIALS);
                    if (Constants.material.size() == 0) {//只填充一次
                        XMLUtil.material2Map(MATERIALS);
                    }
                    if (null != applyBeans) {
                        bigFileDate = new ArrayList<ApplyBean>();
                        licenseDate = new ArrayList<ApplyBean>();
                        for (int i = 0; i < applyBeans.size(); i++) {

                            //从map比对初始化列表状态
                            if (null != Constants.material.get(applyBeans.get(i).getCLBH()) && Constants.material.get(applyBeans.get(i).getCLBH()).size() != 0) {
                                applyBeans.get(i).setSTATUS("1");

                            }

                            if (applyBeans.get(i).getDZHYQ().contains("3") || (applyBeans.get(i).getDZHYQ().contains("5"))) {
                                bigFileDate.add(applyBeans.get(i));

                            } else if (applyBeans.get(i).getDZHYQ().contains("4")) {
                                licenseDate.add(applyBeans.get(i));
                            }
                        }

                    }
                    handler.sendEmptyMessage(GET_APPLY_BEAN_SUCCESS);
                } else {
                    DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }
    };

    private class ApplyBeanComparator implements Comparator<ApplyBean> {
        public int compare(ApplyBean o1, ApplyBean o2) {
            int a = Integer.parseInt(o1.getSTATUS());
            int b = Integer.parseInt(o2.getSTATUS());
            return a - b;
        }
    }

    final Runnable GetApplyList = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("PERMID", PERMID);
                param.put("PAGENO", "1");
                param.put("PAGESIZE", "1000");
                String response = HTTP.excute("getClxxByPermid", "RestPermissionitemService", param.toString());
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    applyBeans = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<ApplyBean>>() {
                    }.getType());
                    if (null != applyBeans) {
                        bigFileDate = new ArrayList<ApplyBean>();
                        licenseDate = new ArrayList<ApplyBean>();
                        for (int i = 0; i < applyBeans.size(); i++) {
                            //从map比对初始化列表状态
                            if (null != Constants.material.get(applyBeans.get(i).getCLBH()) && Constants.material.get(applyBeans.get(i).getCLBH()).size() != 0) {
                                applyBeans.get(i).setSTATUS("1");
                            }

                            if (applyBeans.get(i).getDZHYQ().contains("3") || (applyBeans.get(i).getDZHYQ().contains("5"))) {
                                bigFileDate.add(applyBeans.get(i));

                            } else if (applyBeans.get(i).getDZHYQ().equals("4")) {
                                licenseDate.add(applyBeans.get(i));
                            }
                        }
                    }
                    handler.sendEmptyMessage(GET_APPLY_BEAN_SUCCESS);
                } else {
                    DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_occurs_error_network")));
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
                String response = HTTP.excute("getFormByPermid", "RestPermissionitemService", param.toString());
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    forms = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Form>>() {
                    }.getType());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (null != forms) {
                                webView.loadUrl(getUrlById(forms.get(0).getID()));
                            }
                        }
                    });

                } else {
                    DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_occurs_error_network")));
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
                String response = HTTP.excute("getInsFormData", "RestOnlineDeclareService", param.toString());
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    String returnValue = json.getString("ReturnValue");
                    String FORMS = new JSONObject(returnValue).getString("FORMS");
                    String formXml = new String(Base64.decode(FORMS.getBytes(), 1));
                    final String jsonData = XMLUtil.toJsonForJS(formXml);
                    dataidArray = new JSONObject(jsonData).getJSONArray("dataid");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            webView.loadUrl("javascript:shareformvalue('" + jsonData + "','form1')");
                        }
                    });

                } else {
                    DialogUtil.showUIToast(Upload.this, json.getString("error"));
                }

            } catch (Exception e) {
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
                String response = HTTP.excute("getInfoByUserid", "RestUserService", param.toString());
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    userDetail = json.getString("ReturnValue");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (null != FileUtil.Load(Upload.this, Constants.user.getUSER_ID() + "_" + PERMID) && !FileUtil.Load(Upload.this, Constants.user.getUSER_ID() + "_" + PERMID).equals("")) {
                                // 从缓存中读取
                                webView.loadUrl("javascript:shareformvalue('" + FileUtil.Load(Upload.this, Constants.user.getUSER_ID() + "_" + PERMID) + "')");
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
        public void save(String s) {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            try {

                JSONObject json = new JSONObject(s);
                formItemBeans = JSONUtil.getGson().fromJson(json.getString("data"), new TypeToken<List<FormItemBean>>() {
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
                if (FileUtil.Load(Upload.this, Constants.user.getUSER_ID() + "_" + PERMID).equals("")) {
                    formDataCache = new JSONObject();
                } else {
                    formDataCache = new JSONObject(FileUtil.Load(Upload.this, Constants.user.getUSER_ID() + "_" + PERMID));
                }

                for (int j = 0; j < formItemBeans.size(); j++) {
                    serializer.startTag("", formItemBeans.get(j).getName());
                    serializer.attribute("", "type", formItemBeans.get(j).getType());
                    serializer.cdsect(formItemBeans.get(j).getValue());
                    serializer.endTag("", formItemBeans.get(j).getName());
                    formDataCache.put(formItemBeans.get(j).getName(), formItemBeans.get(j).getValue());
                }
                FileUtil.Write(Upload.this, Constants.user.getUSER_ID() + "_" + PERMID, formDataCache.toString());

                serializer.endTag("", "data");
                serializer.endDocument();
                forms.get(index).setXML(writer.toString());
                System.out.println("fuchl  " + forms.get(index).getXML());
                if (index == forms.size() - 1) {// 最后一张表单时将xml组装
                    // DialogUtil.showUIToast(Upload.this,
                    // "表单填报完成，请尽快提交材料附件和证照信息！");
                    for (int i = 0; i < forms.size(); i++) {
                        formsXML += forms.get(i).getXML();
                        formsXML = formsXML.replaceAll("<\\?xml.*.\\?>", "");
                    }
                    formsXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><forms>" + formsXML + "</forms>";
                    FileUtil.Write(Upload.this, Constants.user.getUSER_ID() + "_" + PERMID + "XML", formsXML);
                    finish();
                } else {
                    index++;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            webView.loadUrl(getUrlById(forms.get(index).getID()));
                            goBack.setVisibility(View.VISIBLE);
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
                Upload.this.finish();
            } else {
                index--;
                webView.goBack();
            }
        }

    }

    private void InitViewPager() {
        back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
        viewPager = (ViewPager) findViewById(MSFWResource.getResourseIdByName(this, "id", "vPager"));
        views = new ArrayList<View>();
        inflater = getLayoutInflater();
        formPage = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_forms"), null);
        bigFilePage = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_share_big_file"), null);
        licensePage = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_share_big_file"), null);

        views.add(formPage);
        views.add(bigFilePage);
        views.add(licensePage);
        viewPager.setAdapter(new MyViewPagerAdapter(views));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        bigFileList = (ListView) bigFilePage.findViewById(MSFWResource.getResourseIdByName(this, "id", "listView"));
        licenseList = (ListView) licensePage.findViewById(MSFWResource.getResourseIdByName(this, "id", "listView"));
        ImageView empty = (ImageView) bigFilePage.findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
        bigFileList.setEmptyView(empty);
        empty = (ImageView) licensePage.findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
        licenseList.setEmptyView(empty);
    }

    /**
     * 初始化头标
     */

    private void InitTextView() {

        textView1 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text1"));
        textView2 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text2"));
        textView3 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text3"));


        titles = new ArrayList<TextView>();
        titles.add(textView1);
        titles.add(textView2);
        titles.add(textView3);

        textView1.setOnClickListener(new MyOnClickListener(0));
        textView2.setOnClickListener(new MyOnClickListener(1));
        textView3.setOnClickListener(new MyOnClickListener(2));

    }

    /**
     * 2 * 初始化动画 3
     */

    private void InitImageView() {
        imageView = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "cursor"));
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dm.widthPixels * 4 / 25, DensityUtil.dip2px(this, 2));
        imageView.setLayoutParams(params);
        bmpW = dm.widthPixels * 4 / 25;
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        Animation animation = new TranslateAnimation(0, offset, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(1);
        imageView.startAnimation(animation);
    }

    /**
     * 头标点击监听 3
     */
    private class MyOnClickListener implements OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }

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
                TYPE = 2;// 材料附件
            } else if (arg0 == 1) {// 证照信息
                TYPE = 1;
            }
            Animation animation = new TranslateAnimation(one * currIndex + offset, one * arg0 + offset, 0, 0);
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            imageView.startAnimation(animation);
            for (int i = 0; i < titles.size(); i++) {
                titles.get(i).setTextColor(MSFWResource.getResourseIdByName(Upload.this, "color", "tj_tab_text"));
            }
            titles.get(arg0).setTextColor(Upload.this.getResources().getColor(MSFWResource.getResourseIdByName(Upload.this, "color", "tj_my_green")));
        }

    }

    class UploadAdapter extends BaseAdapter {

        private List<ApplyBean> data;

        public UploadAdapter(List<ApplyBean> applyBeans) {
            this.data = applyBeans;

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
                convertView = inflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_histore_share_item"), parent, false);
                container.type = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "type"));
                container.name = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "name"));
                container.status = (ImageButton) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "status"));
                container.chooseFile = (Button) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "chooseFile"));
                container.choosePhoto = (Button) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "choosePhoto"));
                container.download = (Button) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "download"));
                convertView.setTag(container);
            } else {
                container = (Container) convertView.getTag();
            }

            final ApplyBean applyBean = data.get(position);
            String status = applyBean.getSTATUS();
            // 已共享 未共享分类
            if (position == 0 && status.equals("1")) {// 第一个
                container.type.setVisibility(View.VISIBLE);
                container.type.setText("已提交");
                container.type.setBackgroundColor(getResources().getColor(MSFWResource.getResourseIdByName(Upload.this, "color", "tj_is_share")));

            } else if (position == 0 && !status.equals("1")) {
                container.type.setVisibility(View.VISIBLE);
                container.type.setText("未提交");
                container.type.setBackgroundColor(getResources().getColor(MSFWResource.getResourseIdByName(Upload.this, "color", "tj_no_share")));
            } else if (position > 0 && !status.equals(data.get(position - 1).getSTATUS())) {// 状态不一样加分割条
                container.type.setVisibility(View.VISIBLE);
                container.type.setText("已提交");
                container.type.setBackgroundColor(getResources().getColor(MSFWResource.getResourseIdByName(Upload.this, "color", "tj_is_share")));
            } else {
                container.type.setVisibility(View.GONE);
            }
            container.chooseFile.setOnClickListener(new OnClickListener() {// 选择文件上传
                @Override
                public void onClick(View v) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        fileChooserIntent = new Intent(Upload.this, FileChooserActivity.class);
                        fileChooserIntent.putExtra("PERMID", PERMID);
                        fileChooserIntent.putExtra("FILEID", applyBean.getFILEID());
                        fileChooserIntent.putExtra("CLMC", applyBean.getCLMC());
                        fileChooserIntent.putExtra("FILE", applyBean);
                        fileChooserIntent.putExtra("position", position);
                        fileChooserIntent.putExtra("TYPE", TYPE + "");
                        startActivityForResult(fileChooserIntent, REQUEST_FILE_CODE);
                    } else {
                        DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_sdcard_unmonted_hint")));
                    }
                }
            });
            container.choosePhoto.setOnClickListener(new OnClickListener() {// 拍照上传

                @Override
                public void onClick(View v) {

                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        intent = new Intent();
                        intent.setClass(Upload.this, TakePhotos.class);
                        intent.putExtra("PERMID", PERMID);
                        intent.putExtra("FILEID", applyBean.getFILEID());
                        intent.putExtra("CLMC", applyBean.getCLMC());
                        intent.putExtra("FILE", applyBean);
                        intent.putExtra("position", position);
                        intent.putExtra("TYPE", TYPE + "");
                        startActivityForResult(intent, REQUEST_PHOTO_CODE);
                    } else {
                        DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_sdcard_unmonted_hint")));
                    }
                }
            });
            container.download.setOnClickListener(new OnClickListener() { // 文件下载

                @Override
                public void onClick(View v) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        Intent intent = new Intent();
                        intent.setClass(Upload.this, Download.class);
                        intent.putExtra("title", applyBean.getCLMC());
                        intent.putExtra("FILEID", applyBean.getFILEID());
                        startActivity(intent);
                    } else {
                        DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_sdcard_unmonted_hint")));
                    }
                }
            });
            container.name.setText(applyBean.getCLMC());
            if (status.equals("1") && (null == applyBean.getFILEID() || applyBean.getFILEID().equals(""))) {// 设置提交状态
                container.status.setBackgroundResource(MSFWResource.getResourseIdByName(Upload.this, "drawable", "tj_is_submit"));
                container.download.setVisibility(View.GONE);
            } else if (status.equals("1") && null != applyBean.getFILEID() && !applyBean.getFILEID().equals("")) {
                container.status.setBackgroundResource(MSFWResource.getResourseIdByName(Upload.this, "drawable", "tj_is_share"));
                container.download.setVisibility(View.GONE);

            } else {
                container.status.setBackgroundResource(MSFWResource.getResourseIdByName(Upload.this, "drawable", "tj_no_submit"));
                container.download.setVisibility(View.GONE);
            }
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    // System.out.println("@@@@@@@@@@@@@@@"+data.get(position).getATTS().toString());
                    intent.putExtra("applyBean", applyBean);
                    intent.putExtra("PERMID", PERMID);
                    intent.putExtra("position", position);
                    intent.putExtra("TYPE", TYPE);
                    ArrayList<ApplyBean> CLBHList = new ArrayList<ApplyBean>();
                    for (int i = 0; i < data.size(); i++) {
                        CLBHList.add(data.get(i));
                    }
                    intent.putExtra("CLBHList",CLBHList);
                    System.out.print(CLBHList.toString()+"xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    intent.setClass(Upload.this, MaterialManageActivity.class);
                    startActivityForResult(intent, 3);
                }
            });

            return convertView;

        }

        public final class Container {
            TextView type;// 材料大分类
            TextView name;// 材料名称
            ImageButton status;// 已提交、未提交
            Button chooseFile;// 选择文件
            Button choosePhoto;// 拍照上传
            Button download;// 下载
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Collections.sort(licenseDate, new ApplyBeanComparator());// 重新排序
        Collections.sort(bigFileDate, new ApplyBeanComparator());
        bigFileList.setAdapter(new UploadAdapter(bigFileDate));
        licenseList.setAdapter(new UploadAdapter(licenseDate));
        super.onActivityResult(requestCode, resultCode, data);

    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case GET_APPLY_BEAN_SUCCESS:
//				Collections.sort(licenseDate, new ApplyBeanComparator());// 重新排序
//				Collections.sort(bigFileDate, new ApplyBeanComparator());
                    bigFileList.setAdapter(new UploadAdapter(bigFileDate));
                    licenseList.setAdapter(new UploadAdapter(licenseDate));
                    // 获取空间大附件和证照信息
                    if (Constants.isShare && (STATUS == -1 || STATUS == 4 || STATUS == 9)) {//空间开启，并且办件类型是新办理的、暂存件、预审不通过的才可以比对
                        dialog = Background.Process(Upload.this, GetAttachShare, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_loding")));
                        dialog = Background.Process(Upload.this, GetCertShare, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_loding")));
                    }
                    break;

                default:
                    break;
            }
        }
    }

    final Runnable ApplySubmit = new Runnable() {
        @Override
        public void run() {
            try {

                String businessXML = getBusinessXML();
                String materialsXML = getMaterialsXML();

                if (null == formsXML || formsXML.equals("")) {
                    // DialogUtil.showUIToast(Upload.this, "请先完善表单信息");
                    return;
                }
                if (null == businessXML || businessXML.equals("")) {
                    DialogUtil.showUIToast(Upload.this, "业务信息异常");
                    return;
                }
                if (null == materialsXML || materialsXML.equals("")) {
                    DialogUtil.showUIToast(Upload.this, "材料信息异常");
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
                System.out.println("fuchl" + formsXML);
                System.out.println("fuchl" + businessXML);
                System.out.println("fuchl" + materialsXML);

                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("FORMS", new String(Base64.encode(formsXML.getBytes(), 0), "UTF-8"));
                param.put("BUSINESS", new String(Base64.encode(businessXML.getBytes(), 0), "UTF-8"));
                param.put("MATERIALS", new String(Base64.encode(materialsXML.getBytes(), 0), "UTF-8"));

                String response = HTTP.excute("submit", "RestOnlineDeclareService", param.toString());
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    if (status.equals("0")) {
                        DialogUtil.showUIToast(Upload.this, "申报成功！");
                    } else {
                        DialogUtil.showUIToast(Upload.this, "暂存成功！");
                    }
                    intent = new Intent();
                    intent.setClass(Upload.this, WDBJ.class);
                    startActivity(intent);
                    finish();
                } else {
                    DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                DialogUtil.showUIToast(Upload.this, getString(MSFWResource.getResourseIdByName(Upload.this, "string", "tj_occurs_error_network")));
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
        webView.loadUrl("javascript:androidSave()");// 将表单数据拼装xml后写入手机文件
        return FileUtil.Load(this, Constants.user.getUSER_ID() + "_" + PERMID + "XML");// 从文件里面获取xml
    }

    private String getMaterialsXML() throws Exception {
        return "";
    }

    private String getBusinessXML() throws Exception {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        serializer.setOutput(writer);
        serializer.startDocument("utf-8", null);
        serializer.startTag("", "business");

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

        serializer.endTag("", "business");
        serializer.endDocument();
        String xml = writer.toString();
        return xml;

    }

}
