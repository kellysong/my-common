package com.sjl.core.permission;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename PermissionAppFragment
 * @time 2021/7/27 20:02
 * @copyright(C) 2021 song
 */
public class PermissionAppFragment extends Fragment implements BasePermissionFragment {


    public PermissionAppFragment() {
    }

    public static PermissionAppFragment newInstance() {
        return new PermissionAppFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当状态发生改变，比如设备旋转时候，Fragment不会被销毁
        setRetainInstance(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);//权限申请
    }

    @Override
    public void dispatchPermissions(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @Override
    public void dispatchSpecialPermission(SpecialPermission specialPermission) {
        SpecialPermissionUtils.dispatchSpecialPermission(this, specialPermission);
    }

    @Override
    public void openActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public Context getPermissionContext() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext();
        }
        return getActivity();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SpecialPermissionUtils.onActivityResult(getPermissionContext(), requestCode, resultCode);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpecialPermissionUtils.isNotificationJump()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SpecialPermissionUtils.callbackNotification(getPermissionContext());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SpecialPermissionUtils.setNotificationJump(true);
    }
}
