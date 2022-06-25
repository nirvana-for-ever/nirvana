package com.nirvana.blog.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nirvana.blog.BuildConfig
import com.nirvana.blog.api.article.ArticleServiceApi
import com.nirvana.blog.db.article.ArticleDataBase
import com.nirvana.blog.entity.db.article.ArticleInfoEntity
import com.nirvana.blog.entity.db.article.ArticleInfoRemoteKey
import com.nirvana.blog.exception.NetworkRespFailException
import com.nirvana.blog.mapper.article.ArticleInfoMapper
import com.nirvana.blog.utils.Constants

@OptIn(ExperimentalPagingApi::class)
class ArticleInfoRemoteMediator(
    private val tagId: String,
    private val api: ArticleServiceApi,
    private val database: ArticleDataBase
) : RemoteMediator<Int, ArticleInfoEntity>() {

    /**
     * 优先于 load 方法调用，只调用一次，就算之后再调用 Adapter.refresh() 刷新还是不会再次调用该方法
     * 只有退出当前 UI 再进来才会调用
     * 用处：
     * 可以用于判断缓存是否过期需要删除的情况，若没过期就返回 SKIP_INITIAL_REFRESH
     * 过期就返回 LAUNCH_INITIAL_REFRESH
     * 但是需要注意，如果有在当前 UI 设置下拉刷新(调用 Adapter.refresh() 的刷新)，
     * 这种刷新并不会调用此方法，因此需要另作判断
     * @return
     * LAUNCH_INITIAL_REFRESH：默认返回值，返回该值则表示后续会发起 refresh 操作
     * 也就是会调用 load 方法，传入的 LoadType 是 REFRESH
     *
     * SKIP_INITIAL_REFRESH：不会调用 LoadType 是 REFRESH 的 load 方法
     */
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    /**
     * 从网络获取数据并且存储到数据库
     * 有一个重点，就是如何获取当前要查询哪一页？
     * 如果是 REFRESH，那就查第一页没问题
     * 如果是 APPEND，就需要讨论一下，有两种方式
     * (1)：通过 load 方法的第二个参数 state 的 state.lastItemOrNull() 方法
     * 该方法返回的是 Room 为我们生成的 PagingSource 的返回值 LoadResult 中的 data 的最后一个 item
     * 如果是null，就表明 REFRESH 压根没加载到数据，
     * 于是直接返回 MediatorResult.Success(endOfPaginationReached = true)
     * 若有数据，因为我们保存在数据库的对象有一个 nextPage 属性
     * 每次查到新数据后，在保存到数据库的时候都会保存一个 nextPage 属性用于告知下一次查询从哪一页开始查
     * !!但是，这种方式存在问题：
     * 可能在构建 PagingState 的时候，PagingSource 还未取得任何数据(REFRESH 还未存到数据库或者还没来得及从数据库中加载)
     * 并且我们需要知道，load 方法是串行执行的，而 PagingState 参数是提前准备好的
     * 而且，官方说明REFRESH发出的时候，优先级最高，可能会插队到APPEND之前
     * 因此当真正执行到 load 方法的时候，有可能 PagingState 参数已经不是最新的了
     * 所以最好用方法2
     * (2)：远程键
     * 新建一个数据库表 HeroRemoteKey，用于保存下一次加载的页数
     * 每当完成一次加载后，就清空该表的数据，添加下一页的页码，因为 load 是串行执行的
     * 在 load 方法中就完成了下一页的页码的更新操作，因此不用担心(1)的情况
     *
     * @param loadType 本次加载的方式，有可能是刷新(REFRESH)、向后页加载数据(APPEND)，向前页加载数据(PREPEND)
     * @param state 包含到目前为止已加载的页面信息、 PagingConfig 对象的相关信息。
     * 注意：获取到的之前加载的数据的类型是数据库对象存储的类型
     * @return
     * MediatorResult.Success(endOfPaginationReached = true)：方法调用没出问题，但是已经到达页底
     * MediatorResult.Success(endOfPaginationReached = false)：方法调用没出问题，并且此次还未到页底
     * MediatorResult.Error(e)：方法出错
     */
    override suspend fun load(loadType: LoadType, state: PagingState<Int, ArticleInfoEntity>): MediatorResult {
        return try {
            val loadPage = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKey = database.getArticleInfoRemoteKeyDao().selectRemoteKey(tagId)
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    remoteKey.nextPage
                }
            }

            val pagingResult = api.pageArticleByTag(
                tagId,
                loadPage,
                when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            ).run {
                if (!success) {
                    throw NetworkRespFailException("ArticleInfo 加载错误. $message code:$code")
                }
                data
            }

            // 数据保存在数据库中，放在事务里做
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // 如果是刷新就将数据库的缓存全部清空
                    database.getArticleDao().clearArticleInfos(tagId)
                }
                // 远程键也要清空
                database.getArticleInfoRemoteKeyDao().deleteRemoteKey(tagId)

                // 获取下一页的页码，存入远程键，作为下一次查找的依据
                val nextPage = when (loadType) {
                    LoadType.REFRESH -> Constants.ARTICLE_INFO_INITIAL_PAGE_MULTIPLIER + 1
                    else -> loadPage + 1
                }
                // 将查到的当前页数据存入数据库
                database.getArticleDao().insertArticleInfo(pagingResult!!.list.map { ArticleInfoMapper.reverseMap(it, tagId) })
                // 添加新的远程键
                database.getArticleInfoRemoteKeyDao().insertRemoteKey(ArticleInfoRemoteKey(tagId, nextPage))
            }

            // 若还有下一页，endOfPaginationReached 就返回 true，否则就false，以后再刷就不会请求新数据了
            MediatorResult.Success(endOfPaginationReached = !pagingResult!!.hasNext)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            MediatorResult.Error(e)
        }
    }
}