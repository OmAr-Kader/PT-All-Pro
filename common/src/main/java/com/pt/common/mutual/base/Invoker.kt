package com.pt.common.mutual.base

data class Invoker(@Volatile var inv: (() -> Unit)?) : () -> Unit {

    override fun invoke() {
        inv?.invoke()
        inv = null
    }
}
