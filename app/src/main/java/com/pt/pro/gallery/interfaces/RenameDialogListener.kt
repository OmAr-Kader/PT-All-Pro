package com.pt.pro.gallery.interfaces

interface RenameDialogListener {
    var fileHolder: com.pt.common.global.FileSack?
    var mediaHold: com.pt.common.global.MediaSack?
    var oneListener: com.pt.pro.gallery.dialogs.RenameDialog.DialogListenerOne?
    var twoListener: com.pt.pro.gallery.dialogs.RenameDialog.DialogListenerTwo?
}