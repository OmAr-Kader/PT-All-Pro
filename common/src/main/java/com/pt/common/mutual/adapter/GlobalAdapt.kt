package com.pt.common.mutual.adapter

abstract class GlobalAdapt<I>(
    val rootBase: android.view.ViewGroup
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(rootBase), HolderListener,
    android.view.View.OnClickListener {

    abstract val Int.item: I

    inline val posA: Int
        get() = layoutPosition.let {
            if (it != -1) it else absoluteAdapterPosition
        }

    inline val ctxA: android.content.Context get() = rootBase.context

    inline val themA: android.content.res.Resources.Theme get() = rootBase.context.theme

    inline val recA: android.content.res.Resources get() = rootBase.context.resources

    inline val contA: android.content.ContentResolver
        get() = kotlin.runCatching {
            if (com.pt.common.global.isV_N) {
                (androidx.core.content.ContextCompat.isDeviceProtectedStorage(ctxA).run {
                    if (this) androidx.core.content.ContextCompat.createDeviceProtectedStorageContext(ctxA)?.contentResolver else ctxA.contentResolver
                } ?: ctxA.contentResolver)
            } else {
                ctxA.contentResolver
            }
        }.getOrDefault(ctxA.contentResolver)

    inline val Float.toPixelA: Int
        get() = (this * disA.density + 0.5f).toInt()

    inline val disA: android.util.DisplayMetrics
        get() = rootBase.context.resources.displayMetrics


    protected inline fun fetchImageView(
        num: Int,
        a: androidx.appcompat.widget.AppCompatImageView.() -> Unit
    ) {
        (rootBase.getChildAt(num) as androidx.appcompat.widget.AppCompatImageView?)?.let(a)
    }

    protected inline fun fetchTextView(
        num: Int,
        a: androidx.appcompat.widget.AppCompatTextView.() -> Unit
    ) {
        (rootBase.getChildAt(num) as androidx.appcompat.widget.AppCompatTextView?)?.let(a)
    }

}