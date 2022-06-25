package com.nirvana.blog.viewmodel.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nirvana.blog.repository.MessageRepository
import com.nirvana.blog.utils.Constants
import com.nirvana.blog.utils.messageCommentClientTime
import com.nirvana.blog.utils.messageLikeClientTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repository: MessageRepository
) : ViewModel() {

    private val messageLikeFlow: Flow<PagingData<Any>>? = null

    private val messageCommentFlow: Flow<PagingData<Any>>? = null

    fun messageFlow(flag: Int): Flow<PagingData<Any>> {
        return when (flag) {
            Constants.MESSAGE_INTERACTION_LIKE_TYPE -> {
                messageLikeClientTime = System.currentTimeMillis()
                messageLikeFlow ?: generateFlow(flag)
            }
            Constants.MESSAGE_INTERACTION_COMMENT_TYPE -> {
                messageCommentClientTime = System.currentTimeMillis()
                messageCommentFlow ?: generateFlow(flag)
            }
            else -> throw RuntimeException()
        }
    }

    private fun generateFlow(flag: Int): Flow<PagingData<Any>> {
        return Pager(
            PagingConfig(
                pageSize = Constants.MESSAGE_PAGE_SIZE,
                // initialLoadSize 默认是 PAGE_SIZE * 3
                // 含义是：一开始加载的数量，也就是一开始不会只加载1页，默认会加载3页的数据
                // initialLoadSize 必须是 PAGE_SIZE 的整数倍，不然不好设置分页请求页数
                initialLoadSize = Constants.MESSAGE_PAGE_SIZE * Constants.MESSAGE_INITIAL_PAGE_MULTIPLIER,
            )
        ) {
            // 添加 PagingSource
            repository.interactionMessagePagingSource(flag)
        }.flow
            .flowOn(Dispatchers.IO)
            // cachedIn 交由 viewModelScope 绑定，这样分页数据就托管在 viewModelScope 生命周期
            // 不会因为旋转屏幕而导致重新加载
            .cachedIn(viewModelScope)
    }

}