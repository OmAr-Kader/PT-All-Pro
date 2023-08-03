package com.pt.common.mutual.adapter

abstract class GlobalAdapter<VB : androidx.viewbinding.ViewBinding, I>(
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
        binder.attach(posA.item)
    }

    @com.pt.common.global.UiAnn
    protected abstract fun VB.attach(it: I)


    @com.pt.common.global.UiAnn
    override fun clear() {
        binder.clear()
    }

    @com.pt.common.global.UiAnn
    protected abstract fun VB.clear()

    override fun onClick(v: android.view.View?) {
        posA.let {
            it.item.onClick(it)
        }
    }

    abstract fun I.onClick(i: Int)

}
