package com.sjl.core.permission;

/**
 * 特殊权限枚举
 *
 * @author Kelly
 * @version 1.0.0
 * @filename SpecialPermission
 * @time 2021/7/28 10:09
 * @copyright(C) 2021 song
 */
public enum SpecialPermission {
    INSTALL,// 未知应用安装权限
    OVERLAY,// 悬浮窗权限

    SETTING,// 设置权限
    NOTIFICATION_ACCESS,//访问通知权限
    MANAGE_ALL_FILES_ACCESS //管理外部存储权限

}
