package com.pt.pro.extra.background

import android.view.Gravity
import com.pt.pro.databinding.FloatingHeadWindowsBinding
import com.pt.common.global.framePara
import com.pt.common.mutual.life.GlobalServiceManger

abstract class GlobalServiceHead(ctx: android.content.Context?) : GlobalServiceManger(ctx) {

    private inline val FloatingHeadWindowsBinding.rightCheck: Boolean
        get() = appsFrame.tag != Gravity.END or Gravity.CENTER_VERTICAL

    private inline val FloatingHeadWindowsBinding.leftCheck: Boolean
        get() = appsFrame.tag != Gravity.START or Gravity.CENTER_VERTICAL

    protected fun FloatingHeadWindowsBinding.changeFramesForStart(
        isRight: Boolean,
        screen: Boolean
    ) {
        if (isRight && rightCheck) {
            if (screen) {
                changeFramesForEndScreen()
            } else {
                changeFramesForEndNot()
            }
        } else if (!isRight && leftCheck) {
            if (screen) {
                changeFramesForStartScreen()
            } else {
                changeFramesForStartNot()
            }
        }
    }

    protected fun FloatingHeadWindowsBinding.changeFramesForEnd(screen: Boolean, isRight: Boolean) {
        if (isRight && leftCheck) {
            if (screen) {
                changeFramesForStartScreen()
            } else {
                changeFramesForStartNot()
            }
        } else if (!isRight && rightCheck) {
            if (screen) {
                changeFramesForEndScreen()
            } else {
                changeFramesForEndNot()
            }
        }
    }

    private fun FloatingHeadWindowsBinding.changeFramesForEndNot() {
        appsFrame.tag = Gravity.END or Gravity.CENTER_VERTICAL
        appsFrame.framePara(50F.toPixelS, 50F.toPixelS) {
            gravity = Gravity.END or Gravity.CENTER_VERTICAL
        }
        alarmFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.END or Gravity.TOP
            bottomMargin = 124F.toPixelS
            marginEnd = 2F.toPixelS
        }
        fileFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.END or Gravity.BOTTOM
            topMargin = 124F.toPixelS
            marginEnd = 2F.toPixelS
        }
        dataFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START or Gravity.BOTTOM
            marginEnd = 64F.toPixelS
            bottomMargin = 30F.toPixelS
        }
        galleryFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START
            marginEnd = 64F.toPixelS
            topMargin = 30F.toPixelS
        }
    }

    private fun FloatingHeadWindowsBinding.changeFramesForStartNot() {
        appsFrame.tag = Gravity.START or Gravity.CENTER_VERTICAL
        appsFrame.framePara(50F.toPixelS, 50F.toPixelS) {
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }
        alarmFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START or Gravity.TOP
            bottomMargin = 124F.toPixelS
            marginStart = 2F.toPixelS
        }
        galleryFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START
            marginStart = 64F.toPixelS
            topMargin = 30F.toPixelS
        }
        dataFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START or Gravity.BOTTOM
            marginStart = 64F.toPixelS
            bottomMargin = 30F.toPixelS
        }
        fileFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START or Gravity.BOTTOM
            topMargin = 124F.toPixelS
            marginStart = 2F.toPixelS
        }
    }


    private fun FloatingHeadWindowsBinding.changeFramesForEndScreen() {
        appsFrame.tag = Gravity.END or Gravity.CENTER_VERTICAL
        appsFrame.framePara(50F.toPixelS, 50F.toPixelS) {
            gravity = Gravity.END or Gravity.CENTER_VERTICAL
        }
        alarmFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.END or Gravity.TOP
            bottomMargin = 140F.toPixelS
            marginEnd = 2F.toPixelS
        }
        screenShootFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START
            marginStart = 35F.toPixelS
            topMargin = 10F.toPixelS
        }
        galleryFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START
            topMargin = 45F.toPixelS
            marginEnd = 84F.toPixelS
        }
        dataFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START or Gravity.BOTTOM
            bottomMargin = 45F.toPixelS
            marginEnd = 84F.toPixelS
        }
        screenRecordFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START or Gravity.BOTTOM
            marginStart = 35F.toPixelS
            bottomMargin = 10F.toPixelS
        }
        fileFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.END or Gravity.BOTTOM
            topMargin = 140F.toPixelS
            marginEnd = 2F.toPixelS
        }
    }


    private fun FloatingHeadWindowsBinding.changeFramesForStartScreen() {
        appsFrame.tag = Gravity.START or Gravity.CENTER_VERTICAL
        appsFrame.framePara(50F.toPixelS, 50F.toPixelS) {
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }
        alarmFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START or Gravity.TOP
            bottomMargin = 140F.toPixelS
            marginStart = 2F.toPixelS
        }
        screenShootFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START
            marginStart = 49F.toPixelS
            topMargin = 10F.toPixelS
        }
        galleryFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START
            marginStart = 84F.toPixelS
            topMargin = 45F.toPixelS
        }
        dataFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START or Gravity.BOTTOM
            marginStart = 84F.toPixelS
            bottomMargin = 45F.toPixelS
        }
        screenRecordFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START or Gravity.BOTTOM
            marginStart = 49F.toPixelS
            bottomMargin = 10F.toPixelS
        }
        fileFrame.framePara(35F.toPixelS, 35F.toPixelS) {
            gravity = Gravity.START or Gravity.BOTTOM
            topMargin = 140F.toPixelS
            marginStart = 2F.toPixelS
        }
    }

}