package com.pt.pro.main.former

import com.pt.common.global.*
import com.pt.common.stable.*

class MainAdapter(
    override var icons: MutableList<Int>,
    override val widthIcon: Int,
    override val heightIcon: Int,
    override val isNight: Boolean,
    override var iconListener: ((app: Int) -> Unit)?,
) : androidx.recyclerview.widget.RecyclerView.Adapter<MainAdapter.IconsHolder>(), MainAdapterListener {

    override var lastApp: Int = MUSIC
    override var refreshMode: Int = 0 // 0 NORMAL - -1 onResume - onClick 1

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): MainAdapter.IconsHolder = android.view.LayoutInflater.from(parent.context).run {
        IconsHolder(com.pt.pro.main.odd.MainFasten.run {
            parent.context.inflaterMainIcon(
                kotlin.math.min(widthIcon, heightIcon)
            )
        })
    }

    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holder: MainAdapter.IconsHolder, position: Int) {
        holder.bind()
    }

    override fun onViewRecycled(holder: IconsHolder) {
        super.onViewRecycled(holder)
        holder.finalClear()
    }

    override fun getItemCount(): Int = icons.size
    override fun getItemViewType(position: Int): Int = icons[position]

    override suspend fun MutableList<Int>.updateIcons() {
        adder()
        justAdd(size)
    }

    @com.pt.common.global.MainAnn
    private suspend fun MutableList<Int>.adder() {
        withMain {
            icons = this@adder
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun justAdd(siz: Int) {
        withMain {
            notifyItemRangeInserted(0, siz)
        }
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    override fun refreshAdapter(newApp: Int, mode: Int) {
        lastApp = newApp
        refreshMode = mode
        notifyDataSetChanged()
    }

    override fun onAdapterDestroy() {
        iconListener = null
        icons = mutableListOf()
    }

    internal inline val Int.fetchTitleRec: Int
        get() {
            return when (this@fetchTitleRec) {
                ALARM -> com.pt.pro.R.string.am
                FILE_MAN -> com.pt.pro.R.string.fw
                DATA_KEEP -> com.pt.pro.R.string.zd
                GALLERY -> com.pt.pro.R.string.lg
                OVERLAY -> com.pt.pro.R.string.qu
                else -> com.pt.pro.R.string.mk
            }
        }

    internal inline val Int.fetchRec: DSackT<Int, Int>
        get() {
            return when (this@fetchRec) {
                ALARM -> DSackT(com.pt.pro.R.drawable.anim_alarm, com.pt.pro.R.drawable.ic_alarm)
                FILE_MAN -> DSackT(com.pt.pro.R.drawable.anim_file, com.pt.pro.R.drawable.ic_file)
                DATA_KEEP -> DSackT(com.pt.pro.R.drawable.anim_note, com.pt.pro.R.drawable.ic_data)
                GALLERY -> DSackT(com.pt.pro.R.drawable.anim_gallery, com.pt.pro.R.drawable.ic_gallery)
                OVERLAY -> DSackT(com.pt.pro.R.drawable.anim_overlay, com.pt.pro.R.drawable.ic_overlay)
                else -> DSackT(com.pt.pro.R.drawable.anim_music, com.pt.pro.R.drawable.ic_music)
            }
        }

    internal inline val android.content.Context.dStr: (Int) -> String
        get() = {
            try {
                resources.getString(it)
            } catch (e: android.content.res.Resources.NotFoundException) {
                ""
            }
        }

    @com.pt.common.global.UiAnn
    inner class IconsHolder(
        item: com.pt.pro.main.odd.MainIconFasten,
    ) : com.pt.common.mutual.adapter.GlobalAdapter<com.pt.pro.main.odd.MainIconFasten, Int>(item) {

        private var musicAnim: androidx.vectordrawable.graphics.drawable.Animatable2Compat? = null

        override val Int.item: Int
            get() = icons[this]

        override fun com.pt.pro.main.odd.MainIconFasten.bind() {
            cardPic.apply {
                themA.findAttr(android.R.attr.colorPrimary).also { c ->
                    if (posA.item == lastApp) {
                        this@apply.setCardBackgroundColor(c)
                    } else {
                        if (isNight) {
                            this@apply.setCardBackgroundColor(c.toColorAlpha(125))
                        } else {
                            this@apply.setCardBackgroundColor(c.darken.toColorAlpha(125))
                        }
                    }
                }
            }
            iconPic.setOnClickListener(this@IconsHolder)
            iconTxt.setOnClickListener(this@IconsHolder)
            iconPic.apply {
                posA.item.let { ite ->
                    iconTxt.text = ctxA.dStr(ite.fetchTitleRec)
                    ite.fetchRec.let {
                        if (ite == lastApp) {
                            when (refreshMode) {
                                -1 -> {
                                    setImage(it.two)
                                }
                                1 -> {
                                    setImage(it.two)
                                    binder.root_.loadAnim()
                                }
                                else -> {
                                    setAnimImage(DSackT(it.one, it.two)) {
                                        musicAnim = this
                                    }
                                }
                            }
                        } else {
                            setImage(it.two)
                        }
                    }
                }
            }
        }

        private fun android.widget.FrameLayout.loadAnim() {
            kotlin.runCatching {
                kotlin.math.min(widthIcon, heightIcon).let {
                    startAnimation(scaleDownAnimationForMain(if (it < 600) 1.05F else 1.01F))
                }
            }
        }

        override fun Int.onClick(i: Int) {
            iconListener?.invoke(this)
        }

        override fun finalClear() {
            binder.iconPic.apply {
                setImageDrawable(null)
                onViewDestroy()
            }
            catchyUnit {
                musicAnim?.clearAnimationCallbacks()
                if (musicAnim?.isRunning == true) {
                    musicAnim?.stop()
                }
                musicAnim = null
            }
        }

        override fun com.pt.pro.main.odd.MainIconFasten.attach(it: Int) {}
        override fun com.pt.pro.main.odd.MainIconFasten.clear() {}
    }

    override fun androidx.appcompat.widget.AppCompatImageView.setAnimImage(
        re: DSackT<Int, Int>,
        anim: androidx.vectordrawable.graphics.drawable.Animatable2Compat.() -> Unit,
    ) {
        runCatching<Unit> {
            androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat.create(
                context, re.one
            )?.also { dra ->
                setImageDrawable(dra)
                (drawable as? androidx.vectordrawable.graphics.drawable.Animatable2Compat?)?.also {
                    it.start()
                }?.also(anim) ?: setImage(re.two)
            } ?: setImage(re.two)
        }.onFailure {
            setImage(re.two)
        }
    }

    override fun androidx.appcompat.widget.AppCompatImageView.setImage(
        @androidx.annotation.DrawableRes stan: Int,
    ) {
        context.compactImage(stan) {
            setImageDrawable(this)
        }
    }

}

