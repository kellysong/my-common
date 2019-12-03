package com.sjl.core.util;

import android.os.Environment;

import com.sjl.core.app.BaseApplication;
import com.sjl.core.util.log.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具类
 *
 * @author Kelly
 * @version 1.0.0
 * @filename SerializeUtils.java
 * @time 2018/10/5 13:05
 * @copyright(C) 2018 xxx有限公司
 */
public class SerializeUtils {
    /**
     * 对象序列化
     *
     * @param fileName 文件名
     * @param obj      对象
     * @return
     */
    public static boolean serialize(String fileName, Object obj) {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + BaseApplication.getContext().getPackageName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            File file = new File(dir + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            return true;
        } catch (IOException e) {
            LogUtils.e("序列化写文件异常", e);
        }
        return false;
    }

    /**
     * 反序列化
     *
     * @param fileName 文件名
     * @return
     */
    public static Object deserialize(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + BaseApplication.getContext().getPackageName() + File.separator + fileName);
        if (!file.exists()) {
            LogUtils.i("文件不存在，反序列化失败");
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Object object = ois.readObject();
            ois.close();
            return object;
        } catch (IOException e) {
            LogUtils.e("序列化读文件异常", e);
        } catch (ClassNotFoundException e) {
            LogUtils.e(fileName + "没找到", e);
        }
        return null;
    }

    /**
     * 反序列化为指定类型
     *
     * @param fileName 文件名
     * @param clz      泛型class
     * @return
     */
    public static <T> T deserialize(String fileName, Class<?> clz) {
        Object obj = deserialize(fileName);
        return obj != null ? (T) clz.cast(obj) : null;
    }
}
