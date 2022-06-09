package com.nirvana.blog.base

import com.nirvana.blog.BuildConfig
import com.nirvana.blog.entity.network.DBResult
import com.nirvana.blog.entity.network.RespResult
import com.nirvana.blog.utils.Constants

abstract class BaseRepository {

    suspend fun <T> safeReq(req: suspend () -> RespResult<T>): RespResult<T> {
        return try {
            req()
        } catch (t: Throwable) {
            if (BuildConfig.DEBUG) {
                t.printStackTrace()
            }
            RespResult(false, -1, Constants.SERVER_ERROR_MSG)
        }
    }

    suspend fun <T> safeDB(db: suspend () -> T): DBResult<T> {
        return try {
            DBResult(true, db())
        } catch (t: Throwable) {
            if (BuildConfig.DEBUG) {
                t.printStackTrace()
            }
            DBResult(false, null)
        }
    }

}