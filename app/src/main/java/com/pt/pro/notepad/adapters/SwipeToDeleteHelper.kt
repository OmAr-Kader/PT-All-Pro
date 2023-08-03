package com.pt.pro.notepad.adapters

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pt.pro.notepad.interfaces.DataClickListener

class SwipeToDeleteHelper() : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.START or ItemTouchHelper.END
) {

    private var mAdapter: DataKeeperAdapter? = null
    private var doneIcon: Drawable? = null
    private var reloadIcon: Drawable? = null
    private var deleteIcon: Drawable? = null
    private var background: ColorDrawable? = null
    private var isDone = false
    private var delete = false

    @androidx.annotation.NonNull
    private var dataClickListener: DataClickListener? = null
    private var deleteRes: String = "deleteItem"
    private var doneRes: String = "markDone"
    private var reloadRes: String = "reloadItem"

    constructor(
        adapter: DataKeeperAdapter,
        icon: Drawable?,
        icon2: Drawable?,
        icon3: Drawable?,
        color: Int?,
        deleteRes: String,
        doneRes: String,
        reloadRes: String,
        dataClickListener: DataClickListener
    ) : this() {
        this.doneIcon = icon
        this.reloadIcon = icon2
        this.deleteIcon = icon3
        this.dataClickListener = dataClickListener
        this.deleteRes = deleteRes
        this.doneRes = doneRes
        this.reloadRes = reloadRes
        background = ColorDrawable(color!!)
        mAdapter = adapter
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        dataClickListener?.removeItem(position, delete)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (viewHolder != null) {
            mAdapter?.apply {
                isDone = dataItems[viewHolder.absoluteAdapterPosition].isDone
                if (playRecord) stopPlayer()
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c, recyclerView, viewHolder, dX,
            dY, actionState, isCurrentlyActive
        )
        var icon = if (isDone) reloadIcon else doneIcon
        icon = if (dX < 0) {
            dataClickListener?.duringSwipe(deleteRes, true)
            delete = true
            deleteIcon
        } else {
            if (isDone)
                dataClickListener?.duringSwipe(reloadRes, true)
            else
                dataClickListener?.duringSwipe(doneRes, true)

            delete = false
            icon
        }
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        val iconTop = itemView.top + (itemView.height - (icon ?: return).intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight
        when {
            dX > 0 -> { // Swiping to the right
                val iconLeft = itemView.left + icon.intrinsicWidth + 50
                val iconRight = itemView.left + 50
                icon.setBounds(iconRight, iconTop, iconLeft, iconBottom)
                background?.setBounds(
                    itemView.left, itemView.top,
                    itemView.left + dX.toInt() + backgroundCornerOffset,
                    itemView.bottom
                )
            }
            dX < 0 -> { // Swiping to the left
                val iconLeft = itemView.right - icon.intrinsicWidth - 50
                val iconRight = itemView.right - 50
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background?.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom
                )
            }
            else -> { // view is unSwiped
                background?.setBounds(0, 0, 0, 0)
            }
        }
        if (!isCurrentlyActive) {
            dataClickListener?.duringSwipe("", false)
        }
        background?.draw(c)
        icon.draw(c)
    }

}