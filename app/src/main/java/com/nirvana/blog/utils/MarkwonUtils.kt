package com.nirvana.blog.utils

import android.content.Context
import android.graphics.EmbossMaskFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.Target
import com.nirvana.blog.R
import com.nirvana.blog.ext.AricleMKEntry
import io.noties.markwon.*
import io.noties.markwon.core.CoreProps
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.recycler.MarkwonAdapter
import io.noties.markwon.recycler.SimpleEntry
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j
import io.noties.prism4j.annotations.PrismBundle
import org.commonmark.node.*

@PrismBundle(includeAll = true)
object MarkwonUtils {

    fun basicMarkwon(context: Context): Markwon {
        return Markwon.builder(context)
            // 图片加载方式，使用 Glide 加载，还需导入 GlideImagesPlugin 的依赖
            .usePlugin(GlideImagesPlugin.create(object : GlideImagesPlugin.GlideStore {
                override fun load(drawable: AsyncDrawable): RequestBuilder<Drawable> {
                    return Glide.with(context)
                        .load(drawable.destination)
                        .placeholder(R.drawable.ic_loading_img)
                        .error(R.drawable.ic_loading_img_fail)
                }

                override fun cancel(target: Target<*>) {
                    Glide.with(context).clear(target)
                }
            }))
            // 代码的高亮
            .usePlugin(
                SyntaxHighlightPlugin.create(
                    Prism4j(GrammarLocatorDef()),
                    Prism4jThemeDarkula.create()
                )
            )
            .usePlugin(object : AbstractMarkwonPlugin() {
                // 给不同等级的标题设置不同的 Span
                override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                    builder.setFactory(Heading::class.java) { _, props ->
                        val arr = arrayOfNulls<Any>(3)
                        arr[0] = ForegroundColorSpan(ContextCompat.getColor(context, R.color.black_text_color))
                        arr[1] = StyleSpan(Typeface.BOLD)
                        when (CoreProps.HEADING_LEVEL.require(props)) {
                            1 -> arr[2] = RelativeSizeSpan(1.5f)
                            2 -> arr[2] = RelativeSizeSpan(1.4f)
                            3 -> arr[2] = RelativeSizeSpan(1.3f)
                            4 -> arr[2] = RelativeSizeSpan(1.2f)
                            5 -> arr[2] = RelativeSizeSpan(1.1f)
                            else -> arr[2] = RelativeSizeSpan(1f)
                        }
                        arr
                    }
                    // `` 代码的背景跟代码块不一样比较好看
                    builder.setFactory(Code::class.java) { _, _ ->
                        arrayOf(
                            BackgroundColorSpan(ContextCompat.getColor(context, R.color.light_gray)),
                            ForegroundColorSpan(ContextCompat.getColor(context, R.color.black_text_color)),
                            TypefaceSpan("monospace")
                        )
                    }
                }
            })
            .build()
    }

    fun basicAdapter(): MarkwonAdapter {
        return MarkwonAdapter.builderTextViewIsRoot(R.layout.markwon_text_view)
            .include(Heading::class.java, AricleMKEntry(R.layout.markwon_heading_text_view, R.id.markwon_heading_tv))
            .include(
                FencedCodeBlock::class.java,
                SimpleEntry.create(R.layout.markwon_code_block, R.id.text_view)
            )
            .build()
    }

}