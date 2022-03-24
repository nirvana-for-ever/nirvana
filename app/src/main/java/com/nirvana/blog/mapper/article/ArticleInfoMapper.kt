package com.nirvana.blog.mapper.article

import com.nirvana.blog.entity.ui.article.ArticleInfo
import com.nirvana.blog.entity.db.ArticleInfoEntity
import com.nirvana.blog.mapper.BaseMapper

object ArticleInfoMapper : BaseMapper<ArticleInfoEntity, ArticleInfo>() {
    override fun map(input: ArticleInfoEntity, vararg args: Any): ArticleInfo =
        ArticleInfo(
            input.articleId,
            input.authorId,
            input.author,
            input.authorAvatar,
            input.title,
            input.content,
            input.img1,
            input.img2,
            input.img3,
            input.readCount,
            input.commentCount,
            input.likeCount,
        )

    override fun reverseMap(output: ArticleInfo, vararg args: Any): ArticleInfoEntity =
        ArticleInfoEntity(
            output.articleId,
            output.authorId,
            output.authorName,
            output.authorAvatar,
            output.title,
            output.content,
            output.img1,
            output.img2,
            output.img3,
            output.readCount,
            output.commentCount,
            output.likeCount,
            args[0] as String
        )
}