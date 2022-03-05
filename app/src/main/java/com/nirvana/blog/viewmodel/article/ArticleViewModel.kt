package com.nirvana.blog.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nirvana.blog.entity.ArticleInfo
import com.nirvana.blog.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    private val liveDataMap: MutableMap<String, LiveData<PagingData<ArticleInfo>>> = mutableMapOf()

    fun articleInfoLiveData(tagId: String): LiveData<PagingData<ArticleInfo>> {
        return liveDataMap.getOrPut(tagId) {
            repository
                .articleInfoFlow(tagId)
                // cachedIn 交由 viewModelScope 绑定，这样分页数据就托管在 viewModelScope 生命周期
                // 不会因为旋转屏幕而导致重新加载
                .cachedIn(viewModelScope)
                .asLiveData()
        }
    }


}

