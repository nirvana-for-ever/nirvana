package com.nirvana.blog.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AppUpdateBean (
        @SerializedName("success")val success:Boolean,
        @SerializedName("code")val code:Int,
        @SerializedName("message")val message:String,
        @SerializedName("data")val data:AppUpdateListBean
): Serializable

class AppUpdateListBean(
        @SerializedName("versionId")val versionId:String,
        @SerializedName("versionName")val versionName:String,
        @SerializedName("downloadUrl")val downloadUrl:String,
        @SerializedName("publishDate")val publishDate:String,
        @SerializedName("versionCode")val versionCode:String,
        @SerializedName("isForce")val isForce:Boolean,
        @SerializedName("message")val message:String
): Serializable

class AppUpdateResultBean{
        var versionId:String = ""
        var versionName:String = ""
        var downloadUrl:String = ""
        var publishDate:String = ""
        var versionCode:String = ""
        var isForce:Boolean = false
        var message:String = ""
}