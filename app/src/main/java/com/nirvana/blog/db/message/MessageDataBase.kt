package com.nirvana.blog.db.message

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nirvana.blog.db.message.dao.MessageDao
import com.nirvana.blog.db.tag.dao.TagDao
import com.nirvana.blog.entity.db.message.MessageRemoteKey
import com.nirvana.blog.entity.db.tag.TagEntity
import kotlin.reflect.KClass

/**
 * entities 中的类将会变成表
 * exportSchema 默认为true，表示会导出数据库架构文件，但是需要配置 room.schemaLocation 参数，不然会报错
 * 把它设为false就行
 */
@Database(entities = [MessageRemoteKey::class], version = 1, exportSchema = false)
abstract class MessageDataBase : RoomDatabase() {

    companion object {
        // 相当于 volatile 关键字
        @Volatile private var instance: MessageDataBase? = null
        fun get(context: Context) : MessageDataBase {
            if (instance == null) {
                val lock: KClass<MessageDataBase> = MessageDataBase::class
                return synchronized(lock) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(context, MessageDataBase::class.java, "blog_message.db").build()
                        instance!!
                    } else {
                        instance!!
                    }
                }
            }
            return instance!!
        }
    }

    // 直接写一个抽象方法，Room会帮我们实现，通过上面 getInstance 的 Room.databaseBuilder
    // 帮我们创建实现类，会实现这个方法，帮我们返回一个 MessageDao
    abstract fun getMessageDao(): MessageDao
}