package com.nirvana.blog.module

import com.nirvana.blog.api.article.ArticleServiceApi
import com.nirvana.blog.db.article.ArticleDataBase
import com.nirvana.blog.repository.ArticleRepository
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

}