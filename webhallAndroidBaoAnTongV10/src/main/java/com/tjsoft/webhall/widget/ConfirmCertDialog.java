package com.tjsoft.webhall.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;


/**
 * Created by gersy on 2017/6/21.
 */

public class ConfirmCertDialog extends BaseAlertDialog {

    public Confirm confirm;
    TextView tvName;
    TextView tvCertId;
    TextView tvConfirm;
    String name;
    String pid;

    public ConfirmCertDialog(Context context,String name,String pid, Confirm confirm) {
        super(context);
        this.confirm = confirm;
        this.name  = name;
        this.pid = pid;
        tvName.setText("姓名："+name);
        tvCertId.setText("身份证："+pid);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_confirm_cert;
    }

    @Override
    protected void initView() {
        tvConfirm = (TextView) mWindow.findViewById(R.id.tv_confirm);
        tvCertId = (TextView) mWindow.findViewById(R.id.tv_certid);
        tvName = (TextView) mWindow.findViewById(R.id.tv_name);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm.onConfirm();
                dismiss();
            }
        });
    }

    public void setContent(String content) {

    }

    @Override
    protected void initListener() {

    }

    public interface Confirm {
        void onConfirm();
    }
}
