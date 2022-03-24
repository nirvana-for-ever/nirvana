package com.nirvana.blog.entity.ui.article

import android.os.Bundle

/**
 * 仅包含博客的重要信息，不包含博客的全部内容
 */
data class ArticleInfo(
    val articleId: String,
    val authorId: String,
    var authorName: String,
    var authorAvatar: String,
    var title: String,
    var content: String?,
    var img1: String?,
    var img2: String?,
    var img3: String?,
    var readCount: String,
    var commentCount: String,
    var likeCount: String,
)

/**
 * DiffCallback 中高效率更新 item 方法 getChangePayload 的返回值
 * 更新的时候调用下面的 updateBundleFrom 方法
 * 本方法是获取有哪些属性有更新，通过一个 boolean 数组来保存
 */
fun ArticleInfo.diffBundleFrom(from: ArticleInfo): Bundle? {
    return Bundle().run {
        val arr = BooleanArray(10)
        if (authorName != from.authorName) arr[0] = true
        if (authorAvatar != from.authorAvatar) arr[1] = true
        if (title != from.title) arr[2] = true
        if (content != from.content) arr[3] = true
        if (img1 != from.img1) arr[4] = true
        if (img2 != from.img2) arr[5] = true
        if (img3 != from.img3) arr[6] = true
        if (readCount != from.readCount) arr[7] = true
        if (commentCount != from.commentCount) arr[8] = true
        if (likeCount != from.likeCount) arr[9] = true

        if (size() != 0) {
            putBooleanArray(javaClass.simpleName, arr)
            this
        } else {
            null
        }
    }
}

fun ArticleInfo.updateBundleFrom(from: ArticleInfo, bundle: Bundle) {
    val arr = bundle.getBooleanArray(javaClass.simpleName)!!
    if (arr[0]) authorName = from.authorName
    if (arr[1]) authorAvatar = from.authorAvatar
    if (arr[2]) title = from.title
    if (arr[3]) content = from.content
    if (arr[4]) img1 = from.img1
    if (arr[5]) img2 = from.img2
    if (arr[6]) img3 = from.img3
    if (arr[7]) readCount = from.readCount
    if (arr[8]) commentCount = from.commentCount
    if (arr[9]) likeCount = from.likeCount
}
