package com.nirvana.blog.ext

import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.nirvana.blog.utils.DensityUtils
import io.noties.markwon.Markwon
import io.noties.markwon.recycler.MarkwonAdapter
import org.commonmark.node.Heading
import org.commonmark.node.Node

/**
 * 仿照 SimpleEntry 写的，改动了 bindHolder 方法，根据不同的 Heading 等级设置不同的 margin
 */
class AricleMKEntry(
    @LayoutRes val layoutResId: Int,
    @IdRes val textViewIdRes: Int,
) : MarkwonAdapter.Entry<Node, AricleMKEntry.Holder>() {

    // small cache for already rendered nodes
    private val cache: MutableMap<Node, Spanned> = HashMap()

    override fun createHolder(inflater: LayoutInflater, parent: ViewGroup): Holder {
        return Holder(textViewIdRes, inflater.inflate(layoutResId, parent, false))
    }

    override fun bindHolder(markwon: Markwon, holder: Holder, node: Node) {
        if (node is Heading) {
            val params: LinearLayout.LayoutParams = holder.tv.layoutParams as LinearLayout.LayoutParams
            val margin = when (node.level) {
                1 -> DensityUtils.dip2px(holder.tv.context, 10f)
                2 -> DensityUtils.dip2px(holder.tv.context, 8f)
                3 -> DensityUtils.dip2px(holder.tv.context, 6f)
                4 -> DensityUtils.dip2px(holder.tv.context, 4f)
                5 -> DensityUtils.dip2px(holder.tv.context, 2f)
                6 -> DensityUtils.dip2px(holder.tv.context, 1f)
                else -> DensityUtils.dip2px(holder.tv.context, 1f)
            }
            params.topMargin = margin
            params.bottomMargin = margin
        }
        var spanned = cache[node]
        if (spanned == null) {
            spanned = markwon.render(node)
            cache[node] = spanned
        }
        markwon.setParsedMarkdown(holder.tv, spanned)
    }

    override fun clear() {
        cache.clear()
    }

    class Holder(tvId: Int, itemView: View) : MarkwonAdapter.Holder(itemView) {
        val tv: TextView = itemView.findViewById(tvId)
    }

}
