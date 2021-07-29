package com.sjl.core.permission;

import android.Manifest;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename PermissionGroup
 * @time 2021/7/28 18:31
 * @copyright(C) 2021 song
 */
public class PermissionGroup {
    /**
     * 日历权限组
     */
    public static final String[] CALENDAR = new String[]{
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_CALENDAR,
    };

    /**
     * 传感器权限
     */
    public static final String[] SENSORS = new String[]{
            Manifest.permission.BODY_SENSORS
    };

    /**
     * 麦克风权限
     */
    public static final String[] MICROPHONE = new String[]{
            Manifest.permission.RECORD_AUDIO
    };

    /**
     * 短信权限
     */
    public static final String[] SMS = new String[]{
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.BROADCAST_SMS,
    };

    /**
     * 通讯录权限
     */
    public static final String[] CONTACTS = new String[]{
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS
    };

    /**
     * 手机状态权限
     */
    public static final String[] PHONE = new String[]{
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.ADD_VOICEMAIL,
    };


    /**
     * 手机电话权限
     */
    public static final String[] PERMISSION_PHONE_SINGLE = new String[]{
            Manifest.permission.CALL_PHONE
    };

    /**
     * 相机权限
     */
    public static final String[] CAMERA = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 位置权限
     */
    public static final String[] LOCATION = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    /**
     * 存储权限
     */
    public static final String[] STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
}
