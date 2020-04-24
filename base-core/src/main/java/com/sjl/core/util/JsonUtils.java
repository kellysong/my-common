package com.sjl.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sjl.core.annotation.FieldName;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename JsonUtils.java
 * @time 2018/8/3 8:35
 * @copyright(C) 2018 song
 */
public class JsonUtils {
    /**
     * 检查字符串不是json格式
     *
     * @param json
     * @return
     */
    public static boolean isBadJson(String json) {
        return !isGoodJson(json);
    }

    /**
     * 检查字符串是json格式
     *
     * @param json
     * @return
     */
    public static boolean isGoodJson(String json) {
        try {
            new JSONObject(json);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }


    /**
     * bean转map
     *
     * @param bean        转转bean
     * @param excludeNull 是否排除掉字段值为空的字段
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> beanToMap(Object bean, boolean excludeNull) {
        Map<String, Object> result = new LinkedHashMap<>();
        Class<?> sourceClass = bean.getClass();
        //拿到所有的字段,不包括继承的字段
        Field[] sourceFiled = sourceClass.getDeclaredFields();
        try {
            for (Field field : sourceFiled) {
                field.setAccessible(true);
                //设置可访问,不然拿不到private
                FieldName fieldName = field.getAnnotation(FieldName.class);
                Object o = field.get(bean);
                if (excludeNull) {//排除空值的字段
                    if (o == null || o.equals("")) {
                        continue;
                    }
                }
                //没有注解
                if (fieldName == null) {
                    result.put(field.getName(), o);
                } else {
                    if (fieldName.Ignore()) continue;
                    result.put(fieldName.value(), o);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    /**
     * bean转map
     *
     * @param bean 转转bean
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> beanToMap(Object bean) {
        return beanToMap(bean, false);
    }

    /**
     * 转换和赋值
     *
     * @param sourceObj     原数据对象
     * @param methodName    需要赋值原数据对象的方法名
     * @param filedObjValue 需要处理的字段对象值
     * @param filedMapClass 处理的字段对象映射Class
     */
    public static void performTransformWithEvaluation(Object sourceObj, String methodName, Object filedObjValue,
                                                      Class filedMapClass) {
        List<Object> listObjects = performTransform(filedObjValue, filedMapClass);
        Class<?> clazz = sourceObj.getClass();
        Method method = null;
        try {
            method = clazz.getMethod(methodName, Object.class);
            method.invoke(sourceObj, listObjects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统一转换成List输出，对于基本类型，取出集合中的值即可
     *
     * @param filedObjValue 需要处理的字段对象值
     * @param filedMapClass 处理的字段对象映射Class
     * @return
     */
    public static <T> List<T> performTransform(Object filedObjValue, Class filedMapClass) {
        List<T> beanList = new ArrayList<T>();
        Gson gson = new Gson();
        String jsonStr = gson.toJson(filedObjValue);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonStr);
        if (element.isJsonObject()) {
            // 把JsonElement对象转换成JsonObject
            T t = (T) gson.fromJson(element, filedMapClass);
            beanList.add(t);
        } else if (element.isJsonArray()) {
            //下面会导致T为LinkedTreeMap，说明Gson解析时不支持泛型
//            Type type = new TypeToken<List<T>>() {}.getType();
//            // 把JsonElement对象转换成JsonArray
//            List<T> list = gson.fromJson(element, type);


            List<T> list = jsonToList(element,filedMapClass);
            beanList.addAll(list);
        } else if (element.isJsonPrimitive()) {
            T t = (T) gson.fromJson(element, filedMapClass);
            beanList.add(t);
        } else {
            // element.isJsonNull()
            return null;
        }
        return beanList;
    }

    /**
     * 通过json字符串转List
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = new Gson().fromJson(json, type);
        return list;
    }

    /**
     * 通过element转List
     * @param element
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(JsonElement element, Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = new Gson().fromJson(element, type);
        return list;
    }

    /**
     * 自定义ParameterizedType
     */
    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        /**
         * 返回实际类型组成的数据,比如Map<String,Long> map的类型是：java.lang.String、java.lang.Long
         * @return
         */
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] { clazz };
        }

        /**
         * 返回原生类型，比如Map<String,Long> map的原生类型为java.util.Map
         * @return
         */
        @Override
        public Type getRawType() {
            return List.class;
        }

        /**
         * 返回 Type 对象，表示此类型是其成员之一的类型,比如Map.Entry<Long,Short> map的OwnerType为java.util.Map
         * @return
         */
        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    /**
     * 格式化json字符串
     *
     * @param jsonStr 需要格式化的json串
     * @return 格式化后的json串
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                //遇到,换行
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }


    /**
     * http 请求数据返回 json 中中文字符为 unicode 编码转汉字转码
     *
     * @param theString
     * @return 转化后的结果.
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
