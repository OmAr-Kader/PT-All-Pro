package com.pt.common.stable

import com.pt.common.global.WorkerAnn

@kotlin.jvm.Throws(android.database.StaleDataException::class)
@com.pt.common.global.CurAnn
@WorkerAnn
suspend inline fun <T, R> T.useBack(
    @WorkerAnn crossinline block: suspend T.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return this@useBack?.let {
        when {
            it::class.java.isAssignableFrom(java.io.Closeable::class.java) -> {
                when (this@useBack) {
                    is java.io.Closeable? -> useSus(block)
                    else -> useNormal(block)
                }
            }
            it::class.java.isAssignableFrom(AutoCloseable::class.java) -> {
                when (this@useBack) {
                    is AutoCloseable? -> useSusFile(block)
                    else -> useNormal(block)
                }
            }
            else -> useNormal(block)
        }
    } ?: useNormal(block)
}

@kotlin.jvm.Throws(android.database.StaleDataException::class)
@com.pt.common.global.CurAnn
@WorkerAnn
inline fun <T, R> T.useMy(
    @WorkerAnn crossinline block: T.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return this@useMy?.let {
        when {
            it::class.java.isAssignableFrom(java.io.Closeable::class.java) -> {
                when (this@useMy) {
                    is java.io.Closeable? -> use(block)
                    else -> useMyNormal(block)
                }
            }
            it::class.java.isAssignableFrom(AutoCloseable::class.java) -> {
                when (this@useMy) {
                    is AutoCloseable? -> use(block)
                    else -> useMyNormal(block)
                }
            }
            else -> useMyNormal(block)
        }
    } ?: useMyNormal(block)
}

@kotlin.jvm.Throws(android.database.StaleDataException::class)
@com.pt.common.global.CurAnn
@WorkerAnn
suspend inline fun <IT, R> IT.useSusIT(
    @WorkerAnn crossinline block: suspend (IT) -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return this@useSusIT?.let {
        when {
            it::class.java.isAssignableFrom(java.io.Closeable::class.java) -> {
                when (this@useSusIT) {
                    is java.io.Closeable? -> useSus(block)
                    else -> useNormal(block)
                }
            }
            it::class.java.isAssignableFrom(AutoCloseable::class.java) -> {
                when (this@useSusIT) {
                    is AutoCloseable? -> useSusFile(block)
                    else -> useNormal(block)
                }
            }
            else -> useNormal(block)
        }
    } ?: useNormal(block)
}

@kotlin.jvm.Throws(android.database.StaleDataException::class)
@com.pt.common.global.CurAnn
@WorkerAnn
suspend inline fun <T : AutoCloseable?, R> T.useSusFile(
    @WorkerAnn crossinline block: suspend T.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    var exception: Throwable? = null
    return justCoroutineCur {
        try {
            return@justCoroutineCur block(this@useSusFile)
        } catch (e: Throwable) {
            exception = e
            throw e
        } finally {
            this@useSusFile.closeFinallyAuto(exception)
        }
    }
}

@kotlin.jvm.Throws(android.database.StaleDataException::class)
@com.pt.common.global.CurAnn
@WorkerAnn
suspend inline fun <T, R> T.useNormal(
    @WorkerAnn crossinline block: suspend T.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return justCoroutineCur {
        var exception: Throwable? = null
        try {
            return@justCoroutineCur block(this@useNormal)
        } catch (e: Throwable) {
            exception = e
            throw e
        } finally {
            runCatching {
                when {
                    KotlinVersion.CURRENT.isAtLeast(
                        1,
                        1,
                        0
                    ) -> (this@useNormal as java.io.Closeable?)?.closeFinally(exception)
                    this@useNormal == null -> {
                    }
                    exception == null -> (this@useNormal as java.io.Closeable?)?.close()
                    else ->
                        try {
                            (this@useNormal as java.io.Closeable?)?.close()
                        } catch (closeException: Throwable) {
                            // cause.addSuppressed(closeException) // ignored here
                        }
                }
            }.getOrElse {
                null
            }
        }
    }
}

@kotlin.jvm.Throws(android.database.StaleDataException::class)
@com.pt.common.global.CurAnn
@WorkerAnn
inline fun <T, R> T.useMyNormal(
    @WorkerAnn crossinline block: T.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    var exception: Throwable? = null
    try {
        return block(this@useMyNormal)
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        runCatching {
            when {
                KotlinVersion.CURRENT.isAtLeast(
                    1,
                    1,
                    0
                ) -> (this@useMyNormal as java.io.Closeable?)?.closeMyFinally(exception)
                this@useMyNormal == null -> {
                }
                exception == null -> (this@useMyNormal as java.io.Closeable?)?.close()
                else ->
                    try {
                        (this@useMyNormal as java.io.Closeable?)?.close()
                    } catch (closeException: Throwable) {
                        // cause.addSuppressed(closeException) // ignored here
                    }
            }
        }.getOrElse {
            null
        }
    }
}

@kotlin.jvm.Throws(android.database.StaleDataException::class)
@PublishedApi
internal fun java.io.Closeable?.closeMyFinally(cause: Throwable?): Unit = run {
    runCatching {
        when {
            this@closeMyFinally == null -> {
            }
            cause == null -> close()
            else ->
                try {
                    close()
                } catch (closeException: Throwable) {
                    cause.addSuppressed(closeException)
                }
        }
    }
}

@kotlin.jvm.Throws(android.database.StaleDataException::class)
@com.pt.common.global.CurAnn
@WorkerAnn
suspend inline fun <T : java.io.Closeable?, R> T.useSus(
    @WorkerAnn crossinline block: suspend T.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return justCoroutineCur {
        var exception: Throwable? = null
        try {
            return@justCoroutineCur block(this@useSus)
        } catch (e: Throwable) {
            exception = e
            throw e
        } finally {
            runCatching {
                when {
                    KotlinVersion.CURRENT.isAtLeast(1, 1, 0) -> closeFinally(exception)
                    this@useSus == null -> {
                    }
                    exception == null -> close()
                    else ->
                        try {
                            close()
                        } catch (closeException: Throwable) {
                            // cause.addSuppressed(closeException) // ignored here
                        }
                }
            }.getOrElse {
                null
            }
        }
    }
}

@kotlin.jvm.Throws(android.database.StaleDataException::class)
@com.pt.common.global.CurAnn
@WorkerAnn
suspend inline fun <R> com.pt.common.moderator.over.MyMediaMetadataRetriever.useSusMeta(
    @WorkerAnn crossinline block: suspend com.pt.common.moderator.over.MyMediaMetadataRetriever.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    var exception: Throwable? = null
    return justCoroutineCur {
        try {
            return@justCoroutineCur block(this@useSusMeta)
        } catch (e: Throwable) {
            exception = e
            throw e
        } finally {
            this@useSusMeta.closeFinallySus(exception)
        }
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
@kotlin.jvm.Throws(android.database.StaleDataException::class)
suspend fun com.pt.common.moderator.over.MyMediaMetadataRetriever?.closeFinallySus(
    cause: Throwable?,
) {
    justCoroutineCur {
        runCatching {
            when {
                this@closeFinallySus == null -> {
                }
                cause == null -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        withBack {
                            release()
                        }
                        withBack {
                            close()
                        }
                    } else {
                        release()
                    }
                }
                else ->
                    try {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                            withBack {
                                release()
                            }
                            withBack {
                                close()
                            }
                        } else {
                            release()
                        }
                    } catch (closeException: Throwable) {
                        cause.addSuppressed(closeException)
                    }
            }
        }
    }
}


@kotlin.jvm.Throws(android.database.StaleDataException::class)
@PublishedApi
internal suspend fun java.io.Closeable?.closeFinally(cause: Throwable?) {
    justCoroutineCur {
        runCatching {
            when {
                this@closeFinally == null -> {
                }
                cause == null -> close()
                else ->
                    try {
                        close()
                    } catch (closeException: Throwable) {
                        cause.addSuppressed(closeException)
                    }
            }
        }
    }
}

@kotlin.jvm.Throws(android.database.StaleDataException::class)
suspend fun AutoCloseable?.closeFinallyAuto(cause: Throwable?) {
    justCoroutineCur {
        runCatching {
            when {
                this@closeFinallyAuto == null -> {
                }
                cause == null -> close()
                else ->
                    try {
                        close()
                    } catch (closeException: Throwable) {
                        cause.addSuppressed(closeException)
                    }
            }
        }
    }
}

suspend inline fun goIf(
    boolean: Boolean, crossinline block: suspend () -> Unit,
) {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    justScope {
        if (boolean) {
            block.invoke()
        } else return@justScope
    }
}

@com.pt.common.global.UiAnn
suspend inline fun <T> T.alsoSus(
    @com.pt.common.global.UiAnn crossinline block: suspend (T) -> Unit,
): T {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    block(this)
    return this
}

@com.pt.common.global.UiAnn
suspend inline fun <T, R> withSus(
    @com.pt.common.global.UiAnn receiver: T, crossinline block: suspend T.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return receiver.block()
}

@com.pt.common.global.UiAnn
suspend inline fun <T, R> T.letSus(
    @com.pt.common.global.UiAnn crossinline block: suspend (T) -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(this)
}

@com.pt.common.global.UiAnn
suspend inline fun <T, R> T.runSus(
    @com.pt.common.global.UiAnn crossinline block: suspend T.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

@com.pt.common.global.UiAnn
suspend inline fun <T> T.applySus(
    @com.pt.common.global.UiAnn crossinline block: suspend T.() -> Unit,
): T {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    block()
    return this
}

@WorkerAnn
suspend inline fun <T> T.alsoSusBack(
    @WorkerAnn crossinline block: suspend (T) -> Unit,
): T {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    block(this)
    return this
}

@WorkerAnn
suspend inline fun <T, R> withSusBack(
    @WorkerAnn receiver: T, crossinline block: suspend T.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return receiver.block()
}

@WorkerAnn
suspend inline fun <T, R> T.letSusBack(
    @WorkerAnn crossinline block: suspend (T) -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(this)
}

@WorkerAnn
suspend inline fun <T, R> T.runSusBack(
    @WorkerAnn crossinline block: suspend T.() -> R,
): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

@WorkerAnn
suspend inline fun <T> T.applySusBack(
    @WorkerAnn crossinline block: suspend T.() -> Unit,
): T {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    block()
    return this
}

@WorkerAnn
inline fun <T> android.content.Context.newIntent(
    cls: Class<T>,
    @WorkerAnn b: android.content.Intent.() -> android.content.Intent,
): android.content.Intent {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(android.content.Intent(this@newIntent, cls))
}

@WorkerAnn
suspend inline fun <T> android.content.Context.newIntentSus(
    cls: Class<T>,
    @WorkerAnn crossinline b: suspend android.content.Intent.() -> android.content.Intent,
): android.content.Intent {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return justCoroutine {
        android.content.Intent(this@newIntentSus, cls)
    }.letSusBack(b)
}