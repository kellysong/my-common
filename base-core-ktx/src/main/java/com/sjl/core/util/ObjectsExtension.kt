package com.sjl.core.util

import org.json.JSONArray

/**
 * TODO
 * @author Kelly
 * @version 1.0.0
 * @filename ObjectsExtension
 * @time 2021/10/21 14:30
 * @copyright(C) 2021 song
 */

fun String.equals(a: String?): Boolean {
    return this == a || a != null && this == a
}

fun Any.isEmpty(content: Any?): Boolean {
    if (content == null || content == "") {
        return true
    } else if (content is Collection<*> && content.size == 0) {
        return true
    } else if (content is Array<*> && content.size == 0) {
        return true
    } else if (content is JSONArray && content.length() == 0) {
        return true
    }
    return false
}
fun Any.isNotEmpty(content: Any?): Boolean {
   return isEmpty(content)
}
