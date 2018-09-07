package com.tjsoft.webhall.ui.materialmanage;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lenovo on 2017/1/24.
 */

public class MateralManagerAdapter extends BaseItemDraggableAdapter {

    public MateralManagerAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Object o) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
