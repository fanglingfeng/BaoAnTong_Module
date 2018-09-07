package com.tjsoft.webhall.ui.work;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.entity.CompanyBean;
import com.tjsoft.webhall.imp.CompanyCallback;


import net.liang.appbaselibrary.base.BindingViewHolder;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerAdapter;

import java.util.List;

/**
 * Created by lenovo on 2017/5/2.
 */

public class CompanyAdapter extends BaseRecyclerAdapter<CompanyBean> {
    private RecyclerView recyclerView;
    private CompanyCallback companyCallback;
    private boolean isList;

    public CompanyAdapter(Context context, RecyclerView recyclerView, List<CompanyBean> data,boolean isList, CompanyCallback companyCallback) {
        super(context, recyclerView, R.layout.item_company, data);
        this.recyclerView = recyclerView;
        this.companyCallback = companyCallback;
        this.isList = isList;
    }


    @Override
    protected void convert(final BindingViewHolder helper, final CompanyBean item) {
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyCallback.onClick(isList,item);
            }
        });


        TextView tvCompanyName = helper.getView(R.id.tv_company_name);
        TextView tvCompanyIsreal = helper.getView(R.id.tv_company_isreal);
        TextView tv_is_faren_auth = helper.getView(R.id.tv_is_faren_auth);
        TextView tvTYSHXYDM = helper.getView(R.id.tv_tyshxydm);
        TextView tvFRDBXM = helper.getView(R.id.tv_frdbxm);
        RelativeLayout rvCompanyDelete = helper.getView(R.id.rl_delete_company);
        RelativeLayout rlToAuth = helper.getView(R.id.rl_to_auth);
        TextView rvFRSFZH = helper.getView(R.id.tv_frsfzh);
        RelativeLayout rlNext = helper.getView(R.id.rl_next);
        TextView tv_company_default = helper.getView(R.id.tv_company_default);
        RelativeLayout rl_to_auth_faren = helper.getView(R.id.rl_to_auth_faren);

        tvCompanyName.setText(item.getINC_NAME());
        if (TextUtils.equals(item.getISREALNAME(), "1")) {
            tvCompanyIsreal.setText("(已认证)");




            rlToAuth.setVisibility(View.GONE);

            tvCompanyIsreal.setTextColor(mContext.getResources().getColor(R.color.tj_btn_green));
            if (TextUtils.equals(item.getDEPUTY_ISREALNAME(), "0")) {
                rl_to_auth_faren.setVisibility(View.VISIBLE);
                tv_is_faren_auth.setText("(未认证)");
                tv_is_faren_auth.setTextColor(mContext.getResources().getColor(R.color.tj_orange));

            } else {
                tv_is_faren_auth.setText("(已认证)");
                tv_is_faren_auth.setTextColor(mContext.getResources().getColor(R.color.tj_btn_green));

                rl_to_auth_faren.setVisibility(View.GONE);
            }


        } else {
            rl_to_auth_faren.setVisibility(View.GONE);
            rlToAuth.setVisibility(View.VISIBLE);
            tvCompanyIsreal.setText("(未认证)");
            tv_is_faren_auth.setText("(未认证)");
            tv_is_faren_auth.setTextColor(mContext.getResources().getColor(R.color.tj_orange));

            tvCompanyIsreal.setTextColor(mContext.getResources().getColor(R.color.tj_orange));
        }
        if (TextUtils.equals(item.getISDEFAULT(), "1")) {
            tv_company_default.setVisibility(View.VISIBLE);
        } else {
            tv_company_default.setVisibility(View.GONE);
        }
        tvTYSHXYDM.setText("统一社会信用代码：" + item.getINC_TYSHXYDM());
        tvFRDBXM.setText("法人代表姓名：" + item.getINC_DEPUTY());
        rvFRSFZH.setText("法人身份证号：" + item.getINC_PID());
        rvCompanyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyCallback.deleteCompany(item.getINC_EXT_ID());
            }
        });

        if (isList) {
            rvCompanyDelete.setVisibility(View.VISIBLE);
            rlNext.setVisibility(View.GONE);
        } else {
            rvCompanyDelete.setVisibility(View.GONE);
            tv_company_default.setVisibility(View.GONE);
            rlNext.setVisibility(View.VISIBLE);

        }


        rlToAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyCallback.toAuth(item);
            }
        });

        rl_to_auth_faren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyCallback.toAuthFaren(item);
            }
        });

    }
}
