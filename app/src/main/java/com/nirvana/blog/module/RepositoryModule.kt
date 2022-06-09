package com.nirvana.blog.module

import com.nirvana.blog.api.user.AccountServiceApi
import com.nirvana.blog.api.article.ArticleServiceApi
import com.nirvana.blog.api.tag.TagServiceApi
import com.nirvana.blog.db.article.ArticleDataBase
import com.nirvana.blog.db.tag.TagDataBase
import com.nirvana.blog.repository.AccountRepository
import com.nirvana.blog.repository.ArticleRepository
import com.nirvana.blog.repository.TagRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun articleRepository(api: ArticleServiceApi, database: ArticleDataBase): ArticleRepository =
        ArticleRepository(api, database)

    @ViewModelScoped
    @Provides
    fun accountRepository(api: AccountServiceApi): AccountRepository =
        AccountRepository(api)

    @ViewModelScoped
    @Provides
    fun tagRepository(api: TagServiceApi, dataBase: TagDataBase): TagRepository =
        TagRepository(api, dataBase)

}