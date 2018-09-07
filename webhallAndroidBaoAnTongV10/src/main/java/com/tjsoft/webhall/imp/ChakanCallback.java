package com.tjsoft.webhall.imp;

import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.ZZDATABean;

public interface ChakanCallback {

    void chakan(ZZDATABean item);

    void choose(ApplyBean applyBean, ZZDATABean item,int position);

}
