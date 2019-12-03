package com.sjl.core.util;

import android.os.Build;

import com.sjl.core.util.log.LogUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类转换初始化
 *
 * @author songjiali
 * @version 1.0.0
 * @filename TUtils.java
 * @time 2018/10/22 17:25
 * @copyright(C) 2018 song
 */
public class TUtils {



    /**
     * 获取到父类泛型的类型
     *
     * @param o
     * @param index
     * @param <T>
     * @return
     */
    public static <T> T getT(Object o, int index) {
        try {
            // 获取当前运行类泛型父类类型，即为参数化类型，有所有类型公用的高级接口Type接收!
            Type type = o.getClass().getGenericSuperclass();
            if (Build.VERSION.SDK_INT >= 28) {
                LogUtils.i("父类类型名称:" + type.getTypeName());
            }

            if (!(type instanceof ParameterizedType)) {
                return null;
            }
            String string = type.toString();
            LogUtils.e("当前泛型T:" + string);
            // 强转为“参数化类型”，ParameterizedType参数化类型，即泛型
            ParameterizedType pt = (ParameterizedType) type;
            // 获取参数化类型中，实际类型的定义
            Type[] params = pt.getActualTypeArguments();

            if (index >= params.length || index < 0) {
                return null;
            }
            // 转换
            //com.sjl.swimchat.ui.presenter.ExpressPresenter
            Class<T> clz = (Class<T>) params[index];
            //实例化
            T t = clz.newInstance();//会触发默认构造器，可以在默认构造器初始化参数
            return t;
        } catch (Exception e) {
            LogUtils.w(e);
        }
        return null;
    }


    /**
     * 加载class
     *
     * @param className 类全路径
     * @return
     */
    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getInterfaceT(Object o, int index) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[index];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);

    }

    /**
     * 验证Type类型
     *
     * @param type
     * @param index
     * @return
     */
    private static Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type t = pt.getActualTypeArguments()[index];
            return checkType(t, index);//直到Type是class为止
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }


    /**
     * 获取接口上的泛型T,取第一个
     *
     * @param o
     */
    public static Class<?> getInterfaceT0(Object o) {
        return getInterfaceT(o, 0);
    }
}
