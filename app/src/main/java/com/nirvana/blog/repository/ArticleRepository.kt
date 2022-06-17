package com.nirvana.blog.repository

import androidx.paging.*
import com.nirvana.blog.api.article.ArticleServiceApi
import com.nirvana.blog.base.BaseRepository
import com.nirvana.blog.db.article.ArticleDataBase
import com.nirvana.blog.entity.ui.article.ArticleInfo
import com.nirvana.blog.mapper.article.ArticleInfoMapper
import com.nirvana.blog.paging.ArticleInfoRemoteMediator
import com.nirvana.blog.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class ArticleRepository(
    private val api: ArticleServiceApi,
    private val database: ArticleDataBase
) : BaseRepository() {

    private val pagerMap: MutableMap<String, Flow<PagingData<ArticleInfo>>> = mutableMapOf()

    fun articleInfoFlow(tagId: String): Flow<PagingData<ArticleInfo>> {
        return pagerMap.getOrPut(tagId) {
            Pager(
                config = PagingConfig(
                    pageSize = Constants.ARTICLE_INFO_PAGE_SIZE,
                    initialLoadSize = Constants.INITIAL_PAGE_MULTIPLIER * Constants.ARTICLE_INFO_PAGE_SIZE
                ),
                // 添加 remoteMediator，作为网络查询
                remoteMediator = ArticleInfoRemoteMediator(tagId, api, database),
                // 数据库缓存取出
                pagingSourceFactory = {
                    database.getArticleDao().articleInfoPagingSource(tagId)
                }
            ).flow
                // 从数据库查出的对象是 ArticleInfoEntity，需要转化成 ArticleInfo 放入 LiveData 才能供 UI 层使用
                .map { pagingData ->
                    pagingData.map { ArticleInfoMapper.map(it) }
                }
                .flowOn(Dispatchers.IO)
        }
    }

    suspend fun getArticle(articleId: String) = safeReq { api.getArticle(articleId) }

    suspend fun likeArticle(articleId: String, order: Map<String, String>) =
        safeReq { api.likeArticle(articleId, order) }

}

