package com.sjl.core.language

import android.R
import android.annotation.TargetApi
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import com.sjl.core.util.PreferencesHelper
import java.util.*

/**
 * 国际化语言管理，适配8.0以上及以下
 * @author Kelly
 * @version 1.0.0
 * @filename LanguageManager.kt
 * @time 2019/7/22 16:46
 * @copyright(C) 2019 song
 */
const val LANGUAGE_TYPE = "language_type"

object LanguageManager {
    private val mSupportLanguages = object : HashMap<Int, Locale>(5) {
        init {
            val values = LanguageType.values();
            for (v in values) {
                put(v.type, v.locale)
            }
        }
    }

    /**
     * 语言上下文
     */
    var context: Context? = null


    /**
     * 初始化app语言
     *
     * @param context 对于8.0以下的系统， 上文代码中的 mContext 采用 ApplicationContext 可以正确的切换应用的语言类型
     * 但在8.0 系统中，若 mContext 采用 ApplicationContext 则无法切换应用的语言类型,必须是activity的Context
     */
    fun initAppLanguage(context: Context) {
        changeLanguage(context, getCurrentLanguageType(context))
    }


    /**
     * 初始化app语言
     *
     * @param context 对于8.0以下的系统， 上文代码中的 mContext 采用 ApplicationContext 可以正确的切换应用的语言类型
     * 但在8.0 系统中，若 mContext 采用 ApplicationContext 则无法切换应用的语言类型,必须是activity的Context
     * @param languageType 默认语言类型
     */
    fun initAppLanguage(context: Context, languageType: Int) {
        changeLanguage(context, getCurrentLanguageType(context, languageType))
    }

    /**
     * 改变语言
     *
     * @param context 上下文
     * @param languageType 语言种类
     */
    fun changeLanguage(context: Context, languageType: Int?) {
//        LogUtils.i("1.languageType:" + languageType + ",get LocaleString:" + getLocaleString(Locale.getDefault()))
        var locale: Locale
        if (languageType == null) {//如果没有指定语言使用系统首选语言
            locale = getSystemPreferredLanguage(context)
        } else {//指定了语言使用指定语言
            locale = getSupportLanguage(context, languageType)!!
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager: LocaleManager =
                context.getSystemService<LocaleManager>(LocaleManager::class.java)
            if (localeManager != null) {
                if (languageType == null) {
                    localeManager.applicationLocales = LocaleList.getEmptyLocaleList()
                } else {
                    localeManager.applicationLocales = LocaleList(locale)
                }
            }

        }
        val attachBaseContext = LanguageUtils.attachBaseContext(context, locale)
        LanguageManager.context = attachBaseContext
//        LogUtils.i("2.languageType:" + languageType + ",get LocaleString:" + getLocaleString(Locale.getDefault()))
        PreferencesHelper.getInstance(context).put(LANGUAGE_TYPE, languageType)
    }


    fun getLocalContext(context: Context): Context {
        var languageType = getCurrentLanguageType(context)
        var locale: Locale
        if (languageType == null) {//如果没有指定语言使用系统首选语言
            locale = getSystemPreferredLanguage(context)
        } else {//指定了语言使用指定语言
            locale = getSupportLanguage(context, languageType)!!
        }
        val attachBaseContext = LanguageUtils.attachBaseContext(context, locale)
        return attachBaseContext
    }

    /**
     * 是否支持此语言
     *
     * @param language language
     * @return true:支持 false:不支持
     */
    fun isSupportLanguage(language: Int): Boolean {
        return mSupportLanguages.containsKey(language)
    }

    /**
     * 获取支持语言
     *
     * @param context
     * @param language language
     * @return 支持返回支持语言，不支持返回系统首选语言
     */
    @TargetApi(Build.VERSION_CODES.N)
    fun getSupportLanguage(context: Context, language: Int): Locale? {
        if (isSupportLanguage(language)) {
            return mSupportLanguages[language]
        } else {
            return getSystemPreferredLanguage(context)
        }
    }

    /**
     * 获取系统首选语言
     *
     * @return Locale
     */

    fun getSystemPreferredLanguage(context: Context): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 在 Android 13 上，不能用 Resources.getSystem() 来获取系统语种了,Android 13 上面新增了一个 LocaleManager 的语种管理类
            // 因为如果调用 LocaleManager.setApplicationLocales 会影响获取到的结果不准确,所以应该得用 LocaleManager.getSystemLocales 来获取会比较精准
            val localeManager = context.getSystemService(LocaleManager::class.java)
            if (localeManager != null) {
                return localeManager.systemLocales[0]
            }
        }
        val locale: Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0)
        } else {
            locale = Locale.getDefault()
        }
        return locale
    }

    /**
     * 获取当前语言的索引
     *@param context
     * @param languageType 默认值
     */
    fun getCurrentLanguageType(context: Context, languageType: Int): Int {
        var type = PreferencesHelper.getInstance(context).getInteger(LANGUAGE_TYPE, languageType)
        if (type <= 0) {
            type = 0
        }
        return type
    }

    /**
     * 获取当前语言的索引
     *@param context
     */
    fun getCurrentLanguageType(context: Context): Int {
        var type = PreferencesHelper.getInstance(context).getInteger(LANGUAGE_TYPE, 0)
        if (type <= 0) {
            type = 0
        }
        return type
    }

    private fun getLocaleString(locale: Locale): String {
        if (locale == null) {
            return "";
        } else {
            return locale.language + locale.country;
        }
    }

    /**
     * 切换语言
     *
     * @param context
     * @param languageType
     * @param targetClass
     */
    fun changeAppLanguage(context: Context, languageType: Int, targetClass: Class<*>) {
        val language = getCurrentLanguageType(context)
        if (language != languageType) {
            changeLanguage(context, languageType)
            recreateActivity(context, targetClass)
        }
    }

    /**
     * 重新创建Activity
     *
     * @param context
     * @param clazz
     */
    fun recreateActivity(context: Context, clazz: Class<*>) {
        val intent = Intent(context, clazz)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}