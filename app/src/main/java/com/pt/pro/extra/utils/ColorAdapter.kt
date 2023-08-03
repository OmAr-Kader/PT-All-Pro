package com.pt.pro.extra.utils

import com.pt.common.global.findAttr
import com.pt.common.global.svgReClear
import com.pt.common.global.svgReColor
import com.pt.common.stable.getI
import com.pt.pro.R.attr.rmoText

class ColorAdapter(
    override val styles: MutableList<com.pt.common.global.DSackT<Int, Int>>,
    override var colorPosition: Int,
    override var extraItemListener: ((Int, Int, Int) -> Unit)?,
) : androidx.recyclerview.widget.RecyclerView.Adapter<ColorAdapter.IconsHolder>(),
    com.pt.pro.extra.interfaces.ColorListener {

    override val colors: MutableList<Int>
        get() = mutableListOf()

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): IconsHolder = IconsHolder(
        com.pt.pro.extra.fasten.ExtraInflater.run { parent.context.inflaterColorItem() }
    )

    override fun onBindViewHolder(holder: IconsHolder, position: Int) {
        holder.bind()
    }

    override fun onViewAttachedToWindow(holder: IconsHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    override fun getItemCount(): Int = styles.size

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onAdapterDestroy() {
        extraItemListener = null
        styles.clear()
    }

    @com.pt.common.global.UiAnn
    inner class IconsHolder(
        item: com.pt.pro.extra.fasten.ColorItemFasten
    ) : com.pt.common.mutual.adapter.GlobalAdapter<com.pt.pro.extra.fasten.ColorItemFasten, com.pt.common.global.DSackT<Int, Int>>(
        item
    ) {

        override val Int.item: com.pt.common.global.DSackT<Int, Int>
            get() = styles.getI(posA)

        override fun com.pt.pro.extra.fasten.ColorItemFasten.bind() {
            root_.apply {
                posA.let {
                    if (it == colorPosition) {
                        setCardBackgroundColor(themA.findAttr(rmoText))
                    } else {
                        setCardBackgroundColor(0)
                    }
                }
            }
            colorButton.setOnClickListener(this@IconsHolder)
        }

        override fun com.pt.pro.extra.fasten.ColorItemFasten.attach(it: com.pt.common.global.DSackT<Int, Int>) {
            colorButton.svgReColor(it.one)
        }

        override fun com.pt.pro.extra.fasten.ColorItemFasten.clear() {
            colorButton.svgReClear()
            root_.setCardBackgroundColor(0)
        }

        override fun com.pt.common.global.DSackT<Int, Int>.onClick(i: Int) {
            val co = colorPosition
            extraItemListener?.invoke(this@onClick.two, this@onClick.one, i)
            binder.root_.apply {
                setCardBackgroundColor(themA.findAttr(rmoText))
                colorPosition = i
            }
            notifyItemChanged(co)
        }
    }

}