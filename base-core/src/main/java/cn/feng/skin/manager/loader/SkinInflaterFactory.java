package cn.feng.skin.manager.loader;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.sjl.core.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.feng.skin.manager.config.SkinConfig;
import cn.feng.skin.manager.entity.AttrFactory;
import cn.feng.skin.manager.entity.DynamicAttr;
import cn.feng.skin.manager.entity.SkinAttr;
import cn.feng.skin.manager.entity.SkinItem;
import cn.feng.skin.manager.util.L;
import cn.feng.skin.manager.util.ListUtils;

/**
 * 个别机型存在问题，建议使用SkinInflaterFactory2
 * Supply {@link SkinInflaterFactory} to be called when inflating from a LayoutInflater.
 * <p>
 * <p>Use this to collect the {skin:enable="true|false"} views availabled in our XML layout files.
 *
 * @author fengjun
 */
@Deprecated
public class SkinInflaterFactory implements LayoutInflaterFactory {

    private static final String TAG = "SkinInflaterFactory";

    /**
     * Store the view item that need skin changing in the activity
     */
    private List<SkinItem> mSkinItems = new ArrayList<SkinItem>();


//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//
//        // if this is NOT enable to be skined , simplly skip it
//        boolean isSkinEnable = attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false);
//
//        if (!isSkinEnable) {
//            return null;
//        } else {
//            LogUtils.e("name:" + name + ";isSkinEnable:" + isSkinEnable);
//        }
//
//        View view = createView(context, name, attrs);
//
//        if (view == null) {
//            return null;
//        }
//
//        parseSkinAttr(context, attrs, view);
//
//        return view;
//    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // if this is NOT enable to be skined , simplly skip it
        boolean isSkinEnable = attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false);

        if (!isSkinEnable) {
            return null;
        } else {
            LogUtils.e("name:" + name + ";isSkinEnable:" + isSkinEnable);
        }

        View view = createView(context, name, attrs);

        if (view == null) {
            return null;
        }

        parseSkinAttr(context, attrs, view);

        return view;
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
            LogUtils.e("error while create 【" + name + "】",e);
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

            if (!AttrFactory.isSupportedAttr(attrName)) {
                continue;
            }
            Log.i(TAG, "attrValue:" + attrName + ",attrValue:" + attrValue);
            //  android:background="?attr/colorPrimary"//不要使用这些?attr属性，否则不好区分类型
            if (attrValue.startsWith("@")) {
                try {
                    int id = Integer.parseInt(attrValue.substring(1));
                    String entryName = context.getResources().getResourceEntryName(id);
                    String typeName = context.getResources().getResourceTypeName(id);
                    SkinAttr mSkinAttr = AttrFactory.get(attrName, id, entryName, typeName);
                    L.i(TAG, "view:" + view.getClass().getSimpleName());
                    L.i(TAG, "attrName:" + attrName + " | attrValue:" + attrValue);
                    L.i(TAG, "id:" + id);
                    L.i(TAG, "entryName:" + entryName);
                    L.i(TAG, "typeName:" + typeName);
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
