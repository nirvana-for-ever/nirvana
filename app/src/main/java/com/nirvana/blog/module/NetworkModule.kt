package com.nirvana.blog.module

import android.util.Log
import com.nirvana.blog.BuildConfig
import com.nirvana.blog.api.article.ArticleServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun okhttpClient(): OkHttpClient {
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
            build()
        }
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("${BuildConfig.BASE_URL}/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun blogServiceApi(retrofit: Retrofit): ArticleServiceApi = retrofit.create(ArticleServiceApi::class.java)

}