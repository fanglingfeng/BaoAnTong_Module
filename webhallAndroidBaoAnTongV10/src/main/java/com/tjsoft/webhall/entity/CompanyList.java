package com.tjsoft.webhall.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
public class CompanyList implements Serializable {

    public List<CompanyBean> getItems() {
        return Items;
    }

    public void setItems(List<CompanyBean> items) {
        Items = items;
    }

    private List<CompanyBean> Items;
}
