package com.nirvana.blog.api.article

import com.nirvana.blog.entity.ui.article.ArticleInfo
import com.nirvana.blog.entity.network.PagingResult
import com.nirvana.blog.entity.network.RespResult
import com.nirvana.blog.entity.ui.article.Article
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleServiceApi {

    /**
     * 分页查询某个标签的文章
     * @param tagId 查询对应标签的文章
     * @param page 查询第几页
     * @param pageSize 查几条数据
     */
    @GET("main/article/info/{tagId}/{page}")
    suspend fun pageArticleByTag(
        @Path("tagId") tagId: String,
        @Path("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): RespResult<PagingResult<ArticleInfo>>

    /**
     * 获取文章的内容
     */
    @GET("main/article/{articleId}")
    suspend fun getArticle(@Path("articleId") articleId: String): RespResult<Article>

}