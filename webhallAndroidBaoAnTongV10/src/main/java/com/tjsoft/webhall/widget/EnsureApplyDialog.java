package com.tjsoft.webhall.widget;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.DialogUtil;


/**
 * Created by gersy on 2017/6/21.
 */

public class EnsureApplyDialog extends BaseAlertDialog {

    public EnsureConfirm confirm;
    private TextView tvConfirm;
    private CheckBox cbConfirm;


    public EnsureApplyDialog(Context context , EnsureConfirm confirm) {
        super(context);
        this.confirm = confirm;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_ensure_apply;
    }

    @Override
    protected void initView() {
        tvConfirm = (TextView) mWindow.findViewById(R.id.tv_confirm);
        cbConfirm = (CheckBox) mWindow.findViewById(R.id.cb_ensure);

        cbConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvConfirm.setBackgroundResource(R.drawable.bg_btn);
                } else {
                    tvConfirm.setBackgroundResource(R.drawable.tj_gray_btn_shape);

                }
            }
        });


        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbConfirm.isChecked()) {
                    confirm.onConfirm();
                    dismiss();
                } else {
                    DialogUtil.showToast(mContext,"请仔细阅读以上条款并且勾选同意");
                }

            }
        });
    }

    public void setContent(String content) {

    }

    @Override
    protected void initListener() {

    }

    public interface EnsureConfirm {
        void onConfirm();
    }
}
