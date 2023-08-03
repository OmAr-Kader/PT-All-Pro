package com.pt.pro.notepad.interfaces

interface DaysListener {
    val counterDaysNumbers: MutableList<String>
    var tableTimeCounter: Long
    val allStringResource: String
    var dataClickListener: CounterListener?
    var clickedDay: Int
    fun onAdapterDestroy()
    suspend fun MutableList<String>.update(color: Int)
    fun changeColor(counterPos: Int, isAll: Boolean)
    fun resetFromOut()
}