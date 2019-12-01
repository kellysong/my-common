package com.sjl.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类转换初始化
 *
 * @author songjiali
 * @version 1.0.0
 * @filename TUtils.java
 * @time 2018/10/22 17:25
 * @copyright(C) 2018 深圳市海恒智能科技有限公司
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
            if (!(type instanceof ParameterizedType)) {
                return null;
            }
            String string = type.toString();
//            LogUtils.e("当前泛型T:" + string);
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

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
