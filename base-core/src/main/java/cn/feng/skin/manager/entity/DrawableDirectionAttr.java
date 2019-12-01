package cn.feng.skin.manager.entity;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.feng.skin.manager.loader.SkinManager;

/**
 * 设置TextView、EditText的drawableLeft等属性
 *
 * @author Kelly
 * @version 1.0.0
 * @filename DrawableDirectionAttr.java
 * @time 2019/6/4 9:04
 * @copyright(C) 2019 song
 */
public class DrawableDirectionAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            setCompoundDrawables(tv);
        } else if (view instanceof EditText) {
            EditText et = (EditText) view;
            setCompoundDrawables(et);
        } else {
            Log.i("attr", "不匹配attrValueTypeName：" + attrValueTypeName);
        }

    }

    private void setCompoundDrawables(TextView textView) {
        if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
            Drawable bg = SkinManager.getInstance().getDrawable(attrValueRefId);
            // 这一步必须要做,否则不会显示.
            bg.setBounds(0, 0, bg.getMinimumWidth(),
                    bg.getMinimumHeight());
            if (attrName.equals("drawableLeft")) {
                textView.setCompoundDrawables(bg, null, null, null);
            } else if (attrName.equals("drawableRight")) {
                textView.setCompoundDrawables(null, null, bg, null);
            } else if (attrName.equals("drawableBottom")) {
                textView.setCompoundDrawables(null, null, null, bg);
            } else if (attrName.equals("drawableTop")) {
                textView.setCompoundDrawables(null, bg, null, null);
            }
        }
    }
}
