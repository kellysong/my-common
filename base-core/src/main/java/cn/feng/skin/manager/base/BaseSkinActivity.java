package cn.feng.skin.manager.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import cn.feng.skin.manager.entity.DynamicAttr;
import cn.feng.skin.manager.listener.IDynamicNewView;
import cn.feng.skin.manager.listener.ISkinUpdate;
import cn.feng.skin.manager.loader.SkinInflaterFactory3;
import cn.feng.skin.manager.loader.SkinManager;
import cn.feng.skin.manager.statusbar.StatusBarUtil;

/**
 * Base Activity for development
 * <p>
 * <p>NOTICE:<br>
 * You should extends from this if you what to do skin change
 *
 * @author fengjun
 */

public class BaseSkinActivity extends AppCompatActivity implements ISkinUpdate, IDynamicNewView {

    /**
     * Whether response to skin changing after create
     */
    private boolean isResponseOnSkinChanging = true;

    //    private SkinInflaterFactory mSkinInflaterFactory;
//    private SkinInflaterFactory2 mSkinInflaterFactory;
    private SkinInflaterFactory3 mSkinInflaterFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //appcompat 创建view代码
        //1.0
//        mSkinInflaterFactory = new SkinInflaterFactory();
//        getLayoutInflater().setFactory(mSkinInflaterFactory);
        //2.0
//        mSkinInflaterFactory = new SkinInflaterFactory2(getDelegate());
//        LayoutInflaterCompat.setFactory(getLayoutInflater(), mSkinInflaterFactory);
        //3.0
        mSkinInflaterFactory = new SkinInflaterFactory3(getDelegate());
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), mSkinInflaterFactory);
        //必须在onCreate之前否则报错
        super.onCreate(savedInstanceState);
        changeStatusBarColor();

    }


    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clean();
    }

    /**
     * dynamic add a skin view
     *
     * @param view
     * @param attrName
     * @param attrValueResId
     */
    protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
    }

    protected void dynamicAddSkinEnableView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    final protected void enableResponseOnSkinChanging(boolean enable) {
        isResponseOnSkinChanging = enable;
    }

    @Override
    public void onThemeUpdate() {
        if (!isResponseOnSkinChanging) {
            return;
        }
        mSkinInflaterFactory.applySkin();
        changeStatusBarColor();

    }

    /**
     * 改变状态栏颜色，如果需要改变，直接覆写
     */
    protected void changeStatusBarColor() {
        int color = SkinManager.getInstance().getColorPrimary();
        if (color != -1) {//不能少，否则状态栏无效
            StatusBarUtil.setColorNoTranslucent(this, color);
        }
    }


    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }


   /* @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            super.attachBaseContext(getLanguageContext());//不能替换，替换会导致广播注册失败，明明已经注册，提示未注册异常
        } catch (Exception e) {
            LogUtils.e("get LanguageContext error.", e);
            super.attachBaseContext(newBase);
        }
    }*/


    /**
     * 获取语言的上下文
     *
     * @return
     * @throws Exception
     */
    @Deprecated
    private Context getLanguageContext() throws Exception {
        Class<?> clz = Class.forName("com.sjl.swimchat.kotlin.LanguageManager");
        Field instance = clz.getField("INSTANCE");
        instance.setAccessible(true);
        Object o = instance.get(null);
        Method changeLanguage = clz.getDeclaredMethod("getContext");
        Context languageContext = (Context) changeLanguage.invoke(o);
        return languageContext;
    }

}
