package com.sjl.core.language

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 *
 * 不需要替换Context,替换会导致某些SDK方法调用存在安全隐患
 * 参考:
 *
 * @see <a href="https://blog.csdn.net/haha_zhan/article/details/81331719">https://blog.csdn.net/haha_zhan/article/details/81331719</a>
 *
 * @author Kelly
 * @version 1.0.0
 * @filename LanguageUtils.java
 * @time 2019/7/23 16:10
 * @copyright(C) 2019 song
 */
object LanguageUtils {


    fun attachBaseContext(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                configuration.setLocale(locale)
                val localeList = LocaleList(locale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                Locale.setDefault(locale)
                LocaleList.setDefault(localeList);
                resources.updateConfiguration(configuration, resources.displayMetrics)        //必须加,否则无效
                return context.createConfigurationContext(configuration)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
                configuration.setLocale(locale)
                Locale.setDefault(locale)
                return context.createConfigurationContext(configuration);
            }
            else -> {

                configuration.locale = locale;
                Locale.setDefault(locale)
                resources.updateConfiguration(configuration, resources.displayMetrics);
                return context
            }
        }
    }
}
