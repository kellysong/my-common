package com.sjl.lib.entity

/**
 * 响应结果数据
 * @param out T
 * @property errorCode Int
 * @property errorMsg String
 * @property data T 泛型，对应具体的bean
 * @constructor
 */
data class ResponseData<out T>(val errorCode: Int, val errorMsg: String, val data: T)

data class ArticleBean(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)