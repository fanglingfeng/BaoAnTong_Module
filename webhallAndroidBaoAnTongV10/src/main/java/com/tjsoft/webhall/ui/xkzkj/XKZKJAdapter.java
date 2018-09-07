package com.tjsoft.webhall.ui.xkzkj;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.entity.XKZKJBean;
import com.tjsoft.webhall.entity.ZZDATABean;
import com.tjsoft.webhall.imp.ChakanCallback;
import com.tjsoft.webhall.ui.dzzzk.DZZZKActivity;
import com.tjsoft.webhall.ui.dzzzk.FileAdapter;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;

import net.liang.appbaselibrary.base.BindingViewHolder;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerAdapter;

import java.util.List;


/**
 * Created by lenovo on 2017/5/2.
 */

public class XKZKJAdapter
        extends BaseRecyclerAdapter<XKZKJBean>
{
    private Context mContext;
    private ChakanCallback chakanCallback;
    private boolean isfirst = true;
    public XKZKJAdapter(Context context, RecyclerView recyclerView, List<XKZKJBean> data , ChakanCallback chakanCallBack) {
        super(context, recyclerView, R.layout.item_xkz, data);
        this.chakanCallback = chakanCallBack;
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

        if (TextUtils.equals(item.getSTATUS(), "0")) {
            tv_upload_status.setText("未上传");
            tv_upload_status.setBackgroundColor(mContext.getResources().getColor(R.color.edit_hint_color));
        } else {
            tv_upload_status.setText("已上传");
            tv_upload_status.setBackgroundColor(mContext.getResources().getColor(R.color.tj_btn_green));

        }

        tv_xkz_code.setText("证照编号："+item.getCERTCODE());
        tv_xkz_title.setText(item.getCERTNAME());

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

}
