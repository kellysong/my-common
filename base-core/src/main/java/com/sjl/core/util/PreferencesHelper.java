
package com.sjl.core.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * SharedPreferences辅助类
 *
 * @author songjiali
 * @version 1.0.0
 * @filename PreferencesHelper.java
 * @time 2018/10/22 17:25
 * @copyright(C) 2018 song
 */
public class PreferencesHelper {
    private static final String PRE_NAME = "app_config";
    private static PreferencesHelper preferencesHelper;
    private Context context;
    private SharedPreferences sp;

    private PreferencesHelper(Context context) {
        boolean b = context instanceof Application;
        if (!b) {
            this.context = context.getApplicationContext();
        }else {
            this.context = context;
        }
        initSharedPreferences();
    }

    private void initSharedPreferences() {
        sp = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取唯一实例
     *
     * @param context
     * @return
     */
    public static PreferencesHelper getInstance(Context context) {
        if (preferencesHelper == null) {
            preferencesHelper = new PreferencesHelper(context);
        }
        return preferencesHelper;
    }

    /**
     * 根据key保存数据,异步提交
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        Editor editor = createEditor(key, value);
        if (editor != null) {
            editor.apply();
        }
    }

    /**
     * 根据key保存数据,同步提交
     *
     * @param key
     * @param value
     */
    public void putSync(String key, Object value) {
        Editor editor = createEditor(key, value);
        if (editor != null) {
            editor.commit();
        }
    }

    /**
     * 创建sp编辑器
     *
     * @param key
     * @param value
     * @return
     */
    private Editor createEditor(String key, Object value) {
        if (key == null || value == null) {
            return null;
        }
        Editor editor = sp.edit();
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set<String>) value);
        } else if (value instanceof String) {
            editor.putString(key, String.valueOf(value));
        }
        return editor;
    }


    /**
     * 保存Map集合
     *
     * @param map
     */
    public <T> void putAll(Map<String, T> map) {
        for (Map.Entry<String, T> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            put(key, value);
        }
    }

    /**
     * 保存List集合
     *
     * @param key
     * @param list
     */
    public void putAll(String key, List<String> list) {
        putAll(key, list, new ComparatorImpl());
    }

    public void putAll(String key, List<String> list, Comparator<String> comparator) {
        Set<String> set = new TreeSet<>(comparator);
        for (String value : list) {
            set.add(value);
        }
        sp.edit().putStringSet(key, set).apply();
    }

    /**
     * 根据key取出一个数据
     *
     * @param key 键
     */
    public Object get(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return sp.getStringSet(key, null);
        }
    }

    /**
     * 根据key删除数据
     *
     * @param key
     */
    public void remove(String key) {
        if (contains(key)) {
            sp.edit().remove(key).apply();
        }
    }

    /**
     * 删除一个集合key
     *
     * @param keys
     */
    public void removeAll(List<String> keys) {
        Editor edit = sp.edit();
        for (String k : keys) {
            edit.remove(k);
        }
        edit.apply();
    }

    /**
     * 删除数组key
     *
     * @param keys
     */
    public void removeAll(String[] keys) {
        removeAll(Arrays.asList(keys));
    }

    public boolean contains(String key) {
        return sp.contains(key);
    }

    public void clear() {
        sp.edit().clear().apply();
    }

    public String getString(String key, String defaultValue) {
        return (String) get(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return (float) get(key, defaultValue);
    }

    public int getInteger(String key, int defaultValue) {
        return (int) get(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return (long) get(key, defaultValue);
    }

    public Set<String> getSet(String key, Set<String> defaultValue) {
        return (Set<String>) get(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return (boolean) get(key, defaultValue);
    }

    /**
     * 默认比较器，当存储List集合中的String类型数据时，没有指定比较器，就使用默认比较器
     */
    public class ComparatorImpl implements Comparator<String> {

        @Override
        public int compare(String lhs, String rhs) {
            return lhs.compareTo(rhs);
        }
    }
}
