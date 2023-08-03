package com.pt.common.mutual.adapter

abstract class GlobalAdapterEX<VB : androidx.viewbinding.ViewBinding, I>(
    protected val binder: VB
) : GlobalAdapt<I>(binder.root as android.view.ViewGroup) {

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
    override fun attachFilter() {
        posA.let {
            binder.attachPlus(it.item, it)
        }
    }

    @com.pt.common.global.UiAnn
    protected abstract fun VB.attachPlus(it: I, i: Int)


    @com.pt.common.global.UiAnn
    override fun clear() {
        binder.clear()
    }

    @com.pt.common.global.UiAnn
    protected abstract fun VB.clear()


    @com.pt.common.global.UiAnn
    override fun finalClear() {
        binder.finalClear()
    }

    @com.pt.common.global.UiAnn
    protected abstract fun VB.finalClear()

    override fun onClick(v: android.view.View?) {
        posA.let {
            it.item.onClick(it)
        }
    }

    abstract fun I.onClick(i: Int)

}
