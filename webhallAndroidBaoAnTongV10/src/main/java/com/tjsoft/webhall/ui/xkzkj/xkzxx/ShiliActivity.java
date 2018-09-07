package com.tjsoft.webhall.ui.xkzkj.xkzxx;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.ImageLoadUtils2;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.zzxx.ZZXXFragment;

import net.liang.appbaselibrary.base.BaseAppCompatActivity;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.utils.ImageLoadUtils;

public class ShiliActivity extends BaseAppCompatActivity {
    RelativeLayout back;
    TextView titleName;
    WebView webView;
    private int shili;

    @Override
    public void init() {
        super.init();
        back = (RelativeLayout) findViewById(R.id.back);
        titleName = (TextView) findViewById(R.id.titleName);
        webView = (WebView) findViewById(R.id.iv_xkz_yangli);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShiliActivity.this.finish();
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setBlockNetworkImage(false);

        shili = getIntent().getIntExtra("shili", 0);
        if (shili == 1) {
            titleName.setText("样例一");
            if (Constants.XKZshili1.endsWith("pdf")) {
                Uri uri = null;

                uri = Uri.parse(Constants.XKZshili1);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } else {
                webView.loadUrl(Constants.XKZshili1);


            }
        } else if (shili == 2) {
            titleName.setText("样例二");
            if (Constants.XKZshili2.endsWith("pdf")) {
                Uri uri = null;

                uri = Uri.parse(Constants.XKZshili2);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } else {
                webView.loadUrl(Constants.XKZshili2);

            }

        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shili;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }
}
