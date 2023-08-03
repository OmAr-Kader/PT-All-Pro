package com.pt.pro.extra.screener

import com.pt.common.global.*
import com.pt.common.stable.rKTSack

class FragmentNoteHead : com.pt.common.mutual.life.GlobalSimpleFragment<com.pt.pro.databinding.NoteHeadFragmentBinding>() {

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.databinding.NoteHeadFragmentBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root
        }


    @com.pt.common.global.UiAnn
    override fun com.pt.pro.databinding.NoteHeadFragmentBinding.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentNoteHead)
        //run.rKTSack(100L).postAfter()
    }


    override fun onResume() {
        super.onResume()
        run.rKTSack(100L).postAfter()
    }

    override fun onPause() {
        unPost(run.two)
        super.onPause()
    }


    private val run: DSackT<() -> Unit, Int>
        get() = com.pt.common.stable.toCatchSack(22) {
            binder?.constraint?.apply {
                com.pt.common.stable.catchy(Unit) {
                    if (isVis) {
                        invisibleFade(300L)
                        run.rKTSack(1600L).postAfter()
                    } else {
                        visibleFade(300L)
                        run.rKTSack(2000L).postAfter()
                    }
                }
            }
            Unit
        }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.databinding.NoteHeadFragmentBinding.onClick(v: android.view.View) {

    }

}