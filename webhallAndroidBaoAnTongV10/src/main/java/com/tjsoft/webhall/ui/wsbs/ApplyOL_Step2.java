package com.tjsoft.webhall.ui.wsbs;

import java.io.StringWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.XMLUtil;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Form;
import com.tjsoft.webhall.entity.FormItemBean;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
/**
 * 申报步骤二页面
 * @author Administrator
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class ApplyOL_Step2 extends AutoDialogActivity {
	private Button back, home, save, goBack;
	private WebView webView;
	private String PERMID = "";
	private List<Form> forms;
	private int index = 0;
	private List<FormItemBean> formItemBeans;
	private String userDetail;
	private int STATUS = -1;
	private String BSNUM ;
	private JSONArray dataidArray ;
	private String P_GROUP_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_apply_online_step2"));
		Constants.getInstance().addActivity(this);
		PERMID = getIntent().getStringExtra("PERMID");
		STATUS = getIntent().getIntExtra("STATUS", -1);
		BSNUM = getIntent().getStringExtra("BSNUM");
		P_GROUP_ID = getIntent().getStringExtra("P_GROUP_ID");
		
		InitView();
		initSetOnListener();
		dialog = Background.Process(this, GetFormByPermid, getString(MSFWResource.getResourseIdByName(ApplyOL_Step2.this, "string", "tj_loding")));
		if(STATUS!=-1 &&STATUS != 4 && STATUS != 9){
			save.setVisibility(View.GONE);
		}
	}

	private void initSetOnListener() {
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ApplyOL_Step2.this.finish();
			}
		});
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
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

	public String getUrlById(String id) {
		String url = Constants.DOMAIN+"u/forms/" + id + "/" + id + ".html";
		//String url = "http://192.9.207.124:8081/u/forms/" + id + "/" + id + ".html";
		//String url = "file:///android_asset/forms/" + id + "/" + id + ".html";
		return url;
	}
	
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
					DialogUtil.showUIToast(ApplyOL_Step2.this, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ApplyOL_Step2.this, getString(MSFWResource.getResourseIdByName(ApplyOL_Step2.this, "string", "tj_occurs_error_network")));
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
					forms = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Form>>() {}.getType());
					runOnUiThread(new Runnable() {
						public void run() {
							if (null != forms) {//去掉1
								for (int i = 0; i < forms.size(); i++) {
									if(null!=forms.get(i).getFORMTYPE()&&forms.get(i).getFORMTYPE().equals("1")){
										forms.remove(i);
									}
								}
								webView.loadUrl(getUrlById(forms.get(0).getID()));
							}
						}
					});

				} else {
					DialogUtil.showUIToast(ApplyOL_Step2.this, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ApplyOL_Step2.this, getString(MSFWResource.getResourseIdByName(ApplyOL_Step2.this, "string", "tj_occurs_error_network")));
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
						if(null != FileUtil.Load(ApplyOL_Step2.this, Constants.user.getUSER_ID()+"_"+PERMID)&&!FileUtil.Load(ApplyOL_Step2.this, Constants.user.getUSER_ID()+"_"+PERMID).equals("")){
							webView.loadUrl("javascript:shareformvalue('" + FileUtil.Load(ApplyOL_Step2.this, Constants.user.getUSER_ID()+"_"+PERMID) + "')");
						}
						else{
							webView.loadUrl("javascript:shareformvalue('" + userDetail + "')");
							if(Constants.isShare){
								new Thread(GetCorInfo).start();
							}
						}
						}
					});

				} else {
					DialogUtil.showUIToast(ApplyOL_Step2.this, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ApplyOL_Step2.this, getString(MSFWResource.getResourseIdByName(ApplyOL_Step2.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	private void InitView() {
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		save = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "save"));
		goBack = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "goBack"));
		webView = (WebView) findViewById(MSFWResource.getResourseIdByName(this, "id", "webView"));
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
					new Thread(GetFormByBsNo).start();//已申报的
				}else{
					new Thread(GetInfoByUserid).start();//新申报的
				}
				super.onPageFinished(view, url);
			}

		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
				AlertDialog.Builder alert = new AlertDialog.Builder(ApplyOL_Step2.this).setTitle(getString(MSFWResource.getResourseIdByName(ApplyOL_Step2.this, "string", "tj_notify"))).setMessage(message).setPositiveButton(getString(MSFWResource.getResourseIdByName(ApplyOL_Step2.this, "string", "tj_OK")), new AlertDialog.OnClickListener() {
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
	final Runnable GetCorInfo = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("USERCODE", Constants.user.getCODE());
				param.put("USERTYPE",  Constants.user.getTYPE());
				String response = HTTP.excuteShare("baseInfoShare", "SpaceDataInfoService", param.toString());
				final String ReturnValue = new JSONObject(response).getString("ReturnValue");
				runOnUiThread(new  Runnable() {
					public void run() {
						webView.loadUrl("javascript:shareformvalue('" + ReturnValue + "')");
					}
				});
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
				if(null !=dataidArray ){
					serializer.cdsect(dataidArray.getString(index));
				}
				serializer.endTag("", "dataid");
				serializer.startTag("", "pid");
				serializer.cdsect(PERMID);
				serializer.endTag("", "pid");
				serializer.startTag("", "bsnum");
				if(null!=BSNUM){
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
				JSONObject  formDataCache ;
				if(FileUtil.Load(ApplyOL_Step2.this, Constants.user.getUSER_ID()+"_"+PERMID).equals("")){
					  formDataCache = new JSONObject();
				}else{
					formDataCache =new JSONObject(FileUtil.Load(ApplyOL_Step2.this, Constants.user.getUSER_ID()+"_"+PERMID));
				}
				
				for (int j = 0; j < formItemBeans.size(); j++) {
					serializer.startTag("", formItemBeans.get(j).getName());
					serializer.attribute("", "type", formItemBeans.get(j).getType());
					serializer.cdsect(formItemBeans.get(j).getValue());
					serializer.endTag("", formItemBeans.get(j).getName());
					formDataCache.put(formItemBeans.get(j).getName(), formItemBeans.get(j).getValue());
				}
				FileUtil.Write(ApplyOL_Step2.this, Constants.user.getUSER_ID()+"_"+PERMID,formDataCache.toString());
				
				
				serializer.endTag("", "data");
				serializer.endDocument();
				forms.get(index).setXML(writer.toString());
				System.out.println("fuchl  " + forms.get(index).getXML());
				if (index == forms.size() - 1) {
					DialogUtil.showUIToast(ApplyOL_Step2.this, "表单填报完成，请尽快提交材料附件和证照信息！");
					String formsXML = "";
					for (int i = 0; i < forms.size(); i++) {
						formsXML+= forms.get(i).getXML();
						formsXML = formsXML.replaceAll("<\\?xml.*.\\?>", "");
					}
					formsXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><forms>"+formsXML+"</forms>";
					FileUtil.Write(ApplyOL_Step2.this, Constants.user.getUSER_ID()+"_"+PERMID+"XML",formsXML);
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
				ApplyOL_Step2.this.finish();
			} else {
				index--;
				webView.goBack();
			}
		}

	}

}
