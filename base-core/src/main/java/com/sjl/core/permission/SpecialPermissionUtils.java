package com.sjl.core.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename SpecialPermissionUtils
 * @time 2021/7/28 11:10
 * @copyright(C) 2021 song
 */
public class SpecialPermissionUtils {

    private static final int REQUEST_CODE_INSTALL = 2;
    private static final int REQUEST_CODE_OVERLAY = 3;
    private static final int REQUEST_CODE_SETTING = 4;
    private static final int REQUEST_CODE_NOTIFICATION_ACCESS = 5;
    /**
     * Android 11,可以允许你的App拥有对整个SD卡进行读写的权限。
     */
    private static final int REQUEST_CODE_MANAGE_ALL_FILES_ACCESS = 6;


    private static int currentRequestCode = -1;

    /**
     * 分发特殊权限
     *  @param basePermissionFragment
     * @param specialPermission
     */
    public static void dispatchSpecialPermission(BasePermissionFragment basePermissionFragment, SpecialPermission specialPermission) {
        Context context = basePermissionFragment.getPermissionContext();
        switch (specialPermission) {
            case INSTALL: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                    if (!haveInstallPermission) {
                        Uri packageURI = Uri.parse("package:" + context.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                        if (basePermissionFragment instanceof Fragment){

                        }
                        basePermissionFragment.openActivityForResult(intent, REQUEST_CODE_INSTALL);
                        return;
                    }
                }
                callbackInstall(context);
                break;
            }
            case OVERLAY: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(context)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        basePermissionFragment.openActivityForResult(intent, REQUEST_CODE_OVERLAY);
                        return;
                    }
                }
                callbackOverlay(context);
                break;
            }
            case SETTING: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(context)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        basePermissionFragment.openActivityForResult(intent, REQUEST_CODE_SETTING);
                        return;
                    }
                }
                callbackWrite(context);
                break;
            }

            case NOTIFICATION_ACCESS: {
                resetNotificationFlag();

                if (!isNotificationEnabled(context)) {
                    goToNotificationSetting(context);
                    return;
                }
                callbackNotification(context);
                break;
            }

            case MANAGE_ALL_FILES_ACCESS: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    basePermissionFragment.openActivityForResult(intent, REQUEST_CODE_MANAGE_ALL_FILES_ACCESS);
                    return;
                }
                callbackExternalStorageManager();
                break;
            }
            default:
                break;
        }
    }

    /**
     * 在fragment中启动Activity调用fragment.startActivityForResult，否则不会回调onActivityResult方法。
     * @param context
     * @param requestCode
     * @param resultCode
     */
    public static void onActivityResult(Context context, int requestCode, int resultCode) {
        switch (requestCode) {
            case REQUEST_CODE_INSTALL: {
                callbackInstall(context);
                break;
            }
            case REQUEST_CODE_OVERLAY: {
                callbackOverlay(context);
                break;
            }
            case REQUEST_CODE_SETTING: {
                callbackWrite(context);
                break;
            }
         /*   case REQUEST_CODE_NOTIFICATION_ACCESS: { //由于通知开关打开或关闭，没有确切回调，本次通过Fragment生命周期计算
                callbackNotification(context);
                break;
            }*/
            case REQUEST_CODE_MANAGE_ALL_FILES_ACCESS: {
                callbackExternalStorageManager();
                break;
            }
            default:
                break;
        }
    }

    public static void callbackNotification(Context context) {
        resetNotificationFlag();
        if (!isNotificationEnabled(context)) {
            PermissionsManager.getInstance().notifyPermissionsChange(SpecialPermission.NOTIFICATION_ACCESS.name(), PackageManager.PERMISSION_DENIED);
            return;
        }
        PermissionsManager.getInstance().notifyPermissionsChange(SpecialPermission.NOTIFICATION_ACCESS.name(), PackageManager.PERMISSION_GRANTED);
    }

    /**
     * 跳到通知栏设置界面
     *
     * @param context
     */
    public static void goToNotificationSetting(Context context) {
        Intent intent = new Intent();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 8.0以上
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0——7.1 之间的版本可以使用
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {//4.4
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(intent);
    }

    public static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
        boolean isOpened = manager.areNotificationsEnabled();
        return isOpened;
    }

    public static void callbackInstall(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                PermissionsManager.getInstance().notifyPermissionsChange(Manifest.permission.REQUEST_INSTALL_PACKAGES, PackageManager.PERMISSION_DENIED);
                return;
            }
        }
        PermissionsManager.getInstance().notifyPermissionsChange(Manifest.permission.REQUEST_INSTALL_PACKAGES, PackageManager.PERMISSION_GRANTED);
    }

    public static void callbackOverlay(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                PermissionsManager.getInstance().notifyPermissionsChange(Manifest.permission.SYSTEM_ALERT_WINDOW, PackageManager.PERMISSION_DENIED);
                return;
            }
        }
        PermissionsManager.getInstance().notifyPermissionsChange(Manifest.permission.SYSTEM_ALERT_WINDOW, PackageManager.PERMISSION_GRANTED);
    }

    public static void callbackWrite(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context)) {
                PermissionsManager.getInstance().notifyPermissionsChange(Manifest.permission.WRITE_SETTINGS, PackageManager.PERMISSION_DENIED);
                return;
            }
        }
        PermissionsManager.getInstance().notifyPermissionsChange(Manifest.permission.WRITE_SETTINGS, PackageManager.PERMISSION_GRANTED);
    }

    public static void callbackExternalStorageManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                PermissionsManager.getInstance().notifyPermissionsChange(Manifest.permission.MANAGE_EXTERNAL_STORAGE, PackageManager.PERMISSION_DENIED);
                return;
            }
        }
        PermissionsManager.getInstance().notifyPermissionsChange(Manifest.permission.MANAGE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
    }

    /**
     * 是否已经跳转到通知页面
     */
    private static boolean notificationJump = false;

    /**
     * 重置通知标记
     */
    private static void resetNotificationFlag() {
        currentRequestCode = REQUEST_CODE_NOTIFICATION_ACCESS;
        setNotificationJump(false);//重置
    }

    public static void setNotificationJump(boolean notificationJump) {
        if (currentRequestCode == REQUEST_CODE_NOTIFICATION_ACCESS) {
            SpecialPermissionUtils.notificationJump = notificationJump;
        }
    }

    public static boolean isNotificationJump() {
        if (currentRequestCode == REQUEST_CODE_NOTIFICATION_ACCESS) {
            return notificationJump;
        }
        return false;
    }
}
