package com.pt.pro.notepad.interfaces

interface CounterListener {
    fun newUserLauncher()
    fun onCounterDays(CounterDaysNumber: String?, schedule: Boolean)
}