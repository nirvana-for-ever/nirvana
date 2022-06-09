package com.nirvana.blog.db.tag

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nirvana.blog.db.tag.dao.TagDao
import com.nirvana.blog.entity.db.tag.TagEntity
import kotlin.reflect.KClass

/**
 * entities 中的类将会变成表
 * exportSchema 默认为true，表示会导出数据库架构文件，但是需要配置 room.schemaLocation 参数，不然会报错
 * 把它设为false就行
 */
@Database(entities = [TagEntity::class], version = 1, exportSchema = false)
abstract class TagDataBase : RoomDatabase() {

    companion object {
        // 相当于 volatile 关键字
        @Volatile private var instance: TagDataBase? = null
        fun get(context: Context) : TagDataBase {
            if (instance == null) {
                val lock: KClass<TagDataBase> = TagDataBase::class
                return synchronized(lock) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(context, TagDataBase::class.java, "blog_tag.db").build()
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
    // 帮我们创建实现类，会实现这个方法，帮我们返回一个 UserDao
    abstract fun getTagDao(): TagDao
}