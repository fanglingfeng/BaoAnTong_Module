package com.tjsoft.webhall.constants;

/**
 * Created by Dino on 9/25 0025.
 */

public class PermissionCode {
    /**
     * android 6.0 以后对权限策略更改，有些危险的权限，需要请求用户允许后再回调处理，不然会报SecurityException
     * 这里是权限回调code的统一管理
     */

    public static final int MY_PERMISSIONS_CAMERA                        = 1;//相机权限
    public final static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;//读取多媒体的权限
    public static final int MY_PERMISSIONS_WRITE                         = 1;//写入文件
}
