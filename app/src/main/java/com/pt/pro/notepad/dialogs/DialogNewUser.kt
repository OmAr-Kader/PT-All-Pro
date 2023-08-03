package com.pt.pro.notepad.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.toDrawable
import com.pt.common.global.fetchMyEditText
import com.pt.common.global.findAttr
import com.pt.common.BuildConfig.JUST_NAME
import com.pt.pro.R

class DialogNewUser @JvmOverloads constructor(
    private var createListener: CreateNewListener? = null, private val firstTime: Boolean,
    private val isNight: Boolean,
) : AppCompatDialogFragment() {

    private var input: AppCompatEditText? = null
    private var haveClicked = false

    companion object {

        @JvmStatic
        fun newInstance(
            cre: CreateNewListener?,
            firstTime: Boolean,
            isNight: Boolean
        ): DialogNewUser = DialogNewUser(cre, firstTime, isNight)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        requireDialog().window?.apply {
            requireContext().theme.findAttr(R.attr.rmoBackHint).toDrawable().let {
                setBackgroundDrawable(it)
            }
            attributes.windowAnimations = R.style.MyAnimation_Window
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (isNight) {
            AlertDialog.Builder(requireContext(), R.style.DeleteAlarmDialogTheme_Night)
        } else {
            AlertDialog.Builder(requireContext(), R.style.DeleteAlarmDialogTheme_Light)
        }.apply {
            setTitle(resources.getString(R.string.un))
            setMessage(resources.getString(R.string.yn))
            requireContext().fetchMyEditText {
                input = this
                requireContext().theme.findAttr(R.attr.rmoText).let { col ->
                    setTextColor(col)
                    BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                        col,
                        BlendModeCompat.SRC_IN
                    ).let {
                        background.mutate().colorFilter = it
                    }
                }
                setView(this)
                setPositiveButton(resources.getString(R.string.er)) { _, _ ->
                    haveClicked = true
                    if (text.toString() == "") {
                        hint = resources.getString(R.string.yn)
                    } else {
                        createListener?.createUserNew(
                            text.toString(),
                            (text.toString() + System.currentTimeMillis())
                        )
                    }
                }
            }
            setNegativeButton(resources.getString(R.string.co)) { _, _ ->

            }
        }.run {
            create()
        }
    }

    override fun onDismiss(dialog: android.content.DialogInterface) {
        if (!haveClicked && firstTime) {
            createListener?.apply {
                createUserNew(JUST_NAME, JUST_NAME + System.currentTimeMillis())
            }
        }
        super.onDismiss(dialog)
    }

    @FunctionalInterface
    fun interface CreateNewListener {
        fun createUserNew(realName: String, dataName: String)
    }

}