package com.pt.common.mutual.base

data class InvokerBag(
    val invoker: Invoker,
    @Volatile
    var cancellable: java.util.concurrent.ScheduledFuture<Unit>?,
    val idInvoker: Int
)