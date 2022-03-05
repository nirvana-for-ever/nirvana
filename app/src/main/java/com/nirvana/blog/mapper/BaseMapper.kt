package com.nirvana.blog.mapper

abstract class BaseMapper<I, O> {
    abstract fun map(input: I, vararg args: Any): O
    open fun reverseMap(output: O, vararg args: Any): I = throw IllegalStateException("逆向映射关系未实现")
}