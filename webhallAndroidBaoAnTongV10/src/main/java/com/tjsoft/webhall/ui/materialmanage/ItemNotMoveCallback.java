package com.tjsoft.webhall.ui.materialmanage;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;

/**
 * Created by lainghuiyong@outlook.com on 2017/2/4.
 * 重写功能，最后一个位置(从0开始)禁止被排序、禁止拖动
 */

public class ItemNotMoveCallback extends ItemDragAndSwipeCallback {

    public ItemNotMoveCallback(BaseItemDraggableAdapter adapter) {
        super(adapter);
    }

    /**
     * 禁止notMovePosition位置被排序
     * */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        MaterialManageActivity.ItemDragAdapter adapter = (MaterialManageActivity.ItemDragAdapter) recyclerView.getAdapter();
        int targetPosition = target.getLayoutPosition();
        if (targetPosition == (adapter.getData().size())){
            return false;
        }
        return super.onMove(recyclerView, source, target);
    }

    /**
     * 禁止notMovePosition位置被移动
     * */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        viewHolder.getAdapterPosition();
        BaseQuickAdapter adapter = (BaseQuickAdapter) recyclerView.getAdapter();

        int position = viewHolder.getLayoutPosition();

        if (position == (adapter.getData().size())){
            return makeMovementFlags(0, 0);
        }else {
            return super.getMovementFlags(recyclerView, viewHolder);
        }
    }
}
