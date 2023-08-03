package com.pt.common.mutual.base

interface BackInterface {

    val onBackCheck: Boolean get() = false

    fun onBackL(onBackCheck: (Boolean).() -> Unit) {}
    fun onBack() {}

}