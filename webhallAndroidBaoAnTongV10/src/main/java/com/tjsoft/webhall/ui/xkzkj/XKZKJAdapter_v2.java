package com.tjsoft.webhall.ui.xkzkj;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.entity.XKZKJBean;
import com.tjsoft.webhall.imp.ChakanCallback;
import com.tjsoft.webhall.imp.LoadmoreCallback;

import net.liang.appbaselibrary.base.BindingViewHolder;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerAdapter;

import java.util.List;


/**
 * Created by lenovo on 2017/5/2.
 */

public class XKZKJAdapter_v2
        extends BaseRecyclerAdapter<XKZKJBean>
{
    private Context mContext;
    private LoadmoreCallback loadmoreCallback;
    private boolean isfirst = true;
    boolean isloadmore = true;
    boolean showloadmore = true;
    RecyclerView.LayoutManager manager;
    public XKZKJAdapter_v2(Context context, RecyclerView recyclerView, List<XKZKJBean> data , LoadmoreCallback loadmoreCallback) {
        super(context, recyclerView, R.layout.item_xkz_v2, data);
        this.loadmoreCallback = loadmoreCallback;
        mContext = context;
    }
    public void setIsfirst(boolean isfirst){
        isfirst = isfirst;
    }
    @Override
    protected void convert(BindingViewHolder helper, final XKZKJBean item) {

        ViewDataBinding binding = helper.getBinding();
        ////        helper.getView(R.id.title_ll).setOnClickListener(new View.OnClickListener() {
        ////            @Override
        ////            public void onClick(View v) {
        ////                item.setHasMenueOpen(!item.isHasMenueOpen());
        ////                notifyDataSetChanged();
        ////            }
        ////        });
        TextView       tv_upload_status     = helper.getView(R.id.tv_upload_status);
        TextView       tv_xkz_title  = helper.getView(R.id.tv_xkz_title);
        TextView      tv_xkz_code  = helper.getView(R.id.tv_xkz_code);
        final TextView      tv_loadmore  = helper.getView(R.id.tv_loadmore);
        helper.addOnClickListener(R.id.tv_loadmore);
        if (TextUtils.equals(item.getSTATUS(), "0")) {
            tv_upload_status.setText("未上传");
            tv_upload_status.setBackgroundColor(mContext.getResources().getColor(R.color.edit_hint_color));
        } else {
            tv_upload_status.setText("已上传");
            tv_upload_status.setBackgroundColor(mContext.getResources().getColor(R.color.tj_btn_green));

        }

        if (helper.getAdapterPosition()+1 == getItemCount()&&showloadmore) {
            tv_loadmore.setVisibility(View.VISIBLE);
        } else {
            tv_loadmore.setVisibility(View.GONE);
        }
        tv_loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadmoreCallback.loadmore();
                tv_loadmore.setVisibility(View.GONE);
            }
        });

        tv_xkz_code.setText("证照编号："+item.getCERTCODE());
        tv_xkz_title.setText(item.getCERTNAME());

        binding.executePendingBindings();

    }


    private int getTheBiggestNumber(int[] numbers) {
        int tmp = -1;
        if (numbers == null || numbers.length == 0) {
            return tmp;
        }
        for (int num : numbers) {
            if (num > tmp) {
                tmp = num;
            }
        }
        return tmp;
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

    public void showLoadMore(boolean b) {
        showloadmore = b;
    }
}
