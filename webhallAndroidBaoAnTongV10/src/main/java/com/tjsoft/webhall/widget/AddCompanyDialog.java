package com.tjsoft.webhall.widget;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.entity.CompanyBean;


/**
 * Created by gersy on 2017/6/21.
 */

public class AddCompanyDialog extends BaseAlertDialog {

    private final CompanyBean companyBean;
    public Confirm confirm;
    private TextView tvConfirm;
    private EditText etQymc;
    private EditText etTyxydm;
    private EditText etFrdb;
    private EditText etFrsfzhm;


    public AddCompanyDialog(Context context , CompanyBean companyBean, Confirm confirm) {
        super(context);
        this.confirm = confirm;
        this.companyBean = companyBean;
        if (companyBean != null) {
            etQymc.setText(companyBean.getINC_NAME());
            etTyxydm.setText(companyBean.getINC_TYSHXYDM());
            etFrdb.setText(companyBean.getINC_DEPUTY());
            etFrsfzhm.setText(companyBean.getINC_PID());

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_add_company;
    }

    @Override
    protected void initView() {
        tvConfirm = (TextView) mWindow.findViewById(R.id.tv_confirm);
        etQymc = (EditText) mWindow.findViewById(R.id.et_qymc);
        etTyxydm = (EditText) mWindow.findViewById(R.id.et_tyxydm);
        etFrdb = (EditText) mWindow.findViewById(R.id.et_frdb);
        etFrsfzhm = (EditText) mWindow.findViewById(R.id.et_frsfzhm);



        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm.onConfirm(etQymc.getText().toString(),etTyxydm.getText().toString(),etFrdb.getText().toString(),etFrsfzhm.getText().toString());
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
        void onConfirm(String qymc,String tyxydm,String frdb,String frsfzhm );
    }
}
