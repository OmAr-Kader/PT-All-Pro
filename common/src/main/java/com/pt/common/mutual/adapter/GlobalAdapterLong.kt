package com.pt.common.mutual.adapter

abstract class GlobalAdapterLong<VB : androidx.viewbinding.ViewBinding, I>(
    protected val binder: VB
) : GlobalAdapt<I>(binder.root as android.view.ViewGroup), android.view.View.OnLongClickListener {

    @com.pt.common.global.UiAnn
    override fun bind() {
        binder.bind()
    }

    @com.pt.common.global.UiAnn
    protected abstract fun VB.bind()


    @com.pt.common.global.UiAnn
    override fun attach() {
        posA.let {
            binder.attach(it.item, it)
        }
    }

    @com.pt.common.global.UiAnn
    protected abstract fun VB.attach(it: I, i: Int)


    @com.pt.common.global.UiAnn
    override fun clear() {
        binder.clear()
    }

    @com.pt.common.global.UiAnn
    protected abstract fun VB.clear()


    override fun onClick(v: android.view.View?) {
        v?.onClick(posA.item)
    }

    abstract fun android.view.View.onClick(it: I)

    override fun onLongClick(v: android.view.View?): Boolean {
        return v?.onLongClick(posA.item) ?: false
    }

    abstract fun android.view.View.onLongClick(it: I): Boolean


}
