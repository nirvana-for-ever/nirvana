package com.nirvana.blog.utils

object Constants {
    /************************************ArticleInfo****************************************/
    /**
     * paging 有关参数：一页几条数据
     */
    const val ARTICLE_INFO_PAGE_SIZE = 7

    /**
     * paging 有关参数：初始化多少页
     */
    const val ARTICLE_INFO_INITIAL_PAGE_MULTIPLIER = 3

    /************************************Article****************************************/
    /**
     * 点赞或取消点赞发送请求的 key
     */
    const val ARTICLE_LIKE_ORDER_KEY = "order"
    /**
     * 点赞发送请求的 value
     */
    const val ARTICLE_LIKE_ON = "on"
    /**
     * 取消点赞发送请求的 value
     */
    const val ARTICLE_LIKE_OFF = "off"

    /************************************用户登录****************************************/
    /**
     * 登录方式：验证码登录
     */
    const val CODE_LOGIN = -1

    /**
     * 登录方式：账号密码登录
     */
    const val PASSWORD_LOGIN = -2

    /**
     * 登录成功响应码，仅用于页面之前传递时使用
     */
    const val LOGIN_SUCCESS = 200

    /**
     * 未完成登录响应码，仅用于页面之前传递时使用
     */
    const val LOGIN_UNFINISHED = 500

    fun isLoginSuccess(code: Int) = when(code) {
        LOGIN_SUCCESS -> true
        else -> false
    }

    /************************************用户信息****************************************/
    /**
     * 用户信息获取类型-获取简单信息
     */
    const val GET_USER_INFO_SIMPLE = -1
    /**
     * 用户信息获取类型-获取详细信息
     */
    const val GET_USER_INFO_DETAIL = -2

    /************************************标签信息****************************************/
    /**
     * 标签喜好是否变动：变动了
     */
    const val INDEX_TAG_SETTING_CHANGED = 1
    /**
     * 标签喜好是否变动：没变动
     */
    const val INDEX_TAG_SETTING_UNCHANGED = 2

    /************************************消息****************************************/
    /**
     * 互动消息：评论
     */
    const val MESSAGE_INTERACTION_COMMENT_TYPE = 1
    /**
     * 互动消息：点赞
     */
    const val MESSAGE_INTERACTION_LIKE_TYPE = 2
    /**
     * 互动消息：关注
     */
    const val MESSAGE_INTERACTION_SUBSCRIBE_TYPE = 3

    /**
     * paging 有关参数：一页几条数据
     */
    const val MESSAGE_PAGE_SIZE = 10

    /**
     * paging 有关参数：初始化多少页
     */
    const val MESSAGE_INITIAL_PAGE_MULTIPLIER = 3


    /************************************通用常量****************************************/
    /**
     * 邮箱正则
     */
    const val EMAIL_REGEX = "^\\s*\\w+(?:\\.?[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"

    /**
     * 手机号正则
     */
    const val PHONE_REGEX = "^1[3456789]\\d{9}$"

    /**
     * 服务器错误提醒
     */
    const val SERVER_ERROR_MSG = "服务器发烧了，请稍后再试"
}