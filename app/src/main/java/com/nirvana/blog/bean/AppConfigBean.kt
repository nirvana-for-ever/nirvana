package com.nirvana.blog.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AppConfigBean {
        var isForce:String = ""
        var versionName:String = ""
        var versionCode:Int = 0
        var fileName:String = ""
        var customer:String = ""
        var message = arrayListOf<String>()
        var serviceAddress:String = ""
        var printServiceAddress:String = ""
        var weightAddress:String = ""
        var isRedundancy:String = ""
        var weightType:String = ""
        var redundancyAddress:String = ""
        var serialSelect:String = ""
}