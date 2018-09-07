package com.tjsoft.util;

import android.text.TextUtils;

import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.UploadStatus;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.XKZXXActivity;

import java.util.List;

/**
 * Created by lenovo on 2017/4/25.
 * 大附件状态检测工具类（申报时用得上）
 */

public class CheckUploadUtil {
    /**
     * 大附件是否完善
     *
     * @param
     * @return
     */
    public static boolean checkAllUpload(String certcode) {
        if (null != Constants.materialXkz.get(certcode) && Constants.materialXkz.get(certcode).size() != 0) {// 设置文件节点

            for (int j = 0; j < Constants.materialXkz.get(certcode).size(); j++) {
                if (Constants.materialXkz.get(certcode).get(j).getUpStatus() == UploadStatus.UPLOAD_SUCCESS) {//上传成功的材料

                } else {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }
}
