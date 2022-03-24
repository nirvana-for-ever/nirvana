package com.nirvana.blog.repository

import com.nirvana.blog.api.user.AccountServiceApi
import com.nirvana.blog.base.BaseRepository
import com.nirvana.blog.entity.network.user.LoginUserVo

class AccountRepository(
    private val api: AccountServiceApi,
) : BaseRepository() {

    suspend fun login(vo: LoginUserVo) = safeReq { api.login(vo) }

    suspend fun info(type: Int) = safeReq { api.info(type) }

    suspend fun logout() = safeReq { api.logout() }
}