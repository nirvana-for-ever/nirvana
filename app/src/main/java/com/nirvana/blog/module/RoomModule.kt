package com.nirvana.blog.module

import android.app.Application
import com.nirvana.blog.db.article.ArticleDataBase
import com.nirvana.blog.db.message.MessageDataBase
import com.nirvana.blog.db.tag.TagDataBase
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

    @Provides
    @Singleton
    fun tagDatabase(app: Application): TagDataBase = TagDataBase.get(app)

    @Provides
    @Singleton
    fun messageDatabase(app: Application): MessageDataBase = MessageDataBase.get(app)

}