package com.sjl.core.permission;

import android.content.Context;
import android.content.Intent;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BasePermissionFragment
 * @time 2021/7/27 20:01
 * @copyright(C) 2021 song
 */
public interface BasePermissionFragment {



    /**
     * 分发运行时权限
     * @param permissions
     * @param requestCode
     */
    void dispatchPermissions(String[] permissions, int requestCode);

    /**
     * 分发特殊权限
     * @param specialPermission
     */
    void dispatchSpecialPermission(SpecialPermission specialPermission);

    /**
     * Fragment中打开Activity
     * @param intent
     * @param requestCode
     */
    void openActivityForResult(Intent intent, int requestCode);

    /**
     * 获取上下文
     * @return
     */
    Context getPermissionContext();
}
