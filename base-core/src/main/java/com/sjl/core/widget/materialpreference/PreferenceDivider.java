package com.sjl.core.widget.materialpreference;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjl.core.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * 分割线
 */
public class PreferenceDivider extends android.preference.Preference {


    public PreferenceDivider(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public PreferenceDivider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public PreferenceDivider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(LOLLIPOP)
    public PreferenceDivider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    }


    @Override
    protected View onCreateView(ViewGroup parent) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.mp_preference_divider, parent, false);


        return layout;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
    }
}
