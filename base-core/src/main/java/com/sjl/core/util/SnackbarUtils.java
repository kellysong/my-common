package com.sjl.core.util;

import com.google.android.material.snackbar.Snackbar;
import android.view.View;

/**
 * Snackbar工具类
 */
public class SnackbarUtils {
    private static final int color_danger = 0XFFA94442;
    private static final int color_success = 0XFF3C763D;
    private static final int color_info = 0XFF31708F;
    private static final int color_warning = 0XFF8A6D3B;

    private static final int color_action = 0XFFCDC5BF;

    private Snackbar mSnackbar;

    private SnackbarUtils(Snackbar snackbar) {
        mSnackbar = snackbar;
    }

    public static SnackbarUtils makeShort(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        return new SnackbarUtils(snackbar);
    }

    public static SnackbarUtils makeShort(View view, int text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        return new SnackbarUtils(snackbar);
    }

    public static SnackbarUtils makeLong(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        return new SnackbarUtils(snackbar);
    }

    public static SnackbarUtils makeLong(View view, int text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        return new SnackbarUtils(snackbar);
    }

    private View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;

    }


    private Snackbar setSnackBarBackColor(int colorId) {
        View snackBarView = getSnackBarLayout(mSnackbar);
        if (snackBarView != null) {
            snackBarView.setBackgroundColor(colorId);
        }
        return mSnackbar;
    }

    public void info() {
        setSnackBarBackColor(color_info);
        show();
    }

    public void info(String actionText, View.OnClickListener listener) {
        setSnackBarBackColor(color_info);
        show(actionText, listener);
    }

    public void warning() {
        setSnackBarBackColor(color_warning);
        show();
    }

    public void warning(String actionText, View.OnClickListener listener) {
        setSnackBarBackColor(color_warning);
        show(actionText, listener);
    }

    public void danger() {
        setSnackBarBackColor(color_danger);
        show();
    }

    public void danger(String actionText, View.OnClickListener listener) {
        setSnackBarBackColor(color_danger);
        show(actionText, listener);
    }

    public void success() {
        setSnackBarBackColor(color_success);
        show();
    }

    public void success(String actionText, View.OnClickListener listener) {
        setSnackBarBackColor(color_success);
        show(actionText, listener);
    }

    public void show() {
        mSnackbar.show();
    }

    public void show(String actionText, View.OnClickListener listener) {
        mSnackbar.setActionTextColor(color_action);
        mSnackbar.setAction(actionText, listener).show();
    }
}
