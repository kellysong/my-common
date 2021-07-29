package com.sjl.core.util.activityresult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * 处理Activity onActivityResult回调
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ActivityResultUtils
 * @time 2021/7/28 15:27
 * @copyright(C) 2021 song
 */
public class ActivityResultUtils {
    private Context mContext;
    private ResultFragment mResultFragment;
    private static final String TAG = "ResultFragment";


    public static ActivityResultUtils init(Activity activity) {
        return new ActivityResultUtils(activity);
    }

    public static ActivityResultUtils init(Fragment fragment) {
        return new ActivityResultUtils(fragment.getActivity());
    }


    private ActivityResultUtils(Activity activity) {
        mContext = activity;
        if (activity instanceof FragmentActivity) {
            mResultFragment = getResultFragment((FragmentActivity) activity);
        } else {
            throw new IllegalArgumentException("activity 不是FragmentActivity");
        }

    }

    private ResultFragment getResultFragment(FragmentActivity activity) {
        ResultFragment resultFragment = (ResultFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if (resultFragment == null) {
            resultFragment = ResultFragment.newInstance();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(resultFragment, TAG)
                    .commitAllowingStateLoss();
            //commit()方法并不立即执行transaction中包含的动作,而是把它加入到UI线程队列中.
            //如果想要立即执行,可以在commit之后立即调用FragmentManager的executePendingTransactions()方法
            fragmentManager.executePendingTransactions();
        }
        return resultFragment;
    }


    public void startActivityForResult(Class<?> clazz, int requestCode, Callback callback) {
        Intent intent = new Intent(mContext, clazz);
        startActivityForResult(intent, requestCode, callback);
    }

    public void startActivityForResult(Intent intent, int requestCode, Callback callback) {
        mResultFragment.startActivityForResult(intent, requestCode, callback);
    }

    public interface Callback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
