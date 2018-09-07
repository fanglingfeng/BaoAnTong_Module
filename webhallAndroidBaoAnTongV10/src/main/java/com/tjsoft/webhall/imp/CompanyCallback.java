package com.tjsoft.webhall.imp;

import com.tjsoft.webhall.entity.CompanyBean;

public interface CompanyCallback {

    public void deleteCompany(String id);


    void selectCompany(CompanyBean item, int position,boolean isCheck);

    void toAuth(CompanyBean item);

    void onClick(boolean isList,CompanyBean companyBean);

    void toAuthFaren(CompanyBean item);
}
