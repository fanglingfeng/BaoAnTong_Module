package com.tjsoft.webhall.ui.xkzkj;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bean.BaseResponseReturnValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.FileUtil;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.ChakanResponsebean;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.XKZKJBean;
import com.tjsoft.webhall.entity.ZZDATABean;
import com.tjsoft.webhall.imp.ChakanCallback;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.imp.LoadmoreCallback;
import com.tjsoft.webhall.lib.SearchEditText;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.XKZXXActivity;

import net.liang.appbaselibrary.base.BaseAppCompatActivity;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerAdapter;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerViewActivity;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.widget.dialog.DialogHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


/*
 *  @项目名：  baoan
 *  @包名：    com.tjsoft.webhall.ui.dzzzk
 *  @文件名:   DZZZKActivity
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 15:41
 *  @描述：    TODO
 */

public class XKZKJActivity_v2
        extends BaseAppCompatActivity
        implements XKZKJContract.View


{
    RelativeLayout back;
    RecyclerView layout_recyclerview;
    SwipeRefreshLayout swiperefresh;
    ImageView iv_search;

    Button btnSearch;
    SearchEditText textSearch;
    XKZKJContract.Presenter presenter;
    private String SXMC;
    private String SXBM;
    private int pageno = 1;
    private ArrayList<ApplyBean> mApplyBeans;
    private XKZKJAdapter_v2 adapter;
    private DialogHelper dialogHelper;
    public static Map<String, List<ApplyBean>> applybeans = new LinkedHashMap<String, List<ApplyBean>>();// 上传文件缓存
    private TransportEntity transportEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xkzkj;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void init() {
        super.init();


        dialogHelper = new DialogHelper(this);


        back = (RelativeLayout) findViewById(R.id.back);
        layout_recyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swiperefresh.setEnabled(false);
        iv_search = (ImageView) findViewById(R.id.iv_search);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        textSearch = (SearchEditText) findViewById(R.id.textSearch);

        presenter = new XKZKJPresenter(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XKZKJActivity_v2.this.finish();
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(XKZKJActivity_v2.this, XKZKJActivity_search_v2.class));
            }
        });
        layout_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        layout_recyclerview.setHasFixedSize(true);
        layout_recyclerview.setNestedScrollingEnabled(false);
        layout_recyclerview.setFocusable(false);
        adapter = new XKZKJAdapter_v2(this, layout_recyclerview, null, new LoadmoreCallback() {


            @Override
            public void loadmore() {

            }
        });

        layout_recyclerview.setAdapter(adapter);
        /*"token":"12345678",
"ZJHM":"1234657898,5555555,444444,1111",
"SXBM":"33622600107580123214440306",
"SXMC":"就业困难人员申请灵活就业补贴"*/
        //           33622600107580123214440306   就业困难人员申请灵活就业补贴

        //        presenter.getDZZZKList(Constants.user.getTOKEN(),Constants.user.getCODE(),SXBM,SXMC,DZZZKActivity.this);
        String zjhm = Constants.user.getCODE();
        GloabDelegete gloabDelegete = Constants.getInstance()
                .getGloabDelegete();
        transportEntity = gloabDelegete.getUserInfo();

//        presenter.getDZZZKList(Constants.user.getTOKEN(), transportEntity.getINC_ZZJGDM(), XKZKJActivity.this,"");
        layout_recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                XKZKJBean xkzkjBean = (XKZKJBean) adapter.getData().get(position);
                Intent intent = new Intent(XKZKJActivity_v2.this, XKZXXActivity.class);
                intent.putExtra("certcode", xkzkjBean.getCERTCODE());
                intent.putExtra("CERTNAME", xkzkjBean.getCERTNAME());
                intent.putExtra("USERCODE", transportEntity.getINC_ZZJGDM());
                intent.putExtra("status", xkzkjBean.getSTATUS());
                intent.putExtra("token", transportEntity.getToken());
                intent.putExtra("name", xkzkjBean.getCERTNAME());
                Constants.XKZshili1 = xkzkjBean.getEXAMPLEPATH1();
                Constants.XKZshili2 = xkzkjBean.getEXAMPLEPATH2();
                startActivity(intent);


            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if (view.getId() == R.id.tv_loadmore) {
                    presenter.getDZZZKList_v2(Constants.user.getTOKEN(), transportEntity.getINC_ZZJGDM(), XKZKJActivity_v2.this, "", ++pageno);

                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getDZZZKList_v2(Constants.user.getTOKEN(), transportEntity.getINC_ZZJGDM(), XKZKJActivity_v2.this, textSearch.getText().toString(), pageno);
            }
        });

    }

    @Override
    public void showDZZZKList(List<XKZKJBean> value) {

    }

    @Override
    public void showDZZZKList(List<XKZKJBean> value,int pageno) {
        if (pageno != 1) {
            adapter.addData(value);
        } else {
            adapter.showList(value);
        }
        if (value.size() < 10) {
            adapter.showLoadMore(false);
        } else {
            adapter.showLoadMore(true);
        }
//        adapter.showList(value);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        pageno = 1;
        presenter.getDZZZKList_v2(Constants.user.getTOKEN(), transportEntity.getINC_ZZJGDM(), XKZKJActivity_v2.this, "", 1);

    }

    @Override
    public void showUrl(String oneoffurl) {
        Uri uri = Uri.parse(oneoffurl);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);


    }

    @Override
    public void showChakan(ChakanResponsebean returnValue,
                           ApplyBean applyBean,
                           int position,
                           String authcode) {

        presenter.downloadUrl(returnValue, applyBean, position, authcode);


    }

    @Override
    public void showSetPermissionDialog(String permissionType) {

        dialogHelper.alert("帮助",
                "当前应用缺少" + permissionType + "权限。\n请点击“设置”-“权限”-打开所需权限。\n最后点击两次后退按钮即可返回。",
                "设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                },
                "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    @Override
    public void showPreviewLayout(String absolutePath, ApplyBean applyBean) {
        if (applyBean == null) {
            Intent intent = FileUtil.openFile(absolutePath, XKZKJActivity_v2.this);
            startActivity(intent);
        } else {
            //
            //            Intent intent = new Intent();
            //            intent.putExtra("applyBean",applyBean);
            //            intent.putExtra("absolutePath",absolutePath);
            //            setResult(RESULT_OK,intent);


        }


    }

    @Override
    public void refreshList() {
//        adapter.setIsfirst(false);
//        adapter.notifyDataSetChanged();
    }


    @Override
    public void showProgressDialog(String msg) {
        dialogHelper.showProgressDialog(msg);
    }

    /**
     * 隐藏进度条
     */
    @Override
    public void dismissProgressDialog() {
        dialogHelper.dismissProgressDialog();
    }

//    @Override
//    public void onListSuccess(BaseResponseReturnValue<List<XKZKJBean>> xkzkjBeanBaseResponseReturnValue, int pageNo) {
//        adapter.showList(xkzkjBeanBaseResponseReturnValue.getReturnValue(), pageNo);
//        if (xkzkjBeanBaseResponseReturnValue.getReturnValue() == null || xkzkjBeanBaseResponseReturnValue.getReturnValue().size() <= 0) {
//            adapter.loadMoreEnd(true);
//        }
//    }

//    @Override
//    public BaseRecyclerAdapter addListAdapter() {
//        return new XKZKJAdapter(this, recyclerView, null, new ChakanCallback() {
//            @Override
//            public void chakan(ZZDATABean item) {
//
//                presenter.chakan(Constants.user.getTOKEN(),
//                        item.getAUTHCODE(),
//                        item.getNAME(),
//                        XKZKJActivity_v2.this,
//                        null,
//                        0);
//
//            }
//
//            @Override
//            public void choose(ApplyBean applyBean, ZZDATABean item, int position) {
//
//                presenter.chakan(Constants.user.getTOKEN(),
//                        item.getAUTHCODE(),
//                        item.getNAME(),
//                        XKZKJActivity_v2.this,
//                        applyBean,
//                        position);
//
//
//            }
//
//
//        });
//    }
//
//    @Override
//    public Observable<BaseResponseReturnValue<List<XKZKJBean>>> onListGetData(int pageNo) {
//        return         presenter.getDZZZKList(Constants.user.getTOKEN(), transportEntity.getINC_ZZJGDM(), XKZKJActivity_v2.this,"",pageNo);
//
//    }
}
