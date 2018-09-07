package com.tjsoft.webhall.ui.dzzzk;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.FileUtil;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.ChakanResponsebean;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.ZZDATABean;
import com.tjsoft.webhall.imp.ChakanCallback;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;

import net.liang.appbaselibrary.base.BaseAppCompatActivity;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.widget.dialog.DialogHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/*
 *  @项目名：  baoan 
 *  @包名：    com.tjsoft.webhall.ui.dzzzk
 *  @文件名:   DZZZKActivity
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 15:41
 *  @描述：    TODO
 */

public class DZZZKActivity
        extends BaseAppCompatActivity
        implements DZZZKContract.View


{
    RelativeLayout          back;
    RecyclerView            rvDzzzk;
    DZZZKContract.Presenter presenter;
    private String               SXMC;
    private String               SXBM;
    private ArrayList<ApplyBean> mApplyBeans;
    private DZZZKAdapter         adapter;
    private DialogHelper         dialogHelper;
    public static Map<String, List<ApplyBean>> applybeans = new LinkedHashMap<String, List<ApplyBean>>();// 上传文件缓存

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dzzzk;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void init() {
        super.init();

        for (int i = 0; i < HistoreShare_v2.bigFileDate.size(); i++) {
            HistoreShare_v2.bigFileDate.get(i)
                                       .setIsselected(false);
        }


        dialogHelper = new DialogHelper(this);

        SXMC = getIntent().getStringExtra("SXMC");
        SXBM = getIntent().getStringExtra("SXBM");
        //        mApplyBeans = getIntent().getParcelableArrayListExtra("dzzzklist");

        back = (RelativeLayout) findViewById(R.id.back);
        rvDzzzk = (RecyclerView) findViewById(R.id.rv_dzzzk);

        presenter = new DZZZKPresenter(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DZZZKActivity.this.finish();
            }
        });

        rvDzzzk.setLayoutManager(new LinearLayoutManager(this));
        rvDzzzk.setHasFixedSize(true);
        rvDzzzk.setNestedScrollingEnabled(false);
        rvDzzzk.setFocusable(false);
        adapter = new DZZZKAdapter(this, rvDzzzk, null, new ChakanCallback() {
            @Override
            public void chakan(ZZDATABean item) {

                presenter.chakan(Constants.user.getTOKEN(),
                                 item.getAUTHCODE(),
                                 item.getNAME(),
                                 DZZZKActivity.this,
                                 null,
                                 0);

            }

            @Override
            public void choose(ApplyBean applyBean, ZZDATABean item, int position) {

                presenter.chakan(Constants.user.getTOKEN(),
                                 item.getAUTHCODE(),
                                 item.getNAME(),
                                 DZZZKActivity.this,
                                 applyBean,
                                 position);


            }


        });
        rvDzzzk.setAdapter(adapter);
        /*"token":"12345678",
"ZJHM":"1234657898,5555555,444444,1111",
"SXBM":"33622600107580123214440306",
"SXMC":"就业困难人员申请灵活就业补贴"*/
        //           33622600107580123214440306   就业困难人员申请灵活就业补贴

        //        presenter.getDZZZKList(Constants.user.getTOKEN(),Constants.user.getCODE(),SXBM,SXMC,DZZZKActivity.this);
        String zjhm = Constants.user.getCODE();
        GloabDelegete gloabDelegete = Constants.getInstance()
                                               .getGloabDelegete();
        TransportEntity transportEntity = gloabDelegete.getUserInfo();
        if (!TextUtils.isEmpty(transportEntity.getTYSHXYDM())) {
            zjhm = zjhm + "," + transportEntity.getTYSHXYDM();
        }
        if (!TextUtils.isEmpty(transportEntity.getINC_ZZJGDM())) {
            zjhm = zjhm + "," + transportEntity.getINC_ZZJGDM();
        }
        //        presenter.getDZZZKList(Constants.user.getTOKEN(),
        //                               "445222199903040633",
        //                               "33622600107580123214440306",
        //                               "就业困难人员申请灵活就业补贴",
        //                               DZZZKActivity.this);
        presenter.getDZZZKList(Constants.user.getTOKEN(), zjhm, SXBM, SXMC, DZZZKActivity.this);


        //        rvDzzzk.addOnItemTouchListener(new OnItemChildClickListener() {
        //            @Override
        //            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        //                if (view.getId() == R.id.rl_chakan) {
        //                    presenter.chakan(Constants.user.getTOKEN(),((ZZDATABean)adapter.getData().get(position)).getAUTHCODE(),DZZZKActivity.this,null);
        //                }
        //            }
        //        });

    }

    @Override
    public void showDZZZKList(List<ZZDATABean> value) {

        adapter.showList(value);


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
                           String authcode)
    {

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
            Intent intent = FileUtil.openFile(absolutePath, DZZZKActivity.this);
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
        adapter.setIsfirst(false);
        adapter.notifyDataSetChanged();
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
}
