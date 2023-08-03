package com.pt.common.mutual.adapter

abstract class GlobalAdapterSus<VB : androidx.viewbinding.ViewBinding, I>(
    val binder: VB
) : GlobalAdapt<I>(binder.root as android.view.ViewGroup), android.view.View.OnLongClickListener {

    override fun bind() {
        binder.bind()
    }

    protected abstract fun VB.bind()

    override fun attach() {
        posA.let {
            binder.attach(it.item, it)
        }
    }


    protected abstract fun VB.attach(it: I, i: Int)


    override fun attachFilter() {
        binder.attachPlus()
    }

    protected abstract fun VB.attachPlus()

    override suspend fun clearSus() {
        binder.clear(posA)
    }

    protected abstract suspend fun VB.clear(i: Int)

    override fun clear() {}

    override fun finalClear() {
        binder.finalClear()
    }

    protected abstract fun VB.finalClear()

    override fun onLongClick(v: android.view.View?): Boolean {
        v?.onLongClick(posA.item)
        return true
    }

    abstract fun android.view.View.onLongClick(it: I)

    override fun onClick(v: android.view.View?) {
        posA.let {
            it.item.onClick(it)
        }
    }

    abstract fun I.onClick(i: Int)

}
