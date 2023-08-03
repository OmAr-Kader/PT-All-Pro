package com.pt.pro.notepad.dialogs

import android.text.Editable
import androidx.appcompat.widget.AppCompatEditText
import com.pt.common.global.fetchCalenderDay
import com.pt.common.global.makeToastRec
import com.pt.common.mutual.life.GlobalDia
import com.pt.common.stable.withMain
import com.pt.pro.R
import com.pt.pro.databinding.PopUpForEmailBinding
import com.pt.pro.notepad.interfaces.DataClickListener
import com.pt.pro.notepad.models.DataKeeperItem
import com.pt.pro.notepad.objects.KEEP_EMAIL

class DialogEmail @JvmOverloads constructor(
    private var dataClickListener: DataClickListener? = null,
    private var txt: String = "",
) : GlobalDia<PopUpForEmailBinding>() {

    companion object {

        fun newInstance(
            txt: String, listener: DataClickListener,
        ): DialogEmail = DialogEmail(listener, txt)
    }

    override val android.view.LayoutInflater.creBinD: android.view.View
        get() = PopUpForEmailBinding.inflate(actD.layoutInflater).also {
            @com.pt.common.global.ViewAnn
            binder = it
        }.root


    @com.pt.common.global.UiAnn
    override suspend fun PopUpForEmailBinding.intiViews() {
        withMain {
            textLong.setEditableText(txt)
            okPro.setOnClickListener {
                if (!textLong.text.isNullOrEmpty() && !toEmail.text.isNullOrEmpty()) {
                    val t = System.currentTimeMillis()
                    DataKeeperItem(
                        dataText = textLong.text.toString(),
                        keeperType = KEEP_EMAIL,
                        emailToSome = toEmail.text.toString(),
                        emailSubject = subject.text.toString(),
                        timeData = t,
                        recordPath = null,
                        recordLength = 0L,
                        dayNum = t.fetchCalenderDay,
                        imageUrl = null,
                        isDone = false,
                    ).run {
                        dataClickListener?.run { saveEmail() }
                    }
                    dia.dismiss()
                } else {
                    ctxD.makeToastRec(R.string.ql, 0)
                }
            }
            noPro.setOnClickListener {
                dia.dismiss()
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun AppCompatEditText.setEditableText(text: String) {
        this.text = Editable.Factory.getInstance().newEditable(text)
    }

}