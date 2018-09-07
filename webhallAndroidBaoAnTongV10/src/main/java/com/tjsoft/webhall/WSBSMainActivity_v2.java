package com.tjsoft.webhall;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.search.Search;
import com.tjsoft.webhall.ui.work.QYSQ;
import com.tjsoft.webhall.ui.wsbs.GRBSFragment;
import com.tjsoft.webhall.ui.wsbs.MyFragment;
import com.tjsoft.webhall.ui.wsbs.NetworkListActivity;
import com.tjsoft.webhall.ui.wsbs.QYBSFragment;
import com.tjsoft.webhall.ui.xkzkj.XKZKJActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import com.webank.wbcloudfaceverify2.tools.WbCloudFaceVerifySdk;
//import com.webank.wbcloudfaceverify2.ui.FaceVerifyStatus;

/**
 * @author S_Black
 */
public class WSBSMainActivity_v2 extends FragmentActivity {
    // 四个tab布局
    private RelativeLayout homeLayout, grbsLayout, qybsLayout, myLayout;
    private ViewPager mViewPager;
    private TabFragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments;
    private TextView titleName;
    private RelativeLayout back;
    private int index;
    public static boolean hasGotToken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Constants.initImageLoader(this);
        Constants.getInstance().addActivity(this);
        setContentView(MSFWResource.getResourseIdByName(this, "layout", "activity_wsbs_main_v2"));
        index = getIntent().getIntExtra("index", 0);
        initView();
//        initAccessTokenWithAkSk();

    }


    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                DialogUtil.showUIToast(WSBSMainActivity_v2.this, "AK，SK方式获取token失败");
            }
        }, getApplicationContext(), "VpnslHxxBrvtiYiLqdzbX3Un", "zsEH88wLLitjWmts0tWSPWotpNXXRkik");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (!Constants.DEBUG_TOGGLE) {
            GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
            if (gloabDelegete != null) {
                TransportEntity transportEntity = gloabDelegete.getUserInfo();
                System.out.println("fuchl  主页面LOG getIdCardType " + transportEntity.getIdcardType());
                if (checkRealNameInfo(transportEntity)) {
//					notice_wdbj.setVisibility(View.GONE);
                } else {
                    Login(0, transportEntity);
                }
            }
        }
        super.onResume();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        titleName = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "titleName"));
        mViewPager = (ViewPager) findViewById(MSFWResource.getResourseIdByName(this, "id", "main_viewPage"));
        homeLayout = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "tab_home_rl"));
        homeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Constants.getInstance().exit();
            }
        });
        grbsLayout = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "tab_grbs_rl"));
        grbsLayout.setSelected(true);
        grbsLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mViewPager.setCurrentItem(0);
            }
        });
        qybsLayout = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "tab_qybs_rl"));
        qybsLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mViewPager.setCurrentItem(1);
            }
        });
        myLayout = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "tab_my_rl"));
        myLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mViewPager.setCurrentItem(2);
            }
        });
        mFragments = new ArrayList<Fragment>();
        Fragment grbs = new GRBSFragment();
        Fragment qybs = new QYBSFragment();
        Fragment my = new MyFragment();
        mFragments.add(grbs);
        mFragments.add(qybs);
        mFragments.add(my);

        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), mFragments);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(index);
        switch (index) {
            case 0:
                titleName.setText("选择事项申报");
                grbsLayout.setSelected(true);
                qybsLayout.setSelected(false);
                myLayout.setSelected(false);
                break;
            case 1:
                titleName.setText("选择事项申报");
                grbsLayout.setSelected(false);
                qybsLayout.setSelected(true);
                myLayout.setSelected(false);
                break;
            case 2:
                titleName.setText("我的");
                grbsLayout.setSelected(false);
                qybsLayout.setSelected(false);
                myLayout.setSelected(true);
                break;
            default:
                grbsLayout.setSelected(true);
                qybsLayout.setSelected(false);
                myLayout.setSelected(false);
                break;
        }
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                switch (arg0) {
                    case 0:
                        titleName.setText("选择事项申报");
                        grbsLayout.setSelected(true);
                        qybsLayout.setSelected(false);
                        myLayout.setSelected(false);
                        break;
                    case 1:
                        titleName.setText("选择事项申报");
                        grbsLayout.setSelected(false);
                        qybsLayout.setSelected(true);
                        myLayout.setSelected(false);
                        break;
                    case 2:
                        titleName.setText("我的");
                        grbsLayout.setSelected(false);
                        qybsLayout.setSelected(false);
                        myLayout.setSelected(true);
                        break;
                    default:
                        grbsLayout.setSelected(true);
                        qybsLayout.setSelected(false);
                        myLayout.setSelected(false);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }


    class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        //继承FragmentPagerAdapter类 ,并自定义的构造器
        private List<Fragment> fragments;

        public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {


            return fragments.get(position);

        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    /**
     * 万能密码登录
     *
     * @param flag 1 申报  2 预约  3 咨询
     */
    private void Login(final int flag, final TransportEntity transportEntity) {
        final Runnable LoginNotPassW = new Runnable() {
            @Override
            public void run() {
                try {
                    Constants.user=null;
                    JSONObject param = new JSONObject();
                    if (!TextUtils.isEmpty(transportEntity.getName())) {
                        param.put("USERNAME", transportEntity.getName());
                    } else {
//						showDialog();
                        return;
                    }
                    param.put("PASSWORD", "C4BD1B3942F3C2ACD7657CBD0B5D952F");
                    String response = HTTP.excute("login", "RestUserService", param.toString());
                    final JSONObject json = new JSONObject(response);
                    String code = json.getString("code");
                    if (code.equals("200")) {
                        Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
                        int ver = transportEntity.getVersion();
//						if(version<ver){
//							updateUserInfo(transportEntity);
//						}
                        switch (flag) {
                            case 1:
//							new Thread(GetGroup).start();
//							Background.Process(PermGuideContainer.this, GetGroup, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_loding")));// 获取分组ID
                                break;
//						case 2:
//							intent = new Intent();
//							intent.setClass(PermGuideContainer.this, ReserveSubmit.class);
//							intent.putExtra("permission", permission);
//							startActivity(intent);
//							break;
//						case 3:
//							Intent intent = new Intent();
//							intent.setClass(PermGuideContainer.this, AddConsult.class);
//							intent.putExtra("permission", permission);
//							startActivity(intent);							
//							break;
                            default:
                                break;
                        }
                    } else if (code.equals("600")) {
//						if(transportEntity.getEnterpriseStatus().equals("3")){
//							QY_Register(flag, transportEntity);							
//						}else {
//							GR_Register(flag, transportEntity);
//						}					
                    } else {
                        String error = json.getString("error");
                        DialogUtil.showUIToast(WSBSMainActivity_v2.this, error);
                    }

                } catch (Exception e) {
                    DialogUtil.showUIToast(WSBSMainActivity_v2.this, WSBSMainActivity_v2.this.getString(MSFWResource.getResourseIdByName(WSBSMainActivity_v2.this, "string", "tj_occurs_error_network")));
                    e.printStackTrace();

                }
            }
        };
        new Thread(LoginNotPassW).start();
//		dialog=Background.Process(PermGuideContainer.this, LoginNotPassW,PermGuideContainer.this.getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_loding")));
    }

    /**
     * 判断实名信息是否完全
     *
     * @param t
     * @return
     */
    private boolean checkRealNameInfo(TransportEntity t) {
        if (!TextUtils.isEmpty(t.getToken())) {
            return false;
        }
        return true;
    }


    /**
     * 更多
     *
     * @param view
     */
    private void showPopupMore(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(WSBSMainActivity_v2.this).inflate(MSFWResource.getResourseIdByName(this, "layout", "main_popup_more"), null);
        LinearLayout jdcx_ll = (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(this, "id", "popup_more_jdcx"));
        LinearLayout wdfw_ll = (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(this, "id", "popup_more_wdfw"));
        LinearLayout qysq_ll = (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(this, "id", "popup_more_qysq"));
        LinearLayout scxkz_ll = (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(this, "id", "popup_more_scxkz"));


//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels) * 0.35); //屏幕宽
        int height = dm.heightPixels; //屏幕高
        final PopupWindow popupWindow = new PopupWindow(contentView,
                width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(MSFWResource.getResourseIdByName(this, "style", "popwin_anim_style"));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.6f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(MSFWResource.getResourseIdByName(this, "color", "tj_transparent"))));

        // 设置好参数之后再show

//        popupWindow.showAsDropDown(view,0,25);
        popupWindow.showAtLocation(findViewById(MSFWResource.getResourseIdByName(this, "id", "root_parent")), Gravity.RIGHT | Gravity.TOP, DensityUtil.dip2px(this, 5), DensityUtil.dip2px(this, 70));

        jdcx_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent();
                intent.setClass(WSBSMainActivity_v2.this, Search.class);
                startActivity(intent);
            }
        });
        wdfw_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent();
                intent.setClass(WSBSMainActivity_v2.this, NetworkListActivity.class);
                startActivity(intent);
            }
        });
        qysq_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
//				popupWindow.dismiss();
                Intent intent = new Intent();
                intent.setClass(WSBSMainActivity_v2.this, QYSQ.class);
                WSBSMainActivity_v2.this.startActivity(intent);
            }
        });
        scxkz_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
//				popupWindow.dismiss();
                Intent intent = new Intent();
                intent.setClass(WSBSMainActivity_v2.this, XKZKJActivity.class);
                WSBSMainActivity_v2.this.startActivity(intent);
            }
        });
    }
}
