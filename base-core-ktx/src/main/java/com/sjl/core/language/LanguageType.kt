package com.sjl.core.language

import java.util.*

/**
 * 语言常量
 *
 * @author Kelly
 * @version 1.0.0
 * @filename LanguageConstant.java
 * @time 2019/7/24 9:32
 * @copyright(C) 2019 song
 */

enum class LanguageType(val type: Int,val text: String,val locale: Locale) {



    LANGUAGE_TYPE_CN(0,"中文", Locale.SIMPLIFIED_CHINESE),
    LANGUAGE_TYPE_EN(1,"English",Locale.SIMPLIFIED_CHINESE);


    fun languageTextList(): Array<String> {
        val values = values();
        val list: MutableList<String> = ArrayList()
        for (v in values) {
            list.add(v.text)
        }
        return list.toTypedArray();
    }

}