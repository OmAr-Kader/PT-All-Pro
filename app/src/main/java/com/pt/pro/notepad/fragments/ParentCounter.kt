package com.pt.pro.notepad.fragments

import android.widget.ArrayAdapter
import com.pt.common.global.InvokingMutable
import com.pt.common.global.findIntegerPrefDb
import com.pt.common.mutual.life.GlobalFragment
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.databinding.FragmentCounterBinding
import com.pt.pro.notepad.adapters.CounterDaysAdapter
import com.pt.pro.notepad.adapters.TableAdapterData
import com.pt.pro.notepad.interfaces.CounterListener
import com.pt.pro.notepad.interfaces.DataCounterListener
import com.pt.pro.notepad.models.CounterUpdate
import com.pt.pro.notepad.models.TablesModelMonth
import com.pt.pro.notepad.objects.*

abstract class ParentCounter : GlobalFragment<FragmentCounterBinding>(), CounterListener,
    DataCounterListener {

    protected var editableCounterNative: MutableList<CounterUpdate>? = mutableListOf()
    protected inline val editableCounter: MutableList<CounterUpdate> get() = editableCounterNative!!
    protected var stringsNative: MutableList<String>? = mutableListOf()
    protected inline val strings: MutableList<String> get() = stringsNative!!

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: com.pt.common.global.InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    protected var tablesModelCounters: MutableList<TablesModelMonth>? = null
    protected var adapterText: ArrayAdapter<String>? = null
    protected var tableAdapterCounter: TableAdapterData? = null
    protected var counterDaysAdapter: CounterDaysAdapter? = null
    protected var tableUserName: String? = null
    protected var tableCounter: String? = null
    protected val tableCount: String get() = tableCounter!!

    protected var tableTimeCounter: Long = 0
    protected var counterBoolean: Boolean = true
    protected var counterSaveSize: Int = 0

    protected var waitForDetails: Boolean = false
    protected var tableIndex: Int = 0
    protected var currentDay: Int = 0
    protected var currentKey: String = ""
    protected var isSaved: Boolean = false

    internal inline val allStringResource: String get() = R.string.ce.dStr

    protected inline val android.content.Context.fetchColorList: suspend () -> MutableList<Int>
        get() = {
            justCoroutine {
                mutableListOf<Int>().apply {
                    findIntegerPrefDb(COL_KEEP, DATA_COL, datCol).also(::add)
                    findIntegerPrefDb(COL_KEEP, IMP_COL, impCol).also(::add)
                    findIntegerPrefDb(COL_KEEP, LINK_COL, linCol).also(::add)
                    findIntegerPrefDb(COL_KEEP, REM_COL, remCol).also(::add)
                    findIntegerPrefDb(COL_KEEP, SCAN_COL, scaCol).also(::add)
                    findIntegerPrefDb(COL_KEEP, MAIL_COL, emaCol).also(::add)
                }
            }
        }


    override fun onDestroyView() {
        editableCounterNative = null
        stringsNative = null
        adapterText = null
        tablesModelCounters = null
        tableAdapterCounter = null
        counterDaysAdapter = null
        tableUserName = null
        tableCounter = null
        super.onDestroyView()
    }

}