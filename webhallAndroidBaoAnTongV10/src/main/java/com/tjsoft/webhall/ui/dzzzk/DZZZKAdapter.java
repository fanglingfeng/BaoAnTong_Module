package com.tjsoft.webhall.ui.dzzzk;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.entity.ZZDATABean;
import com.tjsoft.webhall.imp.ChakanCallback;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;

import net.liang.appbaselibrary.base.BindingViewHolder;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerAdapter;

import java.util.List;


/**
 * Created by lenovo on 2017/5/2.
 */

public class DZZZKAdapter
        extends BaseRecyclerAdapter<ZZDATABean>
{
    private Context mContext;
    private ChakanCallback chakanCallback;
    private boolean isfirst = true;
    public DZZZKAdapter(Context context, RecyclerView recyclerView, List<ZZDATABean> data ,ChakanCallback chakanCallBack) {
        super(context, recyclerView, R.layout.item_dzzzk, data);
        this.chakanCallback = chakanCallBack;
        mContext = context;
    }
    public void setIsfirst(boolean isfirst){
        isfirst = isfirst;
    }
    @Override
    protected void convert(BindingViewHolder helper, final ZZDATABean item) {

        ViewDataBinding binding = helper.getBinding();
        ////        helper.getView(R.id.title_ll).setOnClickListener(new View.OnClickListener() {
        ////            @Override
        ////            public void onClick(View v) {
        ////                item.setHasMenueOpen(!item.isHasMenueOpen());
        ////                notifyDataSetChanged();
        ////            }
        ////        });
        TextView       title     = helper.getView(R.id.title_value_tv);
        TextView       tvIdcode  = helper.getView(R.id.tv_idcode);
        ImageView      ivStatus  = helper.getView(R.id.status_iv);
        LinearLayout   llTitle   = helper.getView(R.id.title_ll);
        LinearLayout   llContent = helper.getView(R.id.ll_content);
        LinearLayout rlChakan  = helper.getView(R.id.rl_chakan);
        title.setText(item.getNAME());
        tvIdcode.setText("证照编码:" + item.getLICENSECODE());
        if (!item.isIsopen()) {
            ivStatus.setBackgroundResource(R.drawable.close);
            llContent.setVisibility(View.GONE);
        } else {
            ivStatus.setBackgroundResource(R.drawable.open);
            llContent.setVisibility(View.VISIBLE);
        }
        RecyclerView recyclerView = helper.getView(R.id.rv_filelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

        FileAdapter fileAdapter = new FileAdapter(mContext, recyclerView, null,chakanCallback,item);
        recyclerView.setAdapter(fileAdapter);


        if (DZZZKActivity.applybeans.get(item.getAUTHCODE()) == null) {
            fileAdapter.setisFirst(isfirst);
            fileAdapter.showList(HistoreShare_v2.bigFileDate);
        } else {
            fileAdapter.setisFirst(isfirst);
            fileAdapter.showList(DZZZKActivity.applybeans.get(item.getAUTHCODE()));
        }




        llTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setIsopen(!item.isIsopen());
                notifyDataSetChanged();


            }
        });
        rlChakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chakanCallback.chakan(item);
            }
        });
//        recyclerView.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemChildClickListener() {
//            @Override
//            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                ApplyBean applyBean = (ApplyBean) adapter.getItem(position);
//                if (view.getId() ==R.id.tv_choose_material) {
//                    chakanCallback.choose(applyBean,item,position);
//                }
//            }
//        });

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
