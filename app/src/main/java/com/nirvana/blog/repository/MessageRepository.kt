package com.nirvana.blog.repository

import com.nirvana.blog.api.message.MessageServiceApi
import com.nirvana.blog.base.BaseRepository
import com.nirvana.blog.db.message.MessageDataBase
import com.nirvana.blog.paging.InteractionMessagePagingSource

class MessageRepository(
    private val api: MessageServiceApi,
    private val dataBase: MessageDataBase
) : BaseRepository() {

    fun interactionMessagePagingSource(flag: Int) =
        InteractionMessagePagingSource(api, dataBase, flag)

}