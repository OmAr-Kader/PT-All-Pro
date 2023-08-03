package com.pt.pro.notepad.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.pt.common.global.*
import com.pt.common.mutual.adapter.GlobalAdapter
import com.pt.common.mutual.adapter.GlobalAdapterLong
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.databinding.TablesBinding
import com.pt.pro.notepad.interfaces.DataCounterListener
import com.pt.pro.notepad.models.TableDetailsHolder
import com.pt.pro.notepad.models.TablesModelMonth
import com.pt.pro.notepad.models.TablesModelUser
import com.pt.pro.notepad.objects.ADD_USER
import com.pt.pro.notepad.objects.getMonth

@Suppress("KotlinConstantConditions")
@android.annotation.SuppressLint("SetTextI18n")
class TableAdapterData(
    audioFiles: MutableList<TablesModelMonth>,
    dataUser: MutableList<TablesModelUser>,
    tableDetails: TableDetailsHolder,
    dataListener: DataCounterListener?,
    isCount: Boolean,
    listCol: MutableList<Int>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val userData = dataUser
    private val isFromNotification = tableDetails.isNotification

    @Volatile
    private var tableIndex = tableDetails.tableIndex
    private val nightRider = tableDetails.nightRider
    private val users = tableDetails.user
    private val details = tableDetails.detail
    private val firstTime = tableDetails.firstTime
    private val tablesNameList: MutableList<TablesModelMonth> = audioFiles
    private val clickListener: DataCounterListener? = dataListener
    private var firstCato: TablesBinding? = null
    private val isCounter = isCount

    @ColorInt
    private val dat = listCol.getINull(0) ?: android.graphics.Color.DKGRAY

    @ColorInt
    private val imp = listCol.getINull(1) ?: android.graphics.Color.DKGRAY

    @ColorInt
    private val lin = listCol.getINull(2) ?: android.graphics.Color.DKGRAY

    private val rem = listCol.getINull(3) ?: android.graphics.Color.DKGRAY

    @ColorInt
    private val ema = listCol.getINull(5) ?: android.graphics.Color.DKGRAY

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder = LayoutInflater.from(parent.context).run {
        if (users) {
            FoldersHolder(TablesBinding.inflate(this, parent, false))
        } else if (!users && !details) {
            MonthHolder(TablesBinding.inflate(this, parent, false))
        } else {
            CatoHolder(TablesBinding.inflate(this, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (users) {
            (holder as FoldersHolder).bind()
        } else if (!users && !details) {
            (holder as MonthHolder).bind()
        } else {
            (holder as CatoHolder).bind()
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (users) {
            (holder as FoldersHolder).attach()
        } else if (!users && !details) {
            (holder as MonthHolder).attach()
        } else {
            (holder as CatoHolder).attach()
        }
    }

    override fun getItemCount(): Int = if (users) userData.size else tablesNameList.size

    override fun getItemViewType(position: Int): Int = run {
        if (users)
            2
        else if (!users && !details)
            3
        else 4
    }

    override fun getItemId(position: Int): Long = position.toLong()

    inner class FoldersHolder(
        item: TablesBinding,
    ) : GlobalAdapterLong<TablesBinding, TablesModelUser>(item) {

        override val Int.item: TablesModelUser
            get() = userData[this@item]

        override fun TablesBinding.bind() {
            tableNameFrame.apply {
                setOnClickListener(this@FoldersHolder)
                setOnLongClickListener(this@FoldersHolder)
            }
        }

        override fun TablesBinding.attach(it: TablesModelUser, i: Int) {
            if (isFromNotification && i == tableIndex) {
                if (nightRider) {
                    android.graphics.Color.WHITE
                } else {
                    android.graphics.Color.BLACK
                }.let {
                    tableName.setTextColor(it)
                }
                ctxA.compactImage(R.drawable.ic_selected_m) {
                    tableName.background = this@compactImage
                }
            }
            if (userData[posA].userName == ADD_USER) {
                ctxA.compactImage(R.drawable.ic_add_user) {
                    tableName.background = this@compactImage
                }
            } else {
                tableName.text = ("<br>" + userData[posA].userName).toHtmlText
                tableName.textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
            }

        }

        override fun TablesBinding.clear() {

        }

        override fun android.view.View.onClick(it: TablesModelUser) {
            if (it.userName == ADD_USER) {
                it.userId?.let { it1 -> clickListener?.longCLick(it1, false, null) }
            } else {
                clickListener?.onPicFirstData(it.userId, userData[0].userId)
            }
        }

        override fun android.view.View.onLongClick(it: TablesModelUser): Boolean {
            return if (users) {
                it.userId?.let { it1 -> clickListener?.longCLick(it1, true, null) }
                true
            } else {
                false
            }
        }
    }

    inner class MonthHolder(
        item: TablesBinding,
    ) : GlobalAdapterLong<TablesBinding, TablesModelMonth>(item) {

        override val Int.item: TablesModelMonth
            get() = tablesNameList[this@item]

        override fun TablesBinding.bind() {
            tableNameFrame.apply {
                setOnClickListener(this@MonthHolder)
                setOnLongClickListener(this@MonthHolder)
            }
        }

        override fun TablesBinding.attach(it: TablesModelMonth, i: Int) {
            if (isFromNotification && i == tableIndex) {
                if (nightRider) {
                    android.graphics.Color.WHITE
                } else {
                    android.graphics.Color.BLACK
                }.let {
                    tableName.setTextColor(it)
                }
                ctxA.compactImage(R.drawable.ic_selected_m) {
                    tableName.background = this@compactImage
                }
            }
            //views.add(tableName)
            (it.tableDisplay ?: "").run {
                split("_".toRegex()).toTypedArray()
            }.let {
                DSack(it[0].length, it[0], it[1].toDefString(4)).getMonth
            }.let {
                tableName.text = it.toHtmlText
            }
            if (!firstTime) {
                ctxA.compactImage(
                    if (posA == tableIndex) R.drawable.ic_selected_m else R.drawable.ic_unselected_m
                ) {
                    tableName.background = this
                }
            }
        }

        override fun TablesBinding.clear() {

        }

        override fun android.view.View.onClick(it: TablesModelMonth) {
            clickListener?.apply {
                it.onPicClickedData(posA, reload = false)
            }
        }

        override fun android.view.View.onLongClick(it: TablesModelMonth): Boolean {
            clickListener?.longCLick("", true, it.mTableName)
            return true
        }
    }


    inner class CatoHolder(
        item: TablesBinding,
    ) : GlobalAdapter<TablesBinding, TablesModelMonth>(item) {

        override val Int.item: TablesModelMonth
            get() = tablesNameList[this@item]

        override fun TablesBinding.bind() {
            tableNameFrame.apply {
                setOnClickListener(this@CatoHolder)
            }
            if (tableIndex != posA) {
                tableName.alpha = 0.75F
            } else {
                tableName.alpha = 1F
            }
            tableName.textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
        }

        override fun TablesBinding.attach(it: TablesModelMonth) {
            if (posA == 0) {
                ctxA.compactImage(
                    R.drawable.ic_selected_m
                ) {
                    tableName.apply {
                        backReColor(themA.findAttr(android.R.attr.colorAccent))
                        background = this@compactImage
                    }
                }
            } else {
                if (isCounter) {
                    counterPN(posA)
                } else {
                    dataDd(posA)
                }
            }
            if (posA == 0) {
                firstCato = this@attach
                tableName.apply {
                    (it.tableDisplay ?: "").run {
                        split("_".toRegex()).toTypedArray()
                    }.let {
                        DSack(it[0].length, it[0], it[1].toDefString(4)).getMonth
                    }.let { month ->
                        text = month.toHtmlText
                    }
                    setTextColor(themA.findAttr(R.attr.rmoText))
                }
            }
        }

        override fun TablesBinding.clear() {

        }

        override fun TablesModelMonth.onClick(i: Int) {
            posA.let { pos ->
                notifyItemRangeChanged(0, tablesNameList.size)
                tableIndex = pos
                if (pos == 0) {
                    clickListener?.apply {
                        onPicClickedData(pos, reload = true)
                    }
                } else {
                    clickListener?.apply {
                        onPicClickedData(pos, reload = false)
                    }
                }
                Unit
            }
        }
    }

    internal fun TablesBinding.dataDd(posA: Int) {
        tableName.apply {
            (tableName.layoutParams as android.widget.FrameLayout.LayoutParams).apply {
                (70 * root.context.resources.displayMetrics.density).let {
                    width = it.toInt()
                    height = it.toInt()
                }
                layoutParams = this
            }
            setScalelessTextSize(11F)
            context.compactImage(R.drawable.ic_done_details) {
                background = this@compactImage
            }
            when (tablesNameList[posA].mTableTime) {
                1L -> {
                    text = context.resources.getString(R.string.dn).compactTextBr
                    setTextColor(context.theme.findAttr(android.R.attr.textColorPrimary))
                    context.theme.findAttr(android.R.attr.colorPrimary).let {
                        backReColor(it)
                    }
                }
                2L -> {
                    text = context.resources.getString(R.string.zn).compactTextBr
                    dat.let {
                        setTextColor(it.isColorDarkText)
                        backReColor(it)
                    }
                }
                3L -> {
                    text = context.resources.getString(R.string.io).compactTextBr
                    imp.let {
                        setTextColor(it.isColorDarkText)
                        backReColor(it)
                    }
                }
                4L -> {
                    text = context.getString(R.string.ur).compactTextBr
                    lin.let {
                        setTextColor(it.isColorDarkText)
                        backReColor(it)
                    }
                }
                5L -> {
                    text = context.getString(R.string.rb).compactTextBr
                    rem.let {
                        setTextColor(it.isColorDarkText)
                        backReColor(it)
                    }
                }
                6L -> {
                    text = context.getString(R.string.pu).compactTextBr
                    ema.let {
                        setTextColor(it.isColorDarkText)
                        backReColor(it)
                    }
                }
                7L -> {
                    text = context.getString(R.string.ps).compactTextBr
                    setTextColor(android.graphics.Color.BLACK)
                    backReColor(context.fetchColor(R.color.clo))
                }
                8L -> {
                    text = context.getString(R.string.ro).compactTextBr
                    setTextColor(android.graphics.Color.WHITE)
                    backReColor(context.fetchColor(R.color.gry))
                }
            }
        }
    }

    internal fun TablesBinding.counterPN(posA: Int) {
        tableName.apply {
            (layoutParams as android.widget.FrameLayout.LayoutParams).apply {
                (70 * root.context.resources.displayMetrics.density).let {
                    width = it.toInt()
                    height = it.toInt()
                }
                layoutParams = this
            }
            setScalelessTextSize(11F)
            text = tablesNameList[posA].tableDisplay
            context.compactImage(R.drawable.ic_done_details) {
                background = this@compactImage
            }
            @Suppress("HardCodedStringLiteral")
            when (tablesNameList[posA].tableDisplay) {
                "positive" -> {
                    text = ("<br>" + "<br>" + "Positive").toHtmlText
                    setTextColor(android.graphics.Color.BLACK)
                    backReColor(context.fetchColor(R.color.clo))
                }
                "negative" -> {
                    text = ("<br>" + "<br>" + "Negative").toHtmlText
                    setTextColor(android.graphics.Color.WHITE)
                    backReColor(context.fetchColor(R.color.gry))
                }
            }
            textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
        }
    }

    private inline val String.compactTextBr: android.text.Spanned get() = "<br><br>$this".toHtmlText

}
