package com.tjsoft.webhall.ui.xkzkj.xkzxx;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.CheckUploadUtil;
import com.tjsoft.webhall.constants.BusConstant;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ATT;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.CertInfoBean;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.xkzcl.MaterialManageFragment;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.zzxx.ZZXXFragment;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.zzxx.ZZXXPresenter;

import net.liang.appbaselibrary.base.BaseAppCompatActivity;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.widget.dialog.DialogHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class XKZXXActivity extends BaseAppCompatActivity implements XKZXXContract.View {
    public static boolean isAllUpload = true;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView titleName;
    RelativeLayout back;
    Button tv_submit_xkz;
    Button goBack2;
    Button next;
    TextView edit;
    ImageView title_file_status;
    ImageView title_form_staus;
    String certcode;
    String status;
    public XKZXXPresenter xkzxxPresenter;
    public DialogHelper dialogHelper;
    private BusConstant myBusConstant = BusConstant.CHECK_FORM_WRONG;//表单信息是否正确

    private List<String> titles;
    private ZZXXFragment zzxxFragment;
    private MaterialManageFragment materialManageFragment;
    private String formsXML;
    private String materialsXML;
    private String USERCODE;
    private String CERTNAME;
    private String token;
    private String name;
    private List<ATTBean> atts;// 文件集合

    @Override
    public void init() {
        super.init();
        xkzxxPresenter = new XKZXXPresenter(this);
        certcode = getIntent().getStringExtra("certcode");
        status = getIntent().getStringExtra("status");
        CERTNAME = getIntent().getStringExtra("CERTNAME");
        USERCODE = getIntent().getStringExtra("USERCODE");
        name = getIntent().getStringExtra("name");
        token = getIntent().getStringExtra("token");
        dialogHelper = new DialogHelper(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        back = (RelativeLayout) findViewById(R.id.back);
        tv_submit_xkz = (Button) findViewById(R.id.tv_submit_xkz);
        goBack2 = (Button) findViewById(R.id.goBack2);
        next = (Button) findViewById(R.id.next);
        edit = (TextView) findViewById(R.id.tv_edit);
        title_form_staus = (ImageView) findViewById(R.id.title_form_staus);
        title_file_status = (ImageView) findViewById(R.id.title_file_status);
        titleName = (TextView) findViewById(R.id.titleName);

        goBack2.setVisibility(View.GONE);
        tv_submit_xkz.setVisibility(View.GONE);
        titleName.setText(name);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                xkzxxPresenter.setEdit(!xkzxxPresenter.isEdit());



            }
        });
        goBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                EventBus.getDefault().post(BusConstant.UPDATE);
                if (position == 0) {
                    next.setVisibility(View.VISIBLE);
                    goBack2.setVisibility(View.GONE);
                    tv_submit_xkz.setVisibility(View.GONE);
                    edit.setVisibility(View.GONE);





                } else {
                    next.setVisibility(View.GONE);
                    goBack2.setVisibility(View.VISIBLE);
                    tv_submit_xkz.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);

                }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





        xkzxxPresenter.loadBaseFormData(certcode,status,XKZXXActivity.this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XKZXXActivity.this.finish();
            }
        });
        tv_submit_xkz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialManageFragment.getMaterials(certcode, new FileUploadCallBack() {//材料列表xml后去的回调
                    @Override
                    public void onResponse(String materialsXml) {
                        XKZXXActivity.this.materialsXML = materialsXml;
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("materials error--->" + e.getMessage());
//                        isAllXmlNoException = false;
                        showToast("获取材料信息异常！");
                    }
                });
                zzxxFragment.getBaseForm(new BaseFormCallBack() {
                    @Override
                    public void onResponse(String formsXml, JSONArray dataIdArray) {
                        XKZXXActivity.this.formsXML = formsXml;


                        if (myBusConstant == BusConstant.CHECK_FORM_WRONG) {
                            dismissProgressDialog();
                            showToast("表单填写有错误，请填写完整进行申报！");

                            return;
                        }

                        if (!CheckUploadUtil.checkAllUpload(certcode)) {
                            showToast("有必交材料未提交，请提交完全后再进行申报！");
                            return;
                        }

//                        if (isAllXmlNoException) {//所有获取xml都没有抛异常，才执行提交操作
                        Log.e("sendxmlxxxxxxx",formsXml);
                            xkzxxPresenter.applySubmit(USERCODE,certcode,CERTNAME,formsXML,materialsXML,token,XKZXXActivity.this);
//                        } else {
//                            dismissProgressDialog();
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("forms error-->" + e.getMessage());
//                        isAllXmlNoException = false;
                        showToast("基本表单信息异常！");
                    }
                });


            }
        });
        titles = new ArrayList<>();
        titles.add("证照信息");
        titles.add("文件上传");
        materialManageFragment = new MaterialManageFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("certcode", certcode);
        bundle1.putString("status", status);
        materialManageFragment.setArguments(bundle1);

        zzxxFragment = new ZZXXFragment();
        Bundle bundle0 = new Bundle();
        bundle0.putString("certcode", certcode);
        bundle0.putString("status", status);
        zzxxFragment.setArguments(bundle0);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return zzxxFragment;

                    default:
                        return materialManageFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "证照信息";
                    default:
                        return "文件上传";
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置自定义标题
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //设置自定义的标题
            tabLayout.getTabAt(i).setCustomView(getTabView(i));
        }


    }
    @Override
    public void showIsEdit(boolean isEdit) {
        edit.setText(isEdit ? "完成" : "编辑");
        materialManageFragment.showIsEdit(isEdit);
//        presenter.setAttsEdit(isEdit);
//        mAdapter.notifyDataSetChanged();
//
//        //是否编辑控制添加按钮的显示与隐藏
//        if (isEdit) {
//            menuFab.hideMenu(true);
//            showAutoDelete(true);
//        } else {
//            showAutoDelete(false);
//            menuFab.showMenu(true);
//        }
    }
    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_declare_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(titles.get(position));
/*        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        img_title.setImageResource(R.mipmap.declare_pass);*/
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xkzxx;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
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
//        if (!TextUtils.isEmpty(certcode)) {
//            if (null == atts) {
//                atts = new ArrayList<>();
//                Constants.materialXkz.put(certcode, atts);
//            }
//        }
//        List<CertInfoBean.CertfileBean> certfile = returnValue.getCertfile();
//        if (certfile != null && certfile.size() != 0) {
//            for (int i = 0; i < certfile.size(); i++) {
//                atts .add( new ATTBean(certfile.get(i).getFileid(),certfile.get(i).getCERTFILENAME(),certfile.get(i).getATTRACHPATH(),""));
//            }
//            Constants.materialXkz.put(certcode,atts);
//
//
//        }
//
        Constants.XKZshili1 = returnValue.getCertinfo().getEXAMPLEPATH1();
        Constants.XKZshili2 = returnValue.getCertinfo().getEXAMPLEPATH2();


    }

    @Override
    public void submitSuccess() {
        XKZXXActivity.this.finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusConstant busConstant) {
        switch (busConstant) {
            case GOTOBASEFORM://接收基本表单发过来的切换信息
                viewPager.setCurrentItem(0);
                break;
            case UPDATE://滑动通知，更新文件、领证方式tab出图标状态

                if (CheckUploadUtil.checkAllUpload(certcode)) {
                    title_file_status.setImageResource(R.drawable.tj_right_yes);

                } else {
                    title_form_staus.setImageResource(R.drawable.tj_right_no);
                    tv_submit_xkz.setBackgroundResource(R.drawable.gray_btn_gone_shape);
                }



                break;
            case CHECK_FORM_WRONG://表单信息错误，更新表单tab出图标为错误的图标
                KLog.e("CHECK_FORM_WRONG基本表单信息错误");
                title_form_staus.setImageResource(R.drawable.tj_right_no);
                myBusConstant = BusConstant.CHECK_FORM_WRONG;
                tv_submit_xkz.setBackgroundResource(R.drawable.gray_btn_gone_shape);
                break;
            case CHECK_FORM_RIGHT://表单信息正确，更新tab处图标为正确图标
                KLog.e("CHECK_FORM_RIGHT");
                title_form_staus.setImageResource(R.drawable.tj_right_yes);
                myBusConstant = BusConstant.CHECK_FORM_RIGHT;
                if (CheckUploadUtil.checkAllUpload(certcode)) {
                    tv_submit_xkz.setBackgroundResource(R.drawable.select_greent_btn);

                }

                break;
            case FAILED_TO_SAVE_BASE_FORM://保存基本表单失败
                dismissProgressDialog();
                break;
        }
    }
}
