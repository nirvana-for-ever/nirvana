package com.nirvana.blog.base

import com.nirvana.blog.BuildConfig
import com.nirvana.blog.entity.network.RespResult

abstract class BaseRepository {

    suspend fun <T> safeReq(req: suspend () -> RespResult<T>): RespResult<T> {
        return try {
            req()
        } catch (t: Throwable) {
            if (BuildConfig.DEBUG) {
                t.printStackTrace()
            }
            RespResult(false, -1, "服务器发烧了，请稍后再试")
        }
    }

}