package com.tjsoft.webhall.ui.xkzkj.xkzxx.zzxx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.XMLUtil;
import com.tjsoft.webhall.constants.BusConstant;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.CertInfoBean;
import com.tjsoft.webhall.entity.LoadObservableBean;
import com.tjsoft.webhall.entity.UserInfoBean;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.BaseFormCallBack;

import net.liang.appbaselibrary.base.BaseFragment;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.widget.dialog.DialogHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ZZXXFragment extends BaseFragment implements ZZXXContract.View {
    public ZZXXPresenter zzxxPresenter;
    public DialogHelper dialogHelper;
    public String certcode;
    public String status;
    public WebView webView;
    private LoadObservableBean observableBean = new LoadObservableBean();

    @Override
    public void init() {
        super.init();
        zzxxPresenter = new ZZXXPresenter(this);
        dialogHelper = new DialogHelper(getActivity());
        certcode = getArguments().getString("certcode");
        status = getArguments().getString("status");


        webView = (WebView) getActivity().findViewById(R.id.webView);
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
                KLog.e("开始自动填充表单数据...");
                zzxxPresenter.loadBaseFormData(certcode, status, getActivity());//此处执行加载表单信息操作
//                if (presenter.isItemsOld(intentBean)) {//老的申报，设置表单不可编辑
//                    webView.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            return true;
//                        }
//                    });
//                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                observableBean.showReloadLayout.set(true);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//                if (isShowAlert) {
//                    isShowAlert = false;
//                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity()).setTitle("提示")
//                            .setMessage(message)
//                            .setPositiveButton("去完善", new AlertDialog.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //回到基本表单页
//                                    EventBus.getDefault().post(BusConstant.GOTOBASEFORM);
//                                    result.confirm();
//                                }
//                            }).setNegativeButton("忽略", new AlertDialog.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    result.confirm();
//                                }
//                            });
//
//                    alert.setCancelable(false);
//                    alert.create();
//                    alert.show();
//                } else {
//                    result.cancel();
//                }
                return true;
            }
        });
//http://10.99.76.8:8083/u/certificates/102004001/102004001.html
        final String url = Constants.DOMAIN + "u/certificates/" + certcode + "/" + certcode + ".html";
        KLog.d("url=====================", url);
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url);
            }
        });

    }

    public void getBaseForm(final BaseFormCallBack callback) {
        zzxxPresenter.getFormsXml(certcode,getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String value) {
                        callback.onResponse(value, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public final class JSI {
        JSI() {
        }

        @JavascriptInterface
        public void save(String s) {
            zzxxPresenter.androidSave(s, certcode,getActivity());
        }

        @JavascriptInterface
        public void back(String s) {
//            if (index == 0) {
//                getActivity().finish();
//            } else {
//                index--;
//                webView.goBack();
//            }
        }

    }

    @Override
    protected MvpPresenter getPresenter() {
        return zzxxPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zzxx;
    }

    @Override
    public void showProgressDialog(String msg) {
        dialogHelper.showProgressDialog(msg);
    }

    @Override
    public void dismissProgressDialog() {
        dialogHelper.dismissProgressDialog();
    }

    @Override
    public void showOneCertInfo(CertInfoBean returnValue) {

        try {
            String dataxml =new String(Base64.decode(returnValue.getDataxml(), 0),"UTF-8") ;
            dataxml = dataxml.replaceAll("\n","").replaceAll("    ","").replaceAll("  ","");
            Log.i("dataxml",dataxml);
            final String jsonData = XMLUtil.toJsonForJSXkz(dataxml);
            Log.i("jsondataxxxxxxxxxxxx",jsonData);

            webView.loadUrl("javascript:shareformvalue('" + jsonData + "')");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void saveFormInfos() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:androidSave()");// 将表单数据拼装xml后写入手机文件
            }
        });



    }

    @Override
    public void showBaseInfo(UserInfoBean userInfoBean) {
        userInfoBean.setAGE_CARDTYPE("");
        String myuserdetail = new Gson().toJson(userInfoBean);
        webView.loadUrl("javascript:shareformvalue('" + myuserdetail + "')");




    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusConstant busConstant) {
        switch (busConstant) {
            case UPDATE://tab滑动，发出的通知（此时进行表单保存操作）
                KLog.e("tab滑动，发出的通知（此时进行表单保存操作）");
                saveFormInfos();
                break;

        }
    }
}
