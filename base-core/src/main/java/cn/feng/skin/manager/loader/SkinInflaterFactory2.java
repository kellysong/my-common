package cn.feng.skin.manager.loader;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import com.sjl.core.util.LogUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.feng.skin.manager.config.SkinConfig;
import cn.feng.skin.manager.entity.AttrFactory;
import cn.feng.skin.manager.entity.DynamicAttr;
import cn.feng.skin.manager.entity.SkinAttr;
import cn.feng.skin.manager.entity.SkinItem;
import cn.feng.skin.manager.util.L;
import cn.feng.skin.manager.util.ListUtils;

/**
 * 修复个别机型换肤失败，比如vivo x7
 *
 * @author song 20190417
 */
public class SkinInflaterFactory2 implements LayoutInflaterFactory {

    private static final String TAG = "SkinInflaterFactory";

    /**
     * Store the view item that need skin changing in the activity
     */
    private List<SkinItem> mSkinItems = new ArrayList<SkinItem>();

    static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    private static final Map<String, Constructor<? extends View>> sConstructorMap = new ArrayMap<>();
    private final Object[] mConstructorArgs = new Object[2];
    private static Method sCreateViewMethod;
    static final Class<?>[] sCreateViewSignature = new Class[]{View.class, String.class, Context.class, AttributeSet.class};
    private AppCompatDelegate delegate;

    public SkinInflaterFactory2(AppCompatDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // if this is NOT enable to be skined , simplly skip it
        boolean isSkinEnable = attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false);

        if (!isSkinEnable) {
            return null;
        } else {
//            LogUtils.e("name:" + name + ";isSkinEnable:" + isSkinEnable);
        }
        View view = null;

        if (view == null) {
            view = createViewFromTag(context, name, attrs);//修复个别机型bug
        }
        if (view == null) {
            try {
                if (sCreateViewMethod == null) {
                    Method methodOnCreateView = delegate.getClass().getMethod("createView", sCreateViewSignature);
                    sCreateViewMethod = methodOnCreateView;
                }
                Object object = sCreateViewMethod.invoke(delegate, parent, name, context, attrs);
                view = (View) object;
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }


        if (view == null) {
            view = createView(context, name, attrs);//个别机型有bug
        }
        if (view == null) {
            return null;
        }
        /**
         * 解析View属性
         */
        parseSkinAttr(context, attrs, view);

        return view;
    }

    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        View view = null;

        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }
        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                // try the android.widget prefix first...
                if ("View".equals(name)) {
                    view = createView2(context, name, "android.view.");
                }
                if (view == null) {
                    view = createView2(context, name, "android.widget.");
                }
                if (view == null) {
                    view = createView2(context, name, "android.webkit.");
                }
                return view;
            } else {
                return createView2(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createView2(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }

    /**
     * Invoke low-level function for instantiating a view by name. This attempts to
     * instantiate a view class of the given <var>name</var> found in this
     * LayoutInflater's ClassLoader.
     *
     * @param context
     * @param name    The full name of the class to be instantiated.
     * @param attrs   The XML attributes supplied for this instance.
     * @return View The newly instantiated view, or null.
     */
    private View createView(Context context, String name, AttributeSet attrs) {
        View view = null;
        try {
            if (-1 == name.indexOf('.')) {
                if ("View".equals(name)) {

                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs);
            }

            L.i("about to create " + name);

        } catch (Exception e) {
            LogUtils.e("error while create 【" + name + "】", e);
            view = null;
        }
        return view;
    }

    /**
     * Collect skin able tag such as background , textColor and so on
     *
     * @param context
     * @param attrs
     * @param view
     */
    private void parseSkinAttr(Context context, AttributeSet attrs, View view) {
        List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            //1.每次增加新支持的属性必须改这里，重点
            if (!AttrFactory.isSupportedAttr(attrName)) {
                continue;
            }
            //  android:background="?attr/colorPrimary"//不要使用这些?attr属性，否则不好区分类型
            if (attrValue.startsWith("@")) {
                try {
                    int id = Integer.parseInt(attrValue.substring(1));
                    String typeName = context.getResources().getResourceTypeName(id);
                    String entryName = context.getResources().getResourceEntryName(id);
                    SkinAttr mSkinAttr = AttrFactory.get(attrName, id, entryName, typeName);
                    //            android:drawableLeft="@drawable/line_title"

                    L.w(TAG, "===========view:" + view.getClass().getSimpleName());//TextView
                    L.i(TAG, "attrName:" + attrName + " | attrValue:" + attrValue);//drawableLeft 属性
                    L.i(TAG, "id:" + id);
                    L.i(TAG, "typeName:" + typeName);//drawable 引用类型
                    L.i(TAG, "entryName:" + entryName);//line_title 引用属性

                    if (mSkinAttr != null) {
                        viewAttrs.add(mSkinAttr);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!ListUtils.isEmpty(viewAttrs)) {
            SkinItem skinItem = new SkinItem();
            skinItem.view = view;
            skinItem.attrs = viewAttrs;

            mSkinItems.add(skinItem);

            if (SkinManager.getInstance().isExternalSkin()) {
                skinItem.apply();
            }
        }
    }

    public void applySkin() {
        if (ListUtils.isEmpty(mSkinItems)) {
            return;
        }

        for (SkinItem si : mSkinItems) {
            if (si.view == null) {
                continue;
            }
            si.apply();
        }
    }

    /**
     * 对动态创建的view换肤
     *
     * @param context
     * @param view
     * @param pDAttrs
     */
    public void dynamicAddSkinEnableView(Context context, View view, List<DynamicAttr> pDAttrs) {
        List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();
        SkinItem skinItem = new SkinItem();
        skinItem.view = view;

        for (DynamicAttr dAttr : pDAttrs) {
            int id = dAttr.refResId;
            String entryName = context.getResources().getResourceEntryName(id);
            String typeName = context.getResources().getResourceTypeName(id);
            SkinAttr mSkinAttr = AttrFactory.get(dAttr.attrName, id, entryName, typeName);
            viewAttrs.add(mSkinAttr);
        }

        skinItem.attrs = viewAttrs;
        skinItem.apply();
        addSkinView(skinItem);
    }

    /**
     * 对动态创建的view换肤
     * SkinAttr
     * [
     * attrName=background,
     * attrValueRefId=2131689501,
     * attrValueRefName=colorPrimary,
     * attrValueTypeName=color
     * ]
     *
     * @param context
     * @param view
     * @param attrName
     * @param attrValueResId
     */
    public void dynamicAddSkinEnableView(Context context, View view, String attrName, int attrValueResId) {
        int id = attrValueResId;
        String entryName = context.getResources().getResourceEntryName(id);
        String typeName = context.getResources().getResourceTypeName(id);
        SkinAttr mSkinAttr = AttrFactory.get(attrName, id, entryName, typeName);
        SkinItem skinItem = new SkinItem();
        skinItem.view = view;
        List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();
        viewAttrs.add(mSkinAttr);
        skinItem.attrs = viewAttrs;
        skinItem.apply();
        addSkinView(skinItem);
    }

    public void addSkinView(SkinItem item) {
        mSkinItems.add(item);
    }

    public void clean() {
        if (ListUtils.isEmpty(mSkinItems)) {
            return;
        }

        for (SkinItem si : mSkinItems) {
            if (si.view == null) {
                continue;
            }
            si.clean();
        }
        mSkinItems.clear();
    }


}
