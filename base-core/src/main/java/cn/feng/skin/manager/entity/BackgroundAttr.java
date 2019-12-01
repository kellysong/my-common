package cn.feng.skin.manager.entity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;

import cn.feng.skin.manager.loader.SkinManager;

public class BackgroundAttr extends SkinAttr {

    @Override
    public void apply(View view) {

        if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
            view.setBackgroundColor(SkinManager.getInstance().getColor(attrValueRefId));
        } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
            Drawable bg = SkinManager.getInstance().getDrawable(attrValueRefId);
            //个人修复
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(bg);
            }else{
                view.setBackgroundDrawable(bg);
            }
            Log.i("attr", this.attrValueRefName + " 是否可变换状态? : " + bg.isStateful());
        } else {
            Log.i("attr", "不匹配attrValueTypeName：" + attrValueTypeName);
        }
    }
}
