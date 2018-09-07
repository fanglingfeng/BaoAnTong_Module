package com.tjsoft.webhall.ui.dzzzk;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.ZZDATABean;
import com.tjsoft.webhall.imp.ChakanCallback;

import net.liang.appbaselibrary.base.BindingViewHolder;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerAdapter;

import java.util.List;


/**
 * Created by lenovo on 2017/5/2.
 */

public class FileAdapter
        extends BaseRecyclerAdapter<ApplyBean>
{
    private Context mContext;
    private ChakanCallback chakanCallback;
    private ZZDATABean zzdataBean;
    private boolean isfirst;

    public FileAdapter(Context context, RecyclerView recyclerView, List<ApplyBean> data, ChakanCallback chakanCallback, ZZDATABean zzdataBean) {

        super(context, recyclerView, R.layout.item_file, data);
        mContext = context;

        this.chakanCallback = chakanCallback;
        this.zzdataBean = zzdataBean;

    }

    @Override
    protected void convert(final BindingViewHolder helper, final ApplyBean item) {

        ViewDataBinding binding            = helper.getBinding();
        TextView        tvFileName         = helper.getView(R.id.tv_filename);
        LinearLayout    ll_choose_material = helper.getView(R.id.ll_choose_material);
        TextView        tvChoose           = helper.getView(R.id.tv_choose_material);
        final TextView  tv_yixuanze        = helper.getView(R.id.tv_yixuanze);
        tvFileName.setText(item.getCLMC());
        ll_choose_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chakanCallback.choose(item,zzdataBean,helper.getPosition());
            }
        });
////        if (!isfirst) {
//            tv_yixuanze.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    chakanCallback.choose(item,zzdataBean,helper.getPosition());
//                }
//            });
////        }
        if (item.isIsselected()) {
            ll_choose_material.setVisibility(View.GONE);
            tv_yixuanze.setVisibility(View.VISIBLE);

        } else {
            ll_choose_material.setVisibility(View.VISIBLE);
            tv_yixuanze.setVisibility(View.GONE);
        }


        binding.executePendingBindings();

    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater,
                                                          layoutResId,
                                                          parent,
                                                          false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(net.liang.appbaselibrary.R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }

    public void setisFirst(boolean isfirst) {

        this.isfirst = isfirst;
    }
}
