package com.tjsoft.webhall.ui.work.companylist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bean.BaseResponseReturnValue;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.AuthUtil;
import com.tjsoft.webhall.entity.AddCompanySendBean;
import com.tjsoft.webhall.entity.CompanyBean;
import com.tjsoft.webhall.entity.CompanyList;
import com.tjsoft.webhall.imp.CompanyCallback;
import com.tjsoft.webhall.ui.work.CompanyAdapter;
import com.tjsoft.webhall.widget.AddCompanyDialog;

import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerAdapter;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerViewActivity;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.widget.dialog.DialogHelper;

import io.reactivex.Observable;

public class CompanyListActivity extends BaseRecyclerViewActivity<BaseResponseReturnValue<CompanyList>> implements CompanyListContract.View {
    private String token;
    private String USERID;
    private String userPid;
    private CompanyListPresenter presenter;
    private RelativeLayout back;
    private LinearLayout ll_add_company;
    private DialogHelper dialogHelper;
    private CompanyBean companyBean;

    @Override
    public void init() {
        dialogHelper = new DialogHelper(this);

        back = (RelativeLayout) findViewById(R.id.back);
        ll_add_company = (LinearLayout) findViewById(R.id.ll_add_company);
        token = getIntent().getStringExtra("token");
        USERID = getIntent().getStringExtra("USERID");
        userPid = getIntent().getStringExtra("userPid");
        presenter = new CompanyListPresenter(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("isselectCompany","2");
                setResult(RESULT_OK, i);
                finish();
                CompanyListActivity.this.finish();
            }
        });
        ll_add_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCompanyDialog addCompanyDialog = new AddCompanyDialog(CompanyListActivity.this, null, new AddCompanyDialog.Confirm() {


                    @Override
                    public void onConfirm(String myqymc, String mytyxydm, String deputy, String myfrdbid) {
                        AddCompanySendBean addCompanySendBean = new AddCompanySendBean();
                        addCompanySendBean.setUSERID(USERID);
                        addCompanySendBean.setINC_NAME(myqymc);
                        addCompanySendBean.setINC_TYSHXYDM(mytyxydm);
                        addCompanySendBean.setINC_DEPUTY(deputy);
                        addCompanySendBean.setINC_PID(myfrdbid);
                        addCompanySendBean.setToken(token);
                        if (TextUtils.equals(userPid, myfrdbid)) {
                            addCompanySendBean.setDEPUTY_ISREALNAME("1");
                        } else {
                            addCompanySendBean.setDEPUTY_ISREALNAME("0");
                        }


                        presenter.addCompany(addCompanySendBean, CompanyListActivity.this);

                    }
                });
            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("isselectCompany","2");
        setResult(RESULT_OK, i);
        finish();
        CompanyListActivity.this.finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_list_layout;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onListSuccess(BaseResponseReturnValue<CompanyList> responseReturnValue, int pageNo) {
        adapter.showList(responseReturnValue.getReturnValue().getItems(), pageNo);
    }

    @Override
    public BaseRecyclerAdapter addListAdapter() {
        return new CompanyAdapter(CompanyListActivity.this, recyclerView, null, true, new CompanyCallback() {
            @Override
            public void deleteCompany(final String id) {

                new AlertDialog.Builder(CompanyListActivity.this)
                        .setMessage("是否删除该企业")
                        .setTitle("温馨提示")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface mdialog, int which) {
                                AddCompanySendBean sendBean = new AddCompanySendBean();
                                sendBean.setToken(token);
                                sendBean.setINC_EXT_ID(id);
                                presenter.deleteCompany(sendBean, CompanyListActivity.this);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
            }

            @Override
            public void selectCompany(CompanyBean item, int position, boolean isSelected) {

            }

            @Override
            public void toAuth(final CompanyBean item) {
                AddCompanyDialog addCompanyDialog = new AddCompanyDialog(CompanyListActivity.this, item, new AddCompanyDialog.Confirm() {


                    @Override
                    public void onConfirm(String myqymc, String mytyxydm, String myfrdb, String myfrsfzhm) {

                        AddCompanySendBean addCompanySendBean = new AddCompanySendBean();
                        addCompanySendBean.setINC_NAME(myqymc);
                        addCompanySendBean.setINC_DEPUTY(myfrdb);
                        addCompanySendBean.setINC_TYSHXYDM(mytyxydm);
                        addCompanySendBean.setINC_PID(myfrsfzhm);
                        addCompanySendBean.setINC_EXT_ID(item.getINC_EXT_ID());
                        addCompanySendBean.setToken(token);
                        if (TextUtils.equals(userPid, myfrsfzhm)) {
                            addCompanySendBean.setDEPUTY_ISREALNAME("1");
                        } else {
                            addCompanySendBean.setDEPUTY_ISREALNAME("0");
                        }
                        addCompanySendBean.setUSERID(USERID);
                        presenter.editCompany(addCompanySendBean, CompanyListActivity.this);

                    }
                });

            }

            @Override
            public void onClick(boolean isList, CompanyBean companyBean) {

                Intent i = new Intent();
                i.putExtra("isselectCompany","1");
                i.putExtra("selectCompanyResult", companyBean);
                setResult(RESULT_OK, i);
                finish();


            }

            @Override
            public void toAuthFaren(CompanyBean item) {
                //法人验证
                CompanyListActivity.this.companyBean = item;
                AuthUtil.startHuaXunFaceAuthFaren(CompanyListActivity.this, item.getINC_DEPUTY(), item.getINC_PID());


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == AuthUtil.HUAXUN_AUTH_FAREN) {
            String result = data.getStringExtra("result");

            if (TextUtils.equals(result, "success")) {
//                DialogUtil.showUIToast(mContext, "认证成功");
                AddCompanySendBean addCompanySendBean = new AddCompanySendBean();
                addCompanySendBean.setINC_NAME(companyBean.getINC_NAME());
                addCompanySendBean.setINC_DEPUTY(companyBean.getINC_DEPUTY());
                addCompanySendBean.setINC_TYSHXYDM(companyBean.getINC_TYSHXYDM());
                addCompanySendBean.setINC_PID(companyBean.getINC_PID());
                addCompanySendBean.setINC_EXT_ID(companyBean.getINC_EXT_ID());
                addCompanySendBean.setToken(token);

                    addCompanySendBean.setDEPUTY_ISREALNAME("1");

                addCompanySendBean.setUSERID(USERID);
                presenter.editCompany(addCompanySendBean, CompanyListActivity.this);

            } else {
//                DialogUtil.showUIToast(mContext, "认证失败");
            }


        }
    }

    @Override
    public Observable<BaseResponseReturnValue<CompanyList>> onListGetData(int pageNo) {
        return presenter.getCompanyList(token, USERID, CompanyListActivity.this);
    }

    @Override
    public void addCompanySuccess() {
        onRefresh();
    }

    @Override
    public void showProgressDialog(String msg) {
        dialogHelper.showProgressDialog(msg);

    }

    @Override
    public void dismissProgressDialog() {
        dialogHelper.dismissProgressDialog();

    }
}