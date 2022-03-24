package com.nirvana.blog.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.nirvana.blog.BuildConfig
import com.nirvana.blog.api.user.AccountServiceApi
import com.nirvana.blog.api.article.ArticleServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun cookiesSharedPreferences(app: Application): SharedPreferences =
        app.getSharedPreferences("cookies", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun okhttpClient(sharedPreferences: SharedPreferences): OkHttpClient {
        return OkHttpClient.Builder().run {
            // 开发环境就需要打印日志
            if (BuildConfig.DEBUG) {
                // 添加 HttpLoggingInterceptor 拦截器，作用是：响应结果的打印
                addInterceptor(HttpLoggingInterceptor {
                    Log.d("blog", it)
                }.apply {
                    // 这行必须加 不然默认不打印
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                })
            }

            // cookie 配置
            cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookies: MutableList<Cookie> = mutableListOf()
                    println("准备发起请求，遍历当前所有的cookie====================")
                    sharedPreferences.all.forEach {
                        println("key:${it.key}, value:${it.value}")
                        val cookie = Cookie.Companion.parse(url, it.value as String)!!
                        if (cookie.expiresAt > System.currentTimeMillis()) {
                            println("cookie对象:$cookie")
                            cookies.add(cookie)
                        } else {
                            sharedPreferences.edit().apply {
                                remove(it.key)
                                apply()
                            }
                        }
                    }
                    return cookies
                }
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    println("接收到响应，遍历让我们保存的cookie----------------------")
                    sharedPreferences.edit().apply {
                        for (cookie in cookies) {
                            println("响应cookie:$cookie")
                            putString(cookie.name, cookie.toString())
                        }
                        apply()
                    }
                }
            })
            build()
        }
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun articleServiceApi(retrofit: Retrofit): ArticleServiceApi = retrofit.create(ArticleServiceApi::class.java)

    @Provides
    @Singleton
    fun accountServiceApi(retrofit: Retrofit): AccountServiceApi = retrofit.create(AccountServiceApi::class.java)

}