package com.pt.common.mutual.base

interface JobHand : androidx.lifecycle.LifecycleEventObserver {
    /**
    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: com.pt.common.global.InvokingMutable = mutableListOf()
    override var invokerBagList: com.pt.common.global.InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    qHand = androidx.core.os.HandlerCompat.createAsync(ctx.mainLooper)
    exHand = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
    lifecycle.addObserver(this@Fr)
     **/

    var qHand: android.os.Handler?
    var disposableMain: com.pt.common.global.InvokingMutable

    var exHand: java.util.concurrent.ScheduledExecutorService?
    var invokerBagList: com.pt.common.global.InvokingBackMutable

    var lastJob: kotlinx.coroutines.Job?

    val destroyInvoker: (com.pt.common.global.DSackV<Invoker?, Int>) -> Unit
        get() = {
            it.one?.inv = null
            it.one = null
        }

    val destroyBackInvoker: (InvokerBag) -> Unit
        get() = {
            it.invoker.inv = null
            it.cancellable?.cancel(true)
            it.cancellable = null
        }

    override fun onStateChanged(
        source: androidx.lifecycle.LifecycleOwner,
        event: androidx.lifecycle.Lifecycle.Event
    ) {
        if (event == androidx.lifecycle.Lifecycle.Event.ON_DESTROY) {
            stateDestroy()
        }
    }

    fun stateDestroy() {
        com.pt.common.stable.catchyUnit {
            qHand?.removeCallbacksAndMessages(null)
            unPostAll()
            unBackPostAll()
            disposableMain?.clear()
            invokerBagList?.clear()
            lastJob?.cancel()
            exHand?.shutdown()
            disposableMain = null
            invokerBagList = null
            qHand = null
            exHand = null
            lastJob = null
        }
    }

    fun unPostAll() {
        com.pt.common.stable.catchy(Unit) {
            disposableMain?.apply {
                synchronized<Unit>(this@apply) {
                    com.pt.common.stable.catchyUnit {
                        onEach { it.one?.inv = null }.let {
                            this@apply.clear()
                        }.also {
                            qHand?.removeCallbacksAndMessages(null)
                        }
                    }
                }
            }
        }
    }

    fun unPost(id: Int) {
        com.pt.common.stable.catchy(Unit) {
            disposableMain?.apply {
                synchronized(this) {
                    unPostList(id)
                }
            }
        }
    }

    fun com.pt.common.global.InvokingMutable.unPostList(id: Int) {
        com.pt.common.stable.catchy(Unit) {
            if (com.pt.common.global.isV_N) {
                this@unPostList?.stream()?.filter { it.two == id }?.forEach(destroyInvoker)
                this@unPostList?.removeIf {
                    it.two == id
                }
            } else {
                this@unPostList?.asSequence()?.filter { it.two == id }?.forEach(destroyInvoker)
                this@unPostList?.removeAll {
                    it.two == id
                }
            }
        }
    }

    fun com.pt.common.global.DSack<() -> Unit, Int, Long>.postAfter() {
        disposableMain?.apply {
            synchronized<Unit>(this@apply) {
                com.pt.common.stable.catchyUnit {
                    this@apply.unPostList(two)
                    Invoker(one).also {
                        this@apply.add(com.pt.common.global.DSackV(it, two))
                        qHand?.postDelayed(it, three)
                    }
                }
            }
        }
    }

    fun com.pt.common.global.DSack<() -> Unit, Int, Long>.postAfterIfNot() {
        disposableMain?.apply {
            synchronized<Unit>(this@apply) {
                com.pt.common.stable.catchyUnit {
                    any {
                        it.two == two && it.one?.inv != null
                    }.let { match ->
                        if (match) {
                            return
                        } else {
                            Invoker(one).also {
                                this@apply.add(com.pt.common.global.DSackV(it, two))
                                qHand?.postDelayed(it, three)
                            }
                        }
                    }
                }
            }
        }
    }

    fun com.pt.common.global.DSackT<() -> Unit, Int>.postNow() {
        unPost(two)
        qHand?.post(Invoker(one))
    }

    fun pushJob(
        b: (kotlinx.coroutines.Job?) -> kotlinx.coroutines.Job
    ): kotlinx.coroutines.Job? {
        return lastJob.let { prev ->
            return@let b.invoke(prev).also {
                lastJob = it
            }
        }
    }

    fun onDestroyJobs() {
        lastJob?.cancel()
        lastJob = null
    }


    fun unBackPostAll() {
        com.pt.common.stable.catchy(Unit) {
            invokerBagList?.apply {
                synchronized<Unit>(this@apply) {
                    com.pt.common.stable.catchyUnit {
                        onEach(destroyBackInvoker).let {
                            this@apply.clear()
                        }
                    }
                }
            }
        }
    }

    fun unBackPost(id: Int) {
        com.pt.common.stable.catchy(Unit) {
            invokerBagList?.apply {
                synchronized(this) {
                    unPostBackList(id)
                }
            }
        }
    }

    fun com.pt.common.global.InvokingBackMutable.unPostBackList(id: Int) {
        com.pt.common.stable.catchy(Unit) {
            if (com.pt.common.global.isV_N) {
                this@unPostBackList?.stream()?.filter { it.idInvoker == id }?.forEach(destroyBackInvoker)
                this@unPostBackList?.removeIf {
                    it.idInvoker == id
                }
            } else {
                this@unPostBackList?.asSequence()?.filter { it.idInvoker == id }?.forEach(destroyBackInvoker)
                this@unPostBackList?.removeAll {
                    it.idInvoker == id
                }
            }
        }
    }

    fun com.pt.common.global.DSack<() -> Unit, Int, Long>.postBackAfter() {
        invokerBagList?.apply {
            synchronized<Unit>(this@apply) {
                com.pt.common.stable.catchyUnit {
                    this@apply.unPostBackList(two)
                    Invoker(one).also { inv ->
                        exHand.also {
                            if (it != null) {
                                if (it.isShutdown) {
                                    java.util.concurrent.Executors.newSingleThreadScheduledExecutor().also { newOne ->
                                        exHand = newOne
                                    }?.schedule<Unit>(inv, three, java.util.concurrent.TimeUnit.MILLISECONDS).also { can ->
                                        this@apply.add(InvokerBag(inv, can, two))
                                    }
                                } else {
                                    exHand?.schedule<Unit>(inv, three, java.util.concurrent.TimeUnit.MILLISECONDS).also { can ->
                                        this@apply.add(InvokerBag(inv, can, two))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*fun com.pt.common.global.DSack<() -> Unit, Int, Long>.postBackAfterIfNot() {
        invokerBagList?.apply {
            synchronized<Unit>(this@apply) {
                com.pt.common.stable.catchyUnit {
                    any {
                        it.idInvoker == two && it.invoker.inv != null
                    }.let { match ->
                        if (match) {
                            return
                        } else {
                            Invoker(one).also { inv ->
                                exHand.also {
                                    if (it != null) {
                                        if (it.isShutdown) {
                                            java.util.concurrent.Executors.newSingleThreadScheduledExecutor().also { newOne ->
                                                exHand = newOne
                                            }?.schedule<Unit>(inv, three, java.util.concurrent.TimeUnit.MILLISECONDS).also { can ->
                                                this@apply.add(InvokerBag(inv, can, two))
                                            }
                                        } else {
                                            exHand?.schedule<Unit>(inv, three, java.util.concurrent.TimeUnit.MILLISECONDS).also { can ->
                                                this@apply.add(InvokerBag(inv, can, two))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun com.pt.common.global.DSackT<() -> Unit, Int>.postBackNow() {
        unBackPost(two)
        exHand.also {
            if (it != null) {
                if (it.isShutdown) {
                    java.util.concurrent.Executors.newSingleThreadScheduledExecutor().also { newOne ->
                        exHand = newOne
                    }?.execute(Invoker(one))
                } else {
                    it.execute(Invoker(one))
                }
            }
        }
    }*/

}