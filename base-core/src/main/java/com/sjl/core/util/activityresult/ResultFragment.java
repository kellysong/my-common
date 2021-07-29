package com.sjl.core.util.activityresult;

import android.content.Intent;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ActivityResultFragment
 * @time 2021/7/28 15:27
 * @copyright(C) 2021 song
 */
public class ResultFragment extends Fragment {

    private WeakReference<ActivityResultUtils.Callback> mCallbacks;

    public ResultFragment() {
    }

    public static ResultFragment newInstance() {
        return new ResultFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startActivityForResult(Intent intent, int requestCode, ActivityResultUtils.Callback callback) {
        mCallbacks = new WeakReference<ActivityResultUtils.Callback>(callback);
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallbacks == null) {
            return;
        }
        ActivityResultUtils.Callback callback = mCallbacks.get();
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }
}

