package com.sjl.lib.test.mvp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sjl.core.mvp.BaseActivity;
import com.sjl.core.permission.PermissionsManager;
import com.sjl.core.permission.PermissionsResultAction;
import com.sjl.core.permission.SpecialPermission;
import com.sjl.core.util.log.LogUtils;
import com.sjl.lib.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename PermissionsTestActivity
 * @time 2021/7/28 10:28
 * @copyright(C) 2021 song
 */
public class PermissionsTestActivity extends BaseActivity {
    private static final String TAG = PermissionsTestActivity.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.permissions_test_activity;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks, true);
    }

    @Override
    protected void initData() {

    }

    private void requestSpecialPermission(SpecialPermission specialPermission) {
        PermissionsManager.getInstance().requestSpecialPermission(specialPermission, this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                LogUtils.d(specialPermission.name() + ":权限授权通过");
            }

            @Override
            public void onDenied(String permission) {
                LogUtils.d("权限拒绝：" + permission);
            }
        });
    }


    public void btnTestOverlay(View view) {
        requestSpecialPermission(SpecialPermission.OVERLAY);
    }

    public void btnTestSetting(View view) {
        requestSpecialPermission(SpecialPermission.SETTING);
    }

    public void btnTestInstall(View view) {
        requestSpecialPermission(SpecialPermission.INSTALL);

    }


    public void btnTestNotificationAccess(View view) {
        requestSpecialPermission(SpecialPermission.NOTIFICATION_ACCESS);

    }

    public void btnTestManageAllFilesAccess(View view) {
        requestSpecialPermission(SpecialPermission.MANAGE_ALL_FILES_ACCESS);
    }


    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentPreAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
            super.onFragmentPreAttached(fm, f, context);
            Log.i(TAG, "onFragmentPreAttached: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
            super.onFragmentActivityCreated(fm, f, savedInstanceState);
            Log.i(TAG, "onFragmentActivityCreated: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
            super.onFragmentAttached(fm, f, context);
            Log.i(TAG, "onFragmentAttached: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
            super.onFragmentPreCreated(fm, f, savedInstanceState);
            Log.i(TAG, "onFragmentPreCreated: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
            Log.i(TAG, "onFragmentCreated: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            Log.i(TAG, "onFragmentViewCreated: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentStarted(fm, f);
            Log.i(TAG, "onFragmentStarted: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentResumed(fm, f);
            Log.i(TAG, "onFragmentResumed: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentPaused(fm, f);
            Log.i(TAG, "onFragmentPaused: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentStopped(fm, f);
            Log.i(TAG, "onFragmentStopped: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentSaveInstanceState(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Bundle outState) {
            super.onFragmentSaveInstanceState(fm, f, outState);
            Log.i(TAG, "onFragmentSaveInstanceState: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentViewDestroyed(fm, f);
            Log.i(TAG, "onFragmentViewDestroyed: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentDestroyed(fm, f);
            Log.i(TAG, "onFragmentDestroyed: " + f.getClass().getSimpleName());
        }

        @Override
        public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentDetached(fm, f);
            Log.i(TAG, "onFragmentDetached: " + f.getClass().getSimpleName());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFragmentLifecycleCallbacks != null) {
            getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks);
            mFragmentLifecycleCallbacks = null;
        }

    }


}
