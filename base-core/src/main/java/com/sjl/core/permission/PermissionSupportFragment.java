package com.sjl.core.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Fragment 一般依赖于 Activity 存活，并且生命周期跟 Activity 差不多，因此，我们进行权限申请的时候，可以利用透明的 Fragment 进行申请，在里面处理完之后，再进行相应的回调。
 *
 * @author Kelly
 * @version 1.0.0
 * @filename PermissionSupportFragment
 * @time 2021/7/27 18:29
 * @copyright(C) 2021 song
 */
public class PermissionSupportFragment extends Fragment implements BasePermissionFragment {


    public PermissionSupportFragment() {
    }

    public static PermissionSupportFragment newInstance() {
        return new PermissionSupportFragment();
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
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void dispatchSpecialPermission(SpecialPermission specialPermission) {
        SpecialPermissionUtils.dispatchSpecialPermission(this,specialPermission);
    }

    @Override
    public void openActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent,requestCode);
    }

    @Override
    public Context getPermissionContext() {
        return getContext();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SpecialPermissionUtils.onActivityResult(getPermissionContext(),requestCode,resultCode);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (SpecialPermissionUtils.isNotificationJump()){
            SpecialPermissionUtils.callbackNotification(getPermissionContext());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SpecialPermissionUtils.setNotificationJump(true);
    }
}
