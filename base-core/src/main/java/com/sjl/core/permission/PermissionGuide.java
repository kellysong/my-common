package com.sjl.core.permission;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename PermissionGuide.java
 * @time 2019/1/3 13:42
 * @copyright(C) 2019 song
 */
public class PermissionGuide {


    public static String PermissionTip1 = "亲爱的用户 \n\n软件部分功能需要请求您的手机权限，请允许以下权限：\n\n";//权限提醒
    public static String PermissionTip2 = "\n请到 “应用信息 -> 权限” 中授予！";//权限提醒
    public static String PermissionDialogPositiveButton = "去手动授权";
    public static String PermissionDialogNegativeButton = "取消";

    private static PermissionGuide permissionUtils;

    public static PermissionGuide getInstance() {
        if (permissionUtils == null) {
            permissionUtils = new PermissionGuide();
        }
        return permissionUtils;
    }

    private HashMap<String, String> permissions;

    public HashMap<String, String> getPermissions() {
        if (permissions == null) {
            permissions = new HashMap<>();
            initPermissions();
        }
        return permissions;
    }

    private void initPermissions() {
        //联系人/通讯录权限
        permissions.put("android.permission.WRITE_CONTACTS", "--通讯录/联系人");
        permissions.put("android.permission.GET_ACCOUNTS", "--通讯录/联系人");
        permissions.put("android.permission.READ_CONTACTS", "--通讯录/联系人");
        //电话权限
        permissions.put("android.permission.READ_CALL_LOG", "--电话");
        permissions.put("android.permission.READ_PHONE_STATE", "--电话");
        permissions.put("android.permission.CALL_PHONE", "--电话");
        permissions.put("android.permission.WRITE_CALL_LOG", "--电话");
        permissions.put("android.permission.USE_SIP", "--电话");
        permissions.put("android.permission.PROCESS_OUTGOING_CALLS", "--电话");
        permissions.put("com.android.voicemail.permission.ADD_VOICEMAIL", "--电话");
        //日历权限
        permissions.put("android.permission.READ_CALENDAR", "--日历");
        permissions.put("android.permission.WRITE_CALENDAR", "--日历");
        //相机拍照权限
        permissions.put("android.permission.CAMERA", "--相机/拍照");
        //传感器权限
        permissions.put("android.permission.BODY_SENSORS", "--传感器");
        //定位权限
        permissions.put("android.permission.ACCESS_FINE_LOCATION", "--定位");
        permissions.put("android.permission.ACCESS_COARSE_LOCATION", "--定位");
        //文件存取
        permissions.put("android.permission.READ_EXTERNAL_STORAGE", "--文件存储");
        permissions.put("android.permission.WRITE_EXTERNAL_STORAGE", "--文件存储");
        //音视频、录音权限
        permissions.put("android.permission.RECORD_AUDIO", "--音视频/录音");
        //短信权限
        permissions.put("android.permission.READ_SMS", "--短信");
        permissions.put("android.permission.RECEIVE_WAP_PUSH", "--短信");
        permissions.put("android.permission.RECEIVE_MMS", "--短信");
        permissions.put("android.permission.RECEIVE_SMS", "--短信");
        permissions.put("android.permission.SEND_SMS", "--短信");
        permissions.put("android.permission.READ_CELL_BROADCASTS", "--短信");
    }

    /**
     * 获得权限名称集合
     *
     * @param permission 权限数组
     * @return 权限名称
     */
    public String getPermissionNames(List<String> permission) {
        if (permission == null || permission.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        HashMap<String, String> permissions = getPermissions();
        for (int i = 0; i < permission.size(); i++) {
            String name = permissions.get(permission.get(i));
            if (name != null && !list.contains(name)) {
                list.add(name);
                sb.append(name);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 获得权限名称集合
     *
     * @param permission 权限名称
     * @return 权限名称
     */
    public String getPermissionNames(String permission) {
        if (TextUtils.isEmpty(permission)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        HashMap<String, String> permissions = getPermissions();
        String name = permissions.get(permission);
        if (name != null && !list.contains(name)) {
            list.add(name);
            sb.append(name);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 打开APP详情页面，引导用户去设置权限
     *
     * @param activity        页面对象
     * @param permissionNames 权限名称（如是多个，使用\n分割）
     */
    public void openAppDetails(final Activity activity, String permissionNames) {


        StringBuilder sb = new StringBuilder();
        sb.append(PermissionGuide.PermissionTip1);
        sb.append(permissionNames);
        sb.append(PermissionGuide.PermissionTip2);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(sb.toString());
        builder.setPositiveButton(PermissionDialogPositiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String brand = Build.BRAND;//手机厂商
                if (TextUtils.equals(brand.toLowerCase(), "redmi") || TextUtils.equals(brand.toLowerCase(), "xiaomi")) {
                    gotoMiuiPermission(activity);//小米
                } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
                    gotoMeizuPermission(activity);
                } else if (TextUtils.equals(brand.toLowerCase(), "huawei") || TextUtils.equals(brand.toLowerCase(), "honor")) {
                    gotoHuaweiPermission(activity);
                } else {
                    activity.startActivity(getAppDetailSettingIntent(activity));
                }
                activity.finish();
            }
        });
        builder.setNegativeButton(PermissionDialogNegativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        builder.show();
    }


    /**
     * 跳转到miui的权限管理页面
     *
     * @param activity
     */
    private void gotoMiuiPermission(Activity activity) {
        try { // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", activity.getPackageName());
            activity.startActivity(localIntent);
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", activity.getPackageName());
                activity.startActivity(localIntent);
            } catch (Exception e1) { // 否则跳转到应用详情
                activity.startActivity(getAppDetailSettingIntent(activity));
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     *
     * @param activity
     */
    private void gotoMeizuPermission(Activity activity) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", activity.getPackageName());
            activity.startActivity(intent);
        } catch (Exception e) {
            activity.startActivity(getAppDetailSettingIntent(activity));
        }
    }

    /**
     * 华为的权限管理页面
     *
     * @param activity
     */
    private void gotoHuaweiPermission(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            activity.startActivity(intent);
        } catch (Exception e) {
            activity.startActivity(getAppDetailSettingIntent(activity));
        }

    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @param activity
     * @return
     */
    private Intent getAppDetailSettingIntent(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        return localIntent;
    }

}
