package cn.feng.skin.manager.loader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.sjl.core.util.log.LogWriter;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.feng.skin.manager.config.SkinConfig;
import cn.feng.skin.manager.listener.ILoaderListener;
import cn.feng.skin.manager.listener.ISkinLoader;
import cn.feng.skin.manager.listener.ISkinUpdate;
import cn.feng.skin.manager.util.L;

/**
 * Skin Manager Instance
 * <p>
 * <p>
 * <ul>
 * <strong>global init skin manager, MUST BE CALLED FIRST ! </strong>
 * <li> {@link #init()} </li>
 * </ul>
 * <ul>
 * <strong>get single runtime instance</strong>
 * <li> {@link #getInstance()} </li>
 * </ul>
 * <ul>
 * <strong>attach a listener (Activity or fragment) to SkinManager</strong>
 * <li> {@link #onAttach(ISkinUpdate observer)} </li>
 * </ul>
 * <ul>
 * <strong>detach a listener (Activity or fragment) to SkinManager</strong>
 * <li> {@link #detach(ISkinUpdate observer)} </li>
 * </ul>
 * <ul>
 * <strong>load latest theme </strong>
 * <li> {@link #load()} </li>
 * <li> {@link #load(ILoaderListener callback)} </li>
 * </ul>
 * <ul>
 * <strong>load new theme with the giving skinPackagePath</strong>
 * <li> {@link #load(String skinPackagePath, ILoaderListener callback)} </li>
 * </ul>
 *
 * @author fengjun
 */
public class SkinManager implements ISkinLoader {

    private static final String NOT_INIT_ERROR = "SkinManager MUST init with Context first";
    private static Object synchronizedLock = new Object();
    private static SkinManager instance;

    private List<ISkinUpdate> skinObservers;
    private Context context;
    private String skinPackageName;
    private Resources mResources;
    private String skinPath;
    private boolean isDefaultSkin = false;

    /**
     * whether the skin being used is from external .skin file
     *
     * @return is external skin = true
     */
    public boolean isExternalSkin() {
        return !isDefaultSkin && mResources != null;
    }

    /**
     * get current skin path
     *
     * @return current skin path
     */
    public String getSkinPath() {
        return skinPath;
    }

    /**
     * return a global static instance of {@link SkinManager}
     *
     * @return
     */
    public static SkinManager getInstance() {
        if (instance == null) {
            synchronized (synchronizedLock) {
                if (instance == null) {
                    instance = new SkinManager();
                }
            }
        }
        return instance;
    }

    public String getSkinPackageName() {
        return skinPackageName;
    }

    public Resources getResources() {
        return mResources;
    }

    private SkinManager() {
    }

    public void init(Context ctx) {
        context = ctx.getApplicationContext();
    }

    /**
     * 恢复默认主体
     */
    public void restoreDefaultTheme() {
        SkinConfig.saveSkinPath(context, SkinConfig.DEFALT_SKIN);
        isDefaultSkin = true;
        mResources = context.getResources();
        skinPackageName = context.getPackageName();
        notifySkinUpdate();
    }

    /**
     * 加载当前设置的皮肤
     */
    public void load() {
        String skin = SkinConfig.getCustomSkinPath(context);
        File file = new File(skin);
        if (!file.exists()) {
            LogWriter.e("换肤失败:" + skin + ",file.exists()=" + file.exists() + ",file.isFile()=" + file.isFile());
            return;
        }
        load(skin, null);
    }

    /**
     * 同步换肤
     */
    public void loadSync() {
        String skin = SkinConfig.getCustomSkinPath(context);
        File file = new File(skin);
        if (!file.exists()) {
            LogWriter.e("换肤失败:" + skin + ",file.exists()=" + file.exists() + ",file.isFile()=" + file.isFile());
            return;
        }
        Map<String, Object> result = loadSkin(skin);
        if (result != null) {
            mResources = (Resources) result.get("skinResource");
            skinPackageName = (String) result.get("skinPackageName");
            notifySkinUpdate();
        } else {
            isDefaultSkin = true;
        }
    }


    /**
     * 加载皮肤
     *
     * @param callback
     */
    public void load(ILoaderListener callback) {
        if (SkinConfig.isDefaultSkin(context)) {
            return;
        }
        String skin = SkinConfig.getCustomSkinPath(context);
        load(skin, callback);
    }

    /**
     * Load resources from apk in asyc task
     *
     * @param skinPackagePath path of skin apk
     * @param callback        callback to notify user
     */
    public void load(String skinPackagePath, final ILoaderListener callback) {

        new AsyncTask<String, Void, Map<String, Object>>() {

            protected void onPreExecute() {
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            protected Map<String, Object> doInBackground(String... params) {
                if (params.length == 1) {
                    String skinPkgPath = params[0];
                    Map<String, Object> result = loadSkin(skinPkgPath);
                    return result;
                } else {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Map<String, Object> result) {
                if (result != null) {
                    //一起赋值使mResources与skinPackageName一致，防止并发是错乱，导致异常
                    mResources = (Resources) result.get("skinResource");
                    skinPackageName = (String) result.get("skinPackageName");
                    notifySkinUpdate();
                    if (callback != null) callback.onSuccess();
                } else {
                    isDefaultSkin = true;
                    if (callback != null) callback.onFailed();
                }
            }

        }.execute(skinPackagePath);
    }


    /**
     * 加载皮肤包
     *
     * @param skinPkgPath
     * @return
     */
    private Map<String, Object> loadSkin(String skinPkgPath) {
        File file = new File(skinPkgPath);
        if (!file.exists()) {
            return null;
        }
        try {
            //核心代码，利用包管理进行加载资源文件
            PackageManager mPm = context.getPackageManager();
            PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath, PackageManager.GET_ACTIVITIES);
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, skinPkgPath);

            Resources superRes = context.getResources();
            Resources skinResource = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
            SkinConfig.saveSkinPath(context, skinPkgPath);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("skinPackageName", mInfo.packageName);
            result.put("skinResource", skinResource);
            skinPath = skinPkgPath;
            isDefaultSkin = false;
            return result;
        } catch (Exception e) {
            LogWriter.e("加载新换肤失败：isDefaultSkin=" + isDefaultSkin, e);
        }
        return null;
    }


    @Override
    public void attach(ISkinUpdate observer) {
        if (skinObservers == null) {
            skinObservers = new ArrayList<ISkinUpdate>();
        }
        if (!skinObservers.contains(observer)) {
            skinObservers.add(observer);
        }
    }

    @Override
    public void detach(ISkinUpdate observer) {
        if (skinObservers == null) return;
        if (skinObservers.contains(observer)) {
            skinObservers.remove(observer);
        }
    }

    @Override
    public void notifySkinUpdate() {
        if (skinObservers == null) return;
        for (ISkinUpdate observer : skinObservers) {
            observer.onThemeUpdate();
        }
    }

    public int getColor(int resId) {
        int originColor = context.getResources().getColor(resId);
        if (mResources == null || isDefaultSkin) {
            return originColor;
        }

        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
        int trueColor = 0;

        try {
            trueColor = mResources.getColor(trueResId);
        } catch (NotFoundException e) {
            trueColor = originColor;
        }

        return trueColor;
    }

    public Drawable getDrawable(int resId) {
        Drawable originDrawable = context.getResources().getDrawable(resId);
        if (mResources == null || isDefaultSkin) {
            return originDrawable;
        }
        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "drawable", skinPackageName);

        Drawable trueDrawable = null;
        try {
            if (android.os.Build.VERSION.SDK_INT < 22) {
                trueDrawable = mResources.getDrawable(trueResId);
            } else {
                trueDrawable = mResources.getDrawable(trueResId, null);
            }
        } catch (NotFoundException e) {
            trueDrawable = originDrawable;
        }

        return trueDrawable;
    }

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。</br>
     * 无皮肤包资源返回默认主题颜色
     *
     * @param resId
     * @return
     * @author pinotao
     */
    public ColorStateList convertToColorStateList(int resId) {
        L.e("attr1", "convertToColorStateList");

        boolean isExtendSkin = true;

        if (mResources == null || isDefaultSkin) {
            isExtendSkin = false;
        }

        String resName = context.getResources().getResourceEntryName(resId);
        L.e("attr1", "resName = " + resName);
        if (isExtendSkin) {
            L.e("attr1", "isExtendSkin");
            int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
            L.e("attr1", "trueResId = " + trueResId);
            ColorStateList trueColorList = null;
            if (trueResId == 0) { // 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
                try {
                    ColorStateList originColorList = context.getResources().getColorStateList(resId);
                    return originColorList;
                } catch (NotFoundException e) {
                    e.printStackTrace();
                    L.e("resName = " + resName + " NotFoundException : " + e.getMessage());
                }
            } else {
                try {
                    trueColorList = mResources.getColorStateList(trueResId);
                    L.e("attr1", "trueColorList = " + trueColorList);
                    return trueColorList;
                } catch (NotFoundException e) {
                    e.printStackTrace();
                    L.w("resName = " + resName + " NotFoundException :" + e.getMessage());
                }
            }
        } else {
            try {
                ColorStateList originColorList = context.getResources().getColorStateList(resId);
                return originColorList;
            } catch (NotFoundException e) {
                e.printStackTrace();
                L.w("resName = " + resName + " NotFoundException :" + e.getMessage());
            }

        }

        int[][] states = new int[1][1];
        return new ColorStateList(states, new int[]{context.getResources().getColor(resId)});
    }

    public int getColorPrimaryDark() {
        if (mResources != null) {
            int identify = mResources.getIdentifier("colorPrimaryDark", "color", skinPackageName);
            return mResources.getColor(identify);
        }
        return -1;
    }

    public int getColorPrimary() {
        if (mResources != null) {
            int identify = mResources.getIdentifier("colorPrimary", "color", skinPackageName);
            return mResources.getColor(identify);
        }
        return -1;
    }
}