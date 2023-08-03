package com.pt.common.mutual.life

import com.pt.common.stable.listThrowable

abstract class GlobalInflater {

    protected var denNative: Float? = null

    protected inline val android.content.res.Resources.den: Float
        get() = denNative ?: displayMetrics.density
            .also { denNative = it }

    protected inline val android.content.Context.dpToPx: (Int) -> Int
        get() = {
            (it.toFloat() * resources.den + 0.5F).toInt()
        }

    protected inline fun android.content.Context.frameLayoutParent(block: android.widget.FrameLayout.() -> Unit): android.widget.FrameLayout {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return android.widget.FrameLayout(this).also(block)
    }

    protected inline fun android.content.Context.cardViewParent(block: androidx.cardview.widget.CardView.() -> Unit): androidx.cardview.widget.CardView {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.cardview.widget.CardView(this).also(block)
    }

    protected inline fun android.content.Context.constraintParent(block: androidx.constraintlayout.widget.ConstraintLayout.() -> Unit): androidx.constraintlayout.widget.ConstraintLayout {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.constraintlayout.widget.ConstraintLayout(this@constraintParent).also(block)
    }

    protected inline fun android.content.Context.linearLayoutParent(block: androidx.appcompat.widget.LinearLayoutCompat.() -> Unit): androidx.appcompat.widget.LinearLayoutCompat {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.appcompat.widget.LinearLayoutCompat(this).also(block)
    }

    protected inline fun android.view.ViewGroup.card(block: androidx.cardview.widget.CardView.() -> Unit): androidx.cardview.widget.CardView {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.cardview.widget.CardView(context).also(block).also(this@card::addView)
    }

    protected inline fun android.view.ViewGroup.linearTint(block: com.pt.common.moderator.over.LinearTint.() -> Unit): com.pt.common.moderator.over.LinearTint {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return com.pt.common.moderator.over.LinearTint(context).also(block).also(this@linearTint::addView)
    }

    protected inline fun android.view.ViewGroup.linear(block: androidx.appcompat.widget.LinearLayoutCompat.() -> Unit): androidx.appcompat.widget.LinearLayoutCompat {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.appcompat.widget.LinearLayoutCompat(context).also(block).also(this@linear::addView)
    }

    protected inline fun android.view.ViewGroup.image(block: androidx.appcompat.widget.AppCompatImageView.() -> Unit): androidx.appcompat.widget.AppCompatImageView {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.appcompat.widget.AppCompatImageView(context).also(block).also(this@image::addView)
    }

    protected inline fun android.view.ViewGroup.glideImage(block: com.pt.common.moderator.over.GlideImageView.() -> Unit): com.pt.common.moderator.over.GlideImageView {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return com.pt.common.moderator.over.GlideImageView(context).also(block).also(this@glideImage::addView)
    }

    protected inline fun android.view.ViewGroup.textViewer(block: androidx.appcompat.widget.AppCompatTextView.() -> Unit): androidx.appcompat.widget.AppCompatTextView {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.appcompat.widget.AppCompatTextView(context).also(block).also(this@textViewer::addView)
    }

    protected inline fun android.view.ViewGroup.coordinator(block: androidx.coordinatorlayout.widget.CoordinatorLayout.() -> Unit): androidx.coordinatorlayout.widget.CoordinatorLayout {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.coordinatorlayout.widget.CoordinatorLayout(context).also(block).also(this@coordinator::addView)
    }

    protected inline fun android.view.ViewGroup.editTextEvent(block: com.pt.common.moderator.over.EditTextBackEvent.() -> Unit): com.pt.common.moderator.over.EditTextBackEvent {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return com.pt.common.moderator.over.EditTextBackEvent(context).also(block).also(this@editTextEvent::addView)
    }

    protected inline fun android.view.ViewGroup.touchImage(block: com.pt.common.moderator.touch.TouchImageView.() -> Unit): com.pt.common.moderator.touch.TouchImageView {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return com.pt.common.moderator.touch.TouchImageView(context).also(block).also(this@touchImage::addView)
    }

    protected inline fun android.view.ViewGroup.touchFrame(block: com.pt.common.moderator.touch.TouchFrameLayout.() -> Unit): com.pt.common.moderator.touch.TouchFrameLayout {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return com.pt.common.moderator.touch.TouchFrameLayout(context).also(block).also(this@touchFrame::addView)
    }

    protected inline fun android.view.ViewGroup.nestedScroll(block: androidx.core.widget.NestedScrollView.() -> Unit): androidx.core.widget.NestedScrollView {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.core.widget.NestedScrollView(context).also(block).also(this@nestedScroll::addView)
    }

    protected inline fun android.view.ViewGroup.horizontalScroll(block: android.widget.HorizontalScrollView.() -> Unit): android.widget.HorizontalScrollView {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return android.widget.HorizontalScrollView(context).also(block).also(this@horizontalScroll::addView)
    }

    protected inline fun android.view.ViewGroup.pager(block: androidx.viewpager2.widget.ViewPager2.() -> Unit): androidx.viewpager2.widget.ViewPager2 {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.viewpager2.widget.ViewPager2(context).also(block).also(this@pager::addView)
    }

    protected inline fun android.view.ViewGroup.recycler(block: com.pt.common.moderator.recycler.RecyclerForViews.() -> Unit): com.pt.common.moderator.recycler.RecyclerForViews {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return com.pt.common.moderator.recycler.RecyclerForViews(context).also(block).also(this@recycler::addView)
    }

    protected inline fun android.view.ViewGroup.editText(block: androidx.appcompat.widget.AppCompatEditText.() -> Unit): androidx.appcompat.widget.AppCompatEditText {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.appcompat.widget.AppCompatEditText(context).also(block).also(this@editText::addView)
    }

    protected inline fun android.view.ViewGroup.button(block: androidx.appcompat.widget.AppCompatButton.() -> Unit): androidx.appcompat.widget.AppCompatButton {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.appcompat.widget.AppCompatButton(context).also(block).also(this@button::addView)
    }

    protected inline fun android.view.ViewGroup.frame(block: android.widget.FrameLayout.() -> Unit): android.widget.FrameLayout {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return android.widget.FrameLayout(context).also(block).also(this@frame::addView)
    }

    protected inline fun android.view.ViewGroup.constraint(block: androidx.constraintlayout.widget.ConstraintLayout.() -> Unit): androidx.constraintlayout.widget.ConstraintLayout {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.constraintlayout.widget.ConstraintLayout(context).also(block).also(this@constraint::addView)
    }

    protected inline fun android.view.ViewGroup.resizeImage(block: com.pt.common.moderator.over.ResizeImageView.() -> Unit): com.pt.common.moderator.over.ResizeImageView {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return com.pt.common.moderator.over.ResizeImageView(context).also(block).also(this@resizeImage::addView)
    }

    protected inline fun android.view.ViewGroup.scalelessText(block: com.pt.common.moderator.over.ScalelessTextView.() -> Unit): com.pt.common.moderator.over.ScalelessTextView {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return com.pt.common.moderator.over.ScalelessTextView(context).also(block).also(this@scalelessText::addView)
    }

    protected inline fun android.view.ViewGroup.progressBar(block: androidx.core.widget.ContentLoadingProgressBar.() -> Unit): androidx.core.widget.ContentLoadingProgressBar {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.core.widget.ContentLoadingProgressBar(
            androidx.appcompat.view.ContextThemeWrapper(
                context, com.pt.pro.R.style.ProgressBar
            )
        ).also(block).also(this@progressBar::addView)
    }

    protected inline fun android.view.ViewGroup.seekBar(block: androidx.appcompat.widget.AppCompatSeekBar.() -> Unit): androidx.appcompat.widget.AppCompatSeekBar {
        kotlin.contracts.contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return androidx.appcompat.widget.AppCompatSeekBar(context).also(block).also(this@seekBar::addView)
    }

    protected inline val android.content.Context.rmoBackground: Int
        get() = android.util.TypedValue().apply {
            theme.resolveAttribute(com.pt.pro.R.attr.rmoBackground, this, true)
        }.data

    protected inline val android.content.Context.rmoBackHint: Int
        get() = android.util.TypedValue().apply {
            theme.resolveAttribute(com.pt.pro.R.attr.rmoBackHint, this, true)
        }.data

    protected inline val android.content.Context.rmoText: Int
        get() = android.util.TypedValue().apply {
            theme.resolveAttribute(com.pt.pro.R.attr.rmoText, this, true)
        }.data

    protected inline val android.content.Context.rmoGrey: Int
        get() = android.util.TypedValue().apply {
            theme.resolveAttribute(com.pt.pro.R.attr.rmoGrey, this, true)
        }.data

    protected inline val android.content.Context.colorPrimary: Int
        get() = android.util.TypedValue().apply {
            theme.resolveAttribute(android.R.attr.colorPrimary, this, true)
        }.data

    protected inline val android.content.Context.colorAccent: Int
        get() = android.util.TypedValue().apply {
            theme.resolveAttribute(android.R.attr.colorAccent, this, true)
        }.data

    protected inline val android.content.Context.textColorPrimary: Int
        get() = android.util.TypedValue().apply {
            theme.resolveAttribute(android.R.attr.textColorPrimary, this, true)
        }.data

    protected inline val android.content.res.Resources.getDimensions: (Int, def: Int) -> Int
        get() = { rec, def ->
            com.pt.common.stable.catchy(def) {
                try {
                    getDimension(rec).toInt()
                } catch (e: android.content.res.Resources.NotFoundException) {
                    e.listThrowable()
                    def
                }
            }
        }

    protected inline val android.content.res.Resources.getDimensionsF: (Int, def: Float) -> Float
        get() = { rec, def ->
            com.pt.common.stable.catchy(def) {
                try {
                    getDimension(rec)
                } catch (e: android.content.res.Resources.NotFoundException) {
                    e.listThrowable()
                    def
                }
            }
        }

    protected fun android.widget.TextView.textSize(siz: Int) {
        setTextSize(
            android.util.TypedValue.COMPLEX_UNIT_PX,
            siz.toFloat()
        )
    }

    protected fun android.widget.TextView.textSize(siz: Float) {
        setTextSize(
            android.util.TypedValue.COMPLEX_UNIT_PX,
            siz
        )
    }

    protected fun androidx.appcompat.widget.LinearLayoutCompat.verticalLin() {
        orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
    }

    protected fun androidx.appcompat.widget.LinearLayoutCompat.horizontalLin() {
        orientation = androidx.appcompat.widget.LinearLayoutCompat.HORIZONTAL
    }

    protected fun android.view.View.clickableView() {
        isClickable = true
        isFocusable = true
    }

    protected fun android.view.View.nonClickableView() {
        isClickable = false
        isFocusable = false
    }

    protected fun destroyGlobalFasten() {
        denNative = null
    }

}