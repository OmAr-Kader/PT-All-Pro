package com.pt.common.mutual.adapter

interface HolderListener {
    fun bind()
    fun attach()
    fun attachFilter() {}
    fun clear()
    fun finalClear() {}

    suspend fun attachSus() {}
    suspend fun clearSus() {}
}