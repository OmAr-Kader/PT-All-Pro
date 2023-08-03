package com.pt.pro.notepad.models

data class CounterUpdate(
    var target: CounterItem,
    var statCreate: Boolean,
    var statUpdate: Boolean
)