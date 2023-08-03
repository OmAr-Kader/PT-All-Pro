package com.pt.common.stable

import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import kotlinx.coroutines.*

inline val <T> T.getList: MutableList<T>
    get() = mutableListOf(this@getList)

inline val <T> MutableList<T>.getI: (Int) -> T
    get() = {
        when {
            it < 0 -> this@getI[0]
            it > this@getI.lastIndex -> this@getI.last()
            else -> this@getI[it]
        }
    }

fun <T> Sequence<T>.remove(element: T): Sequence<T> {
    return catchy(this@remove) {
        sequence {
            /*this@remove.forEach {
                if (it != element) yield(it)
            }*/
            filter {
                it != element
            }.let {
                yieldAll(it)
            }
        }
    }
}

fun <T> Sequence<T>.add(element: T): Sequence<T> {
    return catchy(this@add) {
        sequence {
            yieldAll(this@add)
            yield(element)
        }
    }
}

inline val <T> MutableList<T>.getINull: (Int) -> T?
    get() = {
        catchy(null) {
            when {
                this@getINull.isEmpty() -> null
                it < 0 -> this@getINull[0]
                it > this@getINull.lastIndex -> this@getINull.last()
                else -> this@getINull[it]
            }
        }
    }

fun <T> MutableList<T>.removeAtIndex(ind: Int): Unit = catchy(Unit) {
    if (size > 0 && (ind in 0 until size)) removeAt(ind)
}

fun <K, V> androidx.collection.ArrayMap<K, V>.removeAtIndexMap(ind: Int): Unit = catchy(Unit) {
    if (size > 0 && (ind in 0 until size)) removeAt(ind)
}

fun android.view.ViewGroup.removeChild(child: androidx.appcompat.widget.AppCompatTextView?): Unit = catchy(Unit) {
    if (childCount > 0) removeView(child ?: return@catchy)
}

inline val <T> MutableList<T>.fetchIndex: (T, Int) -> Int
    get() = { t, i ->
        this@fetchIndex.indexOf(t).let {
            if (it == -1) {
                i
            } else {
                it
            }
        }
    }

inline fun <R> catchy(def: R, block: () -> R): R {
    return try {
        block.invoke()
    } catch (e: Exception) {
        e.listThrowable()
        def
    } catch (e: RuntimeException) {
        e.listThrowable()
        def
    } catch (e: IllegalArgumentException) {
        e.listThrowable()
        def
    } catch (e: IllegalStateException) {
        e.listThrowable()
        def
    } catch (e: UnsupportedOperationException) {
        e.listThrowable()
        def
    } catch (e: NullPointerException) {
        e.listThrowable()
        def
    } catch (e: Throwable) {
        e.listThrowable()
        def
    } catch (e: ClassCastException) {
        e.listThrowable()
        def
    } catch (e: IndexOutOfBoundsException) {
        e.listThrowable()
        def
    } catch (e: NoSuchElementException) {
        e.listThrowable()
        def
    }
}

inline fun catchyUnit(block: () -> Unit) {
    try {
        block.invoke()
    } catch (e: Exception) {
        e.listThrowable()
    } catch (e: RuntimeException) {
        e.listThrowable()
    } catch (e: IllegalArgumentException) {
        e.listThrowable()
    } catch (e: IllegalStateException) {
        e.listThrowable()
    } catch (e: UnsupportedOperationException) {
        e.listThrowable()
    } catch (e: NullPointerException) {
        e.listThrowable()
    } catch (e: Throwable) {
        e.listThrowable()
    } catch (e: ClassCastException) {
        e.listThrowable()
    } catch (e: IndexOutOfBoundsException) {
        e.listThrowable()
    } catch (e: NoSuchElementException) {
        e.listThrowable()
    }
}

fun android.content.Context?.catchyBadToken(block: () -> Unit) {
    kotlin.runCatching {
        try {
            block.invoke()
        } catch (it: android.view.WindowManager.BadTokenException) {
            this@catchyBadToken?.postFun {
                try {
                    block.invoke()
                } catch (it: android.view.WindowManager.BadTokenException) {
                    it.listThrowable()
                } catch (it: IllegalArgumentException) {
                    it.listThrowable()
                }
            }
        } catch (it: IllegalArgumentException) {
            this@catchyBadToken?.postFun {
                try {
                    block.invoke()
                } catch (it: android.view.WindowManager.BadTokenException) {
                    it.listThrowable()
                } catch (it: IllegalArgumentException) {
                    it.listThrowable()
                }
            }
        }
    }.getOrElse {
        it.listThrowable()
    }
}

fun android.content.Context.postFun(a: () -> Unit) {
    androidx.core.os.HandlerCompat.createAsync(
        this@postFun.mainLooper ?: return
    ).post(a)
}


suspend inline fun <R> catchySus(def: R, crossinline block: suspend () -> R): R {
    return try {
        block.invoke()
    } catch (e: Exception) {
        e.listThrowable()
        def
    } catch (e: RuntimeException) {
        e.listThrowable()
        def
    } catch (e: IllegalArgumentException) {
        e.listThrowable()
        def
    } catch (e: IllegalStateException) {
        e.listThrowable()
        def
    } catch (e: UnsupportedOperationException) {
        e.listThrowable()
        def
    } catch (e: NullPointerException) {
        e.listThrowable()
        def
    } catch (e: Throwable) {
        e.listThrowable()
        def
    } catch (e: ClassCastException) {
        e.listThrowable()
        def
    } catch (e: IndexOutOfBoundsException) {
        e.listThrowable()
        def
    } catch (e: NoSuchElementException) {
        e.listThrowable()
        def
    }
}


@Suppress("LongLine")
inline val com.pt.common.global.DSackT<() -> Unit, Int>.rKTSack: (Long) -> com.pt.common.global.DSack<() -> Unit, Int, Long>
    get() = {
        com.pt.common.global.DSack(one, two, it)
    }


/*@get:androidx.annotation.CheckResult("Not Posted Yey")
inline val toCatchSack: (Int, (() -> Unit)) -> com.pt.common.global.DSackT<() -> Unit, Int>
    get() = @androidx.annotation.CheckResult("Not Posted Yey") { id, inv ->
        object : () -> Unit {
            override fun invoke() {
                kotlin.runCatching {
                    inv.invoke()
                }.onFailure {
                    it.toStr.let { s ->
                        if (!s.contains("Cancelled".toRegex())) s.logProvCrash("toCatchRunSack")
                    }
                }
            }
        }.let<() -> Unit, com.pt.common.global.DSackT<() -> Unit, Int>> {
            com.pt.common.global.DSackT(it, id)
        }
    }*/


@androidx.annotation.CheckResult("Not Posted Yey")
fun toCatchSack(id: Int, inv: (() -> Unit)): com.pt.common.global.DSackT<() -> Unit, Int> {
    return object : () -> Unit {
        override fun invoke() {
            kotlin.runCatching {
                inv.invoke()
            }.onFailure {
                it.toStr.let { s ->
                    if (!s.contains("Cancelled".toRegex())) s.logProvCrash("toCatchRunSack")
                }
            }
        }
    }.let<() -> Unit, com.pt.common.global.DSackT<() -> Unit, Int>> {
        com.pt.common.global.DSackT(it, id)
    }
}

/*inline val toCatchSackAfter: (Int, Long, (() -> Unit)) -> com.pt.common.global.DSack<() -> Unit, Int, Long>
    get() = { id, del, inv ->
        object : () -> Unit {
            override fun invoke() {
                kotlin.runCatching {
                    inv.invoke()
                }.onFailure {
                    it.toStr.let { s ->
                        if (!s.contains("Cancelled".toRegex())) s.logProvCrash("toCatchRunSack")
                    }
                }
            }
        }.let<() -> Unit, com.pt.common.global.DSack<() -> Unit, Int, Long>> {
            com.pt.common.global.DSack(it, id, del)
        }
    }*/

@androidx.annotation.CheckResult("Not Posted Yey")
fun toCatchSackAfter(id: Int, del: Long, inv: (() -> Unit)): com.pt.common.global.DSack<() -> Unit, Int, Long> {
    return object : () -> Unit {
        override fun invoke() {
            kotlin.runCatching {
                inv.invoke()
            }.onFailure {
                it.toStr.let { s ->
                    if (!s.contains("Cancelled".toRegex())) s.logProvCrash("toCatchRunSack")
                }
            }
        }
    }.let<() -> Unit, com.pt.common.global.DSack<() -> Unit, Int, Long>> {
        com.pt.common.global.DSack(it, id, del)
    }
}

inline val fetchExtractor: java.util.concurrent.ExecutorService
    get() {
        return if (com.pt.common.global.isV_N) {
            java.util.concurrent.Executors.newWorkStealingPool()
        } else {
            runCatching {
                java.util.concurrent.Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors()
                )
            }.getOrElse {
                java.util.concurrent.Executors.newSingleThreadExecutor()
            }
        }
    }

inline val android.content.Context?.fetchMainExtractor: java.util.concurrent.Executor
    get() {
        return if (com.pt.common.global.isV_P) {
            this?.mainExecutor ?: Dispatchers.Main.asExecutor()
        } else {
            Dispatchers.Main.asExecutor()
        }
    }

fun Throwable.listThrowable(log: String = "toCatchSusRun") {
    try {
        if (com.pt.common.BuildConfig.VERSION_DEBUG) {
            stackTraceToString().logProvCrash(log)
        }
    } catch (_: java.io.IOException) {
    } catch (_: Exception) {
    }
}

internal const val BACK: String = "Work"
internal const val BACK_IO: String = "Work_Io"

internal const val MAIN: String = "Main"
internal const val MAIN_Imm: String = "Main_imm"

@get:androidx.annotation.WorkerThread
inline val <T> T.toCatchSusRun: (suspend CoroutineScope.() -> T) -> (suspend CoroutineScope.() -> T)
    get() = {
        object : suspend CoroutineScope.() -> T {
            override suspend fun invoke(
                p1: CoroutineScope
            ): T = try {
                it.invoke(p1)
            } catch (e: CancellationException) {
                e.listThrowable()
                this@toCatchSusRun
            } catch (e: Throwable) {
                e.listThrowable()
                this@toCatchSusRun
            } catch (e: Exception) {
                e.listThrowable()
                this@toCatchSusRun
            } catch (e: RuntimeException) {
                e.listThrowable()
                this@toCatchSusRun
            } catch (e: IllegalArgumentException) {
                e.listThrowable()
                this@toCatchSusRun
            } catch (e: IllegalStateException) {
                e.listThrowable()
                this@toCatchSusRun
            } catch (e: UnsupportedOperationException) {
                e.listThrowable()
                this@toCatchSusRun
            } catch (e: NullPointerException) {
                e.listThrowable()
                this@toCatchSusRun
            } catch (e: ClassCastException) {
                e.listThrowable()
                this@toCatchSusRun
            } catch (e: IndexOutOfBoundsException) {
                e.listThrowable()
                this@toCatchSusRun
            } catch (e: NoSuchElementException) {
                e.listThrowable()
                this@toCatchSusRun
            }
        }
    }

@get:androidx.annotation.UiThread
inline val <T> T.toCatchSusRunTimeOut: (suspend CoroutineScope.() -> T) -> (suspend CoroutineScope.() -> T)
    get() = {
        object : suspend CoroutineScope.() -> T {
            override suspend fun invoke(
                p1: CoroutineScope
            ): T = try {
                withTimeoutOrNull(4500L) {
                    it.invoke(p1)
                } ?: this@toCatchSusRunTimeOut
            } catch (e: CancellationException) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            } catch (e: Throwable) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            } catch (e: Exception) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            } catch (e: RuntimeException) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            } catch (e: IllegalArgumentException) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            } catch (e: IllegalStateException) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            } catch (e: UnsupportedOperationException) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            } catch (e: NullPointerException) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            } catch (e: ClassCastException) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            } catch (e: IndexOutOfBoundsException) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            } catch (e: NoSuchElementException) {
                e.listThrowable()
                this@toCatchSusRunTimeOut
            }
        }
    }

suspend fun <T> withBackDef(
    def: T,
    @androidx.annotation.WorkerThread block: suspend CoroutineScope.() -> T,
): T = justCoroutine {
    return@justCoroutine coroutineContext[CoroutineName.Key]?.name.letSusBack {
        if (it == BACK_IO) {
            def.toCatchSusRun(block).invoke(this@justCoroutine)
        } else {
            withContext(
                SupervisorJob() +
                        Dispatchers.IO + CoroutineName(BACK_IO) +
                        CoroutineExceptionHandler { _, _ -> },
                def.toCatchSusRun(block)
            )
        }
    }
}

suspend fun withBack(
    shouldBack: Boolean = false,
    @androidx.annotation.WorkerThread block: suspend CoroutineScope.() -> Unit,
) {
    justCoroutine {
        coroutineContext[CoroutineName.Key]?.name.letSusBack {
            if (it == BACK_IO || (it == BACK || !shouldBack)) {
                Unit.toCatchSusRun(block).invoke(this@justCoroutine)
            } else {
                withContext(
                    SupervisorJob() +
                            Dispatchers.IO + CoroutineName(BACK_IO) +
                            CoroutineExceptionHandler { _, _ -> },
                    Unit.toCatchSusRun(block)
                )
            }
        }
    }
}

suspend fun withDefault(
    @androidx.annotation.WorkerThread block: suspend CoroutineScope.() -> Unit,
) {
    justCoroutine {
        coroutineContext[CoroutineName.Key]?.name.letSusBack {
            if (it == BACK || it == BACK_IO) {
                Unit.toCatchSusRun(block).invoke(this@justCoroutine)
            } else {
                withContext(
                    SupervisorJob() +
                            Dispatchers.Default + CoroutineName(BACK) +
                            CoroutineExceptionHandler { _, _ -> },
                    Unit.toCatchSusRun(block)
                )
            }
        }
    }
}

suspend fun <T> withDefaultDef(
    def: T,
    @androidx.annotation.WorkerThread block: suspend CoroutineScope.() -> T,
): T = justCoroutine {
    return@justCoroutine coroutineContext[CoroutineName.Key]?.name.letSusBack {
        if (it == BACK || it == BACK_IO) {
            def.toCatchSusRun(block).invoke(this@justCoroutine)
        } else {
            withContext(
                SupervisorJob() +
                        Dispatchers.Default + CoroutineName(BACK) +
                        CoroutineExceptionHandler { _, _ -> },
                def.toCatchSusRun(block)
            )
        }
    }
}

suspend fun <T> withMainDef(
    def: T,
    @androidx.annotation.UiThread block: suspend CoroutineScope.() -> T,
): T = justCoroutine {
    coroutineContext[CoroutineName.Key]?.name.letSusBack {
        if (it == MAIN || it == MAIN_Imm) {
            def.toCatchSusRunTimeOut(block).invoke(this@justCoroutine)
        } else {
            withContext(
                SupervisorJob() +
                        Dispatchers.Main.immediate + CoroutineName(MAIN_Imm) +
                        CoroutineExceptionHandler { _, _ -> },
                def.toCatchSusRunTimeOut(block)
            )
        }
    }
}

suspend fun withMain(
    @androidx.annotation.UiThread block: suspend CoroutineScope.() -> Unit,
) {
    justCoroutine {
        coroutineContext[CoroutineName.Key]?.name.letSusBack {
            if (it == MAIN_Imm) {
                Unit.toCatchSusRunTimeOut(block).invoke(this@justCoroutine)
            } else {
                withContext(
                    SupervisorJob() +
                            Dispatchers.Main.immediate + CoroutineName(MAIN_Imm) +
                            CoroutineExceptionHandler { _, _ -> },
                    Unit.toCatchSusRunTimeOut(block)
                )
            }
        }
    }
}

suspend fun withMainNormal(
    @androidx.annotation.UiThread block: suspend CoroutineScope.() -> Unit,
) {
    justCoroutine {
        coroutineContext[CoroutineName.Key]?.name.letSusBack {
            if (it == MAIN || it == MAIN_Imm) {
                Unit.toCatchSusRunTimeOut(block).invoke(this@justCoroutine)
            } else {
                withContext(
                    SupervisorJob() +
                            Dispatchers.Main + CoroutineName(MAIN) +
                            CoroutineExceptionHandler { _, _ -> },
                    Unit.toCatchSusRunTimeOut(block)
                )
            }
        }
    }
}

suspend fun <T> withBackCurDef(
    def: T,
    @androidx.annotation.WorkerThread block: suspend CoroutineScope.() -> T,
): T = justCoroutine {
    coroutineContext[CoroutineName.Key]?.name.letSusBack {
        if (it == BACK_IO) {
            def.toCatchSusRun(block).invoke(this@justCoroutine)
        } else {
            withContext(
                SupervisorJob() +
                        Dispatchers.IO + CoroutineName(BACK_IO) +
                        CoroutineExceptionHandler { _, _ -> },
                def.toCatchSusRun(block)
            )
        }
    }
}

suspend fun android.content.Context?.nullChecker() {
    justCoroutine {
        if (this@nullChecker == null) cancelScope()
    }
}

@com.pt.common.global.CurAnn
@androidx.annotation.WorkerThread
suspend fun <R> justCoroutineCur(
    @androidx.annotation.WorkerThread block: suspend CoroutineScope.() -> R,
): R {
    return supervisorScope(block)
}

fun CoroutineScope.cancelScope() {
    coroutineContext.apply {
        synchronized<Unit>(this@apply) {
            catchyUnit {
                this@apply.cancelChildren()
                this@apply.cancel()
            }
        }
    }
    catchyUnit {
        cancel()
    }
}

fun Job.cancelJob() {
    catchy(Unit) {
        cancelChildren()
        cancel()
    }
}

fun CoroutineScope.launchImdMain(
    @androidx.annotation.UiThread block: suspend CoroutineScope.() -> Unit,
): Job {
    return launch(
        SupervisorJob() +
                Dispatchers.Main + CoroutineName(MAIN_Imm) +
                CoroutineExceptionHandler { _, _ -> },
        CoroutineStart.DEFAULT,
        Unit.toCatchSusRunTimeOut(block),
    )
}

fun CoroutineScope.launchMain(
    @androidx.annotation.UiThread block: suspend CoroutineScope.() -> Unit,
): Job {
    return launch(
        SupervisorJob() +
                Dispatchers.Main + CoroutineName(MAIN) +//.immediate
                CoroutineExceptionHandler { _, _ -> },
        CoroutineStart.DEFAULT,
        Unit.toCatchSusRunTimeOut(block),
    )
}


fun CoroutineScope.launchDef(
    @androidx.annotation.WorkerThread block: suspend CoroutineScope.() -> Unit,
): Job {
    return launch(
        SupervisorJob() +
                Dispatchers.Default + CoroutineName(BACK) +
                CoroutineExceptionHandler { _, _ -> },
        CoroutineStart.DEFAULT,
        Unit.toCatchSusRun(block),
    )
}

suspend fun Job?.checkIfDone() {
    if (this@checkIfDone?.isCompleted == false) this@checkIfDone.join() else return
}

suspend fun <R> justCoroutine(
    block: suspend CoroutineScope.() -> R,
): R {
    return supervisorScope(block)
}

suspend fun justScope(
    block: suspend CoroutineScope.() -> Unit,
): Unit = supervisorScope(Unit.toCatchSusRun(block))

@androidx.annotation.UiThread
suspend fun justCoroutineMain(
    @androidx.annotation.UiThread block: suspend CoroutineScope.() -> Unit,
): Unit = supervisorScope(Unit.toCatchSusRunTimeOut(block))

@androidx.annotation.WorkerThread
suspend fun justCoroutineBack(
    @androidx.annotation.WorkerThread block: suspend CoroutineScope.() -> Unit,
): Unit = supervisorScope(Unit.toCatchSusRun(block))

suspend inline fun <T, X, C : MutableList<T>> C.onEachSus(
    x: X, crossinline action: suspend T.() -> Unit,
): C {
    return justCoroutine {
        this@onEachSus.applySus {
            for (element in this) {
                if (x == null) {
                    this@onEachSus.clear()
                    break
                }
                action(element)
            }
        }
    }
}

suspend inline fun <T, X, C : MutableList<T>> C.onEachSusBack(
    x: X, crossinline action: suspend T.() -> Unit,
): C {
    return justCoroutine {
        return@justCoroutine this@onEachSusBack.applySusBack {
            try {
                for (element in this) {
                    if (x == null) {
                        this@onEachSusBack.clear()
                        break
                    }
                    action(element)
                }
            } catch (e: java.util.NoSuchElementException) {
                this@onEachSusBack.clear()
                return@applySusBack
            }
        }
    }
}

suspend inline fun <T, X, C : Sequence<T>> C.onEachBack(
    x: X, crossinline action: suspend T.() -> Unit,
): C {
    return justCoroutine {
        return@justCoroutine this@onEachBack.applySusBack {
            try {
                for (element in this) {
                    if (x == null) {
                        break
                    }
                    action(element)
                }
            } catch (e: java.util.NoSuchElementException) {
                return@applySusBack
            }
        }
    }
}

suspend inline fun <T, X, C : MutableList<T>> C.onEachIndexedSusBack(
    x: X, crossinline action: suspend (index: Int, T) -> Unit,
): C {
    var index = 0
    return justCoroutine {
        this@onEachIndexedSusBack.applySusBack {
            for (item in this) {
                if (x == null) {
                    this@onEachIndexedSusBack.clear()
                    break
                }
                action(index++, item)
            }
        }
    }
}

suspend inline fun <T, X, C : MutableList<T>> C.onEachIndexedSus(
    x: X, crossinline action: suspend (index: Int, T) -> Unit,
): C {
    var index = 0
    return justCoroutine {
        this@onEachIndexedSus.applySus {
            for (item in this) {
                if (x == null) {
                    this@onEachIndexedSus.clear()
                    break
                }
                action(index++, item)
            }
        }
    }
}

inline val android.database.Cursor.curInt: String.() -> Int
    get() = {
        getInt(getColumnIndexOrThrow(this))
    }

inline val android.database.Cursor.curIntNull: String.() -> Int?
    get() = {
        getInt(getColumnIndexOrThrow(this))
    }

inline val android.database.Cursor.curStr: String.() -> String?
    get() = {
        getString(getColumnIndexOrThrow(this))
    }

inline val android.database.Cursor.curLongTime: com.pt.common.global.FileLate.() -> Long
    get() = {
        getLong(2).let { lo ->
            if (lo == 0L) {
                getLong(3).let { lo2 ->
                    if (lo2 == 0L) {
                        lastModified()
                    } else {
                        lo2
                    }
                }
            } else {
                lo
            }
        }.let {
            if (it.length == 13) it else it * 1000
        }
    }

inline val Long.createTime: Long
    get() {
        return if (length == 13) this else this * 1000
    }

inline val Long.length: Int
    get() {
        return when (this) {
            0L -> 1
            else -> kotlin.math.log10(kotlin.math.abs(toDouble())).toInt() + 1
        }
    }

inline val android.database.Cursor.curLong: String.() -> Long
    get() = {
        getLong(getColumnIndexOrThrow(this))
    }

inline val android.database.Cursor.curLongNull: String.() -> Long
    get() = {
        getLong(getColumnIndexOrThrow(this))
    }

inline val android.database.Cursor.curBool: String.() -> Boolean
    get() = {
        getInt(getColumnIndexOrThrow(this)) == 1
    }

inline val android.database.Cursor.curDouble: String.() -> Double
    get() = {
        getDouble(getColumnIndexOrThrow(this))
    }

inline val android.database.Cursor.curName: String?
    get() {
        return com.pt.common.global.FileLate(curStr(PATH).toString()).run {
            nameWithoutExtension.ifEmpty {
                name
            }
        }
    }

inline val PEND_FLAG: Int
    get() {
        return if (com.pt.common.global.isV_M) {
            android.app.PendingIntent.FLAG_IMMUTABLE or
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            android.app.PendingIntent.FLAG_UPDATE_CURRENT
        }
    }

inline fun ensureBack(crossinline a: () -> Unit) {
    Thread {
        a.invoke()
    }.apply {
        priority = Thread.MAX_PRIORITY
    }.start()
}