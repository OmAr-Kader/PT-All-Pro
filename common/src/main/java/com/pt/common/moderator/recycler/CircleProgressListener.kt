package com.pt.common.moderator.recycler

interface CircleProgressListener {
    fun getStrokeWidth(): Float
    fun setStrokeWidth(strokeWidth: Float)
    fun getProgress(): Float
    fun setProgress(progress: Float)
    fun getMin(): Int
    fun setMin(min: Int)
    fun getMax(): Int
    fun setMax(max: Int)
    fun getColor(): Int
    fun setColor(color: Int)
    fun lightenColor(color: Int, factor: Float): Int
    fun setProgressWithAnimation(progress: Float)
}