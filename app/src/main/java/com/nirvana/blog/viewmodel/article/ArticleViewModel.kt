package com.nirvana.blog.viewmodel.article

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nirvana.blog.entity.ui.article.Article
import com.nirvana.blog.entity.ui.article.ArticleInfo
import com.nirvana.blog.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    private val liveDataMap: MutableMap<String, LiveData<PagingData<ArticleInfo>>> = mutableMapOf()

    val article = MutableLiveData<Article>()

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

    fun getArticle(articleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resp = repository.getArticle(articleId)
            if (resp.success) {
                article.postValue(resp.data!!)
            }
        }
    }

}

