package com.pt.pro.extra.utils

class TitleAdapter(
    private val listString: MutableList<String>,
) : androidx.recyclerview.widget.RecyclerView.Adapter<TitleAdapter.TittleHolder>() {

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): TittleHolder = TittleHolder(
        com.pt.pro.extra.fasten.ExtraInflater.run { parent.context.inflaterTitle() }
    )

    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holder: TittleHolder, position: Int) {
        holder.bind()
    }

    override fun onViewAttachedToWindow(holder: TittleHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    override fun getItemCount(): Int = listString.size

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    @com.pt.common.global.UiAnn
    inner class TittleHolder(
        item: com.pt.pro.extra.fasten.TitleScreenFasten
    ) : com.pt.common.mutual.adapter.GlobalAdapter<com.pt.pro.extra.fasten.TitleScreenFasten, String>(
        item
    ) {

        override val Int.item: String
            get() = listString[this]

        override fun com.pt.pro.extra.fasten.TitleScreenFasten.bind() {}

        override fun com.pt.pro.extra.fasten.TitleScreenFasten.clear() {}

        override fun com.pt.pro.extra.fasten.TitleScreenFasten.attach(it: String) {
            title.text = it
        }

        override fun String.onClick(i: Int) {}
    }

}