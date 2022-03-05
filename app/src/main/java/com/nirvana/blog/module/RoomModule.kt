package com.nirvana.blog.module

import android.app.Application
import com.nirvana.blog.db.article.ArticleDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun articleDatabase(app: Application): ArticleDataBase = ArticleDataBase.get(app)

}