package com.pt.pro.extra.utils

import com.pt.common.global.findAttr
import com.pt.common.global.svgReClear
import com.pt.common.global.svgReColor
import com.pt.common.stable.getI
import com.pt.pro.R.attr.rmoText

class ColorDataAdapter(
    override val colors: MutableList<Int>,
    override var colorPosition: Int,
    override var extraItemListener: ((Int, Int, Int) -> Unit)?,
) : androidx.recyclerview.widget.RecyclerView.Adapter<ColorDataAdapter.IconsHolder>(),
    com.pt.pro.extra.interfaces.ColorListener {

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): IconsHolder = IconsHolder(
        com.pt.pro.extra.fasten.ExtraInflater.run { parent.context.inflaterColorItem() }
    )

    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holder: IconsHolder, position: Int) {
        holder.bind()
    }

    override fun onViewAttachedToWindow(holder: IconsHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    override fun getItemCount(): Int = colors.size

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    override val styles: MutableList<com.pt.common.global.DSackT<Int, Int>>
        get() = mutableListOf()

    override fun onAdapterDestroy() {
        extraItemListener = null
        styles.clear()
        colors.clear()
    }

    @com.pt.common.global.UiAnn
    inner class IconsHolder(
        item: com.pt.pro.extra.fasten.ColorItemFasten
    ) : com.pt.common.mutual.adapter.GlobalAdapter<com.pt.pro.extra.fasten.ColorItemFasten, Int>(
        item
    ) {

        override val Int.item: Int
            get() = colors.getI(this)

        override fun com.pt.pro.extra.fasten.ColorItemFasten.bind() {
            root_.apply {
                if (posA == colorPosition)
                    setCardBackgroundColor(themA.findAttr(rmoText))
                else
                    setCardBackgroundColor(0)
            }
            colorButton.svgReColor(colors.getI(posA))
            colorButton.setOnClickListener(this@IconsHolder)
        }

        override fun com.pt.pro.extra.fasten.ColorItemFasten.attach(it: Int) {
            colorButton.svgReColor(it)
        }

        override fun com.pt.pro.extra.fasten.ColorItemFasten.clear() {
            colorButton.svgReClear()
            root_.setCardBackgroundColor(0)
        }

        override fun Int.onClick(i: Int) {
            val co = colorPosition
            extraItemListener?.invoke(0, this@onClick, i)
            binder.root_.apply {
                setCardBackgroundColor(themA.findAttr(rmoText))
                colorPosition = i
            }
            notifyItemChanged(co)
        }
    }

}