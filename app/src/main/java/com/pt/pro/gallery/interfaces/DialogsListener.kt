package com.pt.pro.gallery.interfaces

interface DialogsListener<T> {
    var folderMedia: MutableList<com.pt.common.global.MediaFolderSack>?
    var folderPath: MutableList<com.pt.common.global.MediaSack>?
    var listener: T?
}