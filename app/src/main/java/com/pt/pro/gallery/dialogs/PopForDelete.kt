package com.pt.pro.gallery.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.pt.common.global.MediaFolderSack
import com.pt.common.global.MediaSack
import com.pt.common.global.findAttr
import com.pt.pro.R
import com.pt.pro.gallery.interfaces.DialogsListener

class PopForDelete : DialogFragment(), DialogsListener<PopForDelete.DeleteListener> {

    override var folderPath: MutableList<MediaSack>? = mutableListOf()
    override var listener: DeleteListener? = null
    var isNight: Boolean = false

    override var folderMedia: MutableList<MediaFolderSack>? = null

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

    @com.pt.common.global.MainAnn
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (isNight) {
            AlertDialog.Builder(requireContext(), R.style.DeleteAlarmDialogTheme_Night)
        } else {
            AlertDialog.Builder(requireContext(), R.style.DeleteAlarmDialogTheme_Light)
        }.apply {
            setTitle(R.string.dt)
            setMessage(R.string.dc)
            setPositiveButton(R.string.ys) { _, _ ->
                listener?.run { folderPath?.toMutableList()?.onDelete() }
            }
            setNegativeButton(R.string.no, null)

        }.run {
            create()
        }
    }

    @FunctionalInterface
    fun interface DeleteListener {
        fun MutableList<MediaSack>.onDelete()
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        folderPath = null
        listener = null
        super.onDestroyView()
    }

}