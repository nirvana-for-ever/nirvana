package com.nirvana.blog.viewmodel.tag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nirvana.blog.entity.ui.tag.Tag
import com.nirvana.blog.repository.TagRepository
import com.nirvana.blog.utils.isLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val repository: TagRepository
) : ViewModel() {

    val userTags = MutableLiveData<MutableList<Tag>>()

    val allTags = MutableLiveData<MutableList<Tag>>()

    fun getUserTags() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isLogin) {
                val dbResp = repository.getDBTags()
                if (dbResp.success) {
                    val dbTags = dbResp.data as List<Tag>
                    if (dbTags.isNotEmpty()) {
                        userTags.postValue(dbTags.toMutableList())
                        return@launch
                    }
                }
            }
            val resp = repository.getTags()
            if (resp.success) {
                userTags.postValue(resp.data!!)
                if (!isLogin) {
                    // 未登录就将获取到的标签存入数据库中，作为未登录的订阅标签库
                    repository.saveTags2DB(resp.data)
                }
            }
        }
    }

    fun getAllTags() {
        allTags.value?.clear()
        viewModelScope.launch(Dispatchers.IO) {
            val resp = repository.getAllTags()
            if (resp.success) {
                allTags.postValue(resp.data!!)
            }
        }
    }

    /**
     * 保存标签的变化
     */
    fun saveTagChanging(tags: MutableList<Tag>, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isLogin) {
                // 有登录，就提交请求修改，转换成 tagId 的字符串再发送请求
                repository.saveUserTags(tags.joinToString(separator = ",") { it.tagId })
            } else {
                // 没登录，就修改数据库
                repository.saveTags2DB(tags)
            }
            withContext(Dispatchers.Main) {
                callback()
            }
        }
    }

}