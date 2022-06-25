package com.nirvana.blog.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nirvana.blog.BuildConfig
import com.nirvana.blog.api.message.MessageServiceApi
import com.nirvana.blog.db.message.MessageDataBase
import com.nirvana.blog.entity.db.message.MessageRemoteKey
import com.nirvana.blog.entity.network.RespResult
import com.nirvana.blog.entity.ui.message.MessageComment
import com.nirvana.blog.entity.ui.message.MessageLike
import com.nirvana.blog.utils.Constants
import com.nirvana.blog.utils.messageCommentClientTime
import com.nirvana.blog.utils.messageLikeClientTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InteractionMessagePagingSource(
    private val api: MessageServiceApi,
    private val dataBase: MessageDataBase,
    private val flag: Int
) : PagingSource<Int, Any>() {

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        return try {
            val page = params.key ?: 1
            val size = params.loadSize

            // 获取远程键的数据，就是上一页的最后一条数据的 id
            // 第一页的时候不需要获取
            var remoteKey: MessageRemoteKey? = null
            if (page != 1) {
                withContext(Dispatchers.IO) {
                    remoteKey = dataBase.getMessageDao().selectRemoteKey(flag)
                }
            }

            val nextKey = if (page == 1) Constants.MESSAGE_INITIAL_PAGE_MULTIPLIER + 1 else page + 1

            return when (flag) {
                Constants.MESSAGE_INTERACTION_LIKE_TYPE -> {
                    var res: RespResult<List<MessageLike>>
                    withContext(Dispatchers.IO) {
                        res = api.getMessageLike(
                            messageLikeClientTime.toString(),
                            size,
                            remoteKey?.lastId
                        )
                        // 第一页的情况下，这里插入会覆盖掉之前的远程键，所以不用担心之前查询的远程键对现在的影响
                        dataBase.getMessageDao().insertRemoteKey(MessageRemoteKey(flag, res.data!!.last().messageId))
                    }
                    val data = res.data!!
                    LoadResult.Page(
                        data = data,
                        prevKey = null,
                        nextKey = if (data.size < size) null else nextKey
                    )
                }
                Constants.MESSAGE_INTERACTION_COMMENT_TYPE -> {
                    var res: RespResult<List<MessageComment>>
                    withContext(Dispatchers.IO) {
                        res = api.getMessageComment(messageCommentClientTime.toString(), size, remoteKey?.lastId)
                        dataBase.getMessageDao().insertRemoteKey(MessageRemoteKey(flag, res.data!!.last().messageId))
                    }
                    val data = res.data!!
                    LoadResult.Page(
                        data = data,
                        prevKey = null,
                        nextKey = if (data.size < size) null else nextKey
                    )
                }
                else -> {
                    throw RuntimeException()
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            LoadResult.Error(e)
        }
    }

}