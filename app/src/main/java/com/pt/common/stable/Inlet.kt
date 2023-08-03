package com.pt.common.stable

import com.pt.common.global.compactImageReturn
import com.pt.pro.R

internal inline val Int.checkFileImage: Int
    @androidx.annotation.DrawableRes get() {
        return when (this) {
            FOLDER -> R.drawable.ic_file_folder
            PDF -> R.drawable.ic_file_pdf
            PPT -> R.drawable.ic_file_ppt
            PPTX -> R.drawable.ic_file_ppt
            DOC -> R.drawable.ic_file_word
            DOCX -> R.drawable.ic_file_word
            XLS -> R.drawable.ic_file_xls
            XLSX -> R.drawable.ic_file_xls
            HTML -> R.drawable.ic_file_html
            TXT -> R.drawable.ic_file_txt
            RAR -> R.drawable.ic_file_rar
            ZIP -> R.drawable.ic_file_zip
            APK -> R.drawable.ic_file_apk
            else -> R.drawable.ic_file_unknown
        }
    }

internal suspend fun android.content.Context?.fetchAllSvg(
): androidx.collection.ArrayMap<Int, android.graphics.drawable.Drawable> = justCoroutine {
    return@justCoroutine withBackDef(androidx.collection.arrayMapOf()) {
        if (this@fetchAllSvg == null) return@withBackDef androidx.collection.arrayMapOf()
        return@withBackDef androidx.collection.arrayMapOf<Int, android.graphics.drawable.Drawable>().applySusBack {
            put(FOLDER, compactImageReturn(R.drawable.ic_file_folder))
            put(PDF, compactImageReturn(R.drawable.ic_file_pdf))
            put(PPT, compactImageReturn(R.drawable.ic_file_ppt))
            put(PPTX, compactImageReturn(R.drawable.ic_file_ppt))
            put(DOC, compactImageReturn(R.drawable.ic_file_word))
            put(DOCX, compactImageReturn(R.drawable.ic_file_word))
            put(XLS, compactImageReturn(R.drawable.ic_file_xls))
            put(XLSX, compactImageReturn(R.drawable.ic_file_xls))
            put(HTML, compactImageReturn(R.drawable.ic_file_html))
            put(TXT, compactImageReturn(R.drawable.ic_file_txt))
            put(RAR, compactImageReturn(R.drawable.ic_file_rar))
            put(ZIP, compactImageReturn(R.drawable.ic_file_zip))
            put(APK, compactImageReturn(R.drawable.ic_file_apk))
            put(UNKNOWN, compactImageReturn(R.drawable.ic_file_unknown))
        }
    }
}

internal val android.content.res.Resources.findTextCato: (Int) -> String
    get() = {
        when (it) {
            CATO_MUSIC -> getString(R.string.mk)
            CATO_IMAGE -> getString(R.string.ci)
            CATO_VIDEO -> getString(R.string.vc)
            CATO_AUDIO -> getString(R.string.af)
            CATO_DOCUMENT -> getString(R.string.um)
            CATO_SD -> getString(R.string.zq)
            else -> getString(R.string.ec)
        }
    }

@androidx.annotation.UiThread
internal inline fun newSetEditAlarmFragment(
    @androidx.annotation.UiThread b: com.pt.pro.alarm.views.SetEditAlarmFragment.() -> com.pt.pro.alarm.views.SetEditAlarmFragment,
): com.pt.pro.alarm.views.SetEditAlarmFragment {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.alarm.views.SetEditAlarmFragment())
}

@androidx.annotation.UiThread
internal inline fun newEditAlarmFragment(
    @androidx.annotation.UiThread b: com.pt.pro.alarm.views.EditAlarmFragment.() -> com.pt.pro.alarm.views.EditAlarmFragment,
): com.pt.pro.alarm.views.EditAlarmFragment {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.alarm.views.EditAlarmFragment())
}

@androidx.annotation.UiThread
internal inline fun newFragmentShake(
    @androidx.annotation.UiThread b: com.pt.pro.alarm.decline.FragmentShake.() -> com.pt.pro.alarm.decline.FragmentShake,
): com.pt.pro.alarm.decline.FragmentShake {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.alarm.decline.FragmentShake())
}

@androidx.annotation.UiThread
internal inline fun newFragmentDoubleSwipe(
    @androidx.annotation.UiThread b: com.pt.pro.alarm.decline.FragmentDoubleSwipe.() -> com.pt.pro.alarm.decline.FragmentDoubleSwipe,
): com.pt.pro.alarm.decline.FragmentDoubleSwipe {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.alarm.decline.FragmentDoubleSwipe())
}

@androidx.annotation.UiThread
internal inline fun newFragmentSingleClick(
    @androidx.annotation.UiThread b: com.pt.pro.alarm.decline.FragmentSingleClick.() -> com.pt.pro.alarm.decline.FragmentSingleClick,
): com.pt.pro.alarm.decline.FragmentSingleClick {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.alarm.decline.FragmentSingleClick())
}

@androidx.annotation.UiThread
internal inline fun newFragmentSingleSwipe(
    @androidx.annotation.UiThread b: com.pt.pro.alarm.decline.FragmentSingleSwipe.() -> com.pt.pro.alarm.decline.FragmentSingleSwipe,
): com.pt.pro.alarm.decline.FragmentSingleSwipe {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.alarm.decline.FragmentSingleSwipe())
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun <E> android.content.Context.newDBAlarm(
    @com.pt.common.global.WorkerAnn crossinline block: suspend com.pt.pro.alarm.objects.DBAlarm.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.alarm.objects.DBAlarm(this).useBack {
        block(this@useBack)
    }
}

internal inline fun <E> android.content.Context.newDBAlarmNor(
    crossinline block: com.pt.pro.alarm.objects.DBAlarm.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.alarm.objects.DBAlarm(this).useMy {
        block(this)
    }
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun <E> android.content.Context.newDBFile(
    @com.pt.common.global.WorkerAnn crossinline block: suspend com.pt.pro.file.objects.DBFile.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.file.objects.DBFile(this).useBack {
        block(this@useBack)
    }
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun <T> android.content.Context.newDBPlaylist(
    @com.pt.common.global.WorkerAnn crossinline block: suspend com.pt.pro.musical.back.DBPlaylist.() -> T,
): T {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.musical.back.DBPlaylist(this).useBack {
        block(this@useBack)
    }
}

internal inline fun <E> android.content.Context.newDBGallery(
    crossinline block: com.pt.pro.gallery.objects.DBGallery.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.gallery.objects.DBGallery(this).useMy {
        block(this@useMy)
    }
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun <E> android.content.Context.newDBGallerySus(
    @com.pt.common.global.WorkerAnn crossinline block: suspend com.pt.pro.gallery.objects.DBGallery.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.gallery.objects.DBGallery(this).useBack {
        block(this@useBack)
    }
}

internal suspend inline fun <E> android.content.Context.newDBDataSus(
    b: String,
    @com.pt.common.global.WorkerAnn crossinline block: suspend com.pt.pro.notepad.data.DBData.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.notepad.data.DBData(this, b).useBack {
        block(this@useBack)
    }
}

internal inline fun <E> android.content.Context.newDBData(
    b: String,
    crossinline block: com.pt.pro.notepad.data.DBData.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.notepad.data.DBData(this, b).useMy {
        block(this@useMy)
    }
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun <E> android.content.Context.newDBDataUser(
    @com.pt.common.global.WorkerAnn crossinline block: suspend com.pt.pro.notepad.data.DBDataUser.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.notepad.data.DBDataUser(this).useBack {
        block(this@useBack)
    }
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun <E> android.content.Context.newDBCounter(
    b: String,
    @com.pt.common.global.WorkerAnn crossinline block: suspend com.pt.pro.notepad.data.DBCounter.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.notepad.data.DBCounter(this, b).useBack {
        block(this@useBack)
    }
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun <E> android.content.Context.newDBDataUserCounter(
    @com.pt.common.global.WorkerAnn crossinline block: suspend com.pt.pro.notepad.data.DBDataUserCounter.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.notepad.data.DBDataUserCounter(this).useBack {
        block(this@useBack)
    }
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun <E> android.content.Context.newDBTablesCounter(
    s: String,
    @com.pt.common.global.WorkerAnn crossinline block: suspend com.pt.pro.notepad.data.DBTablesCounter.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.notepad.data.DBTablesCounter(this, s).useBack {
        block(this@useBack)
    }
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun <E> android.content.Context.newDBTables(
    s: String,
    @com.pt.common.global.WorkerAnn crossinline block: suspend com.pt.pro.notepad.data.DBTables.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.notepad.data.DBTables(this, s).useBack {
        block(this@useBack)
    }
}

internal inline fun <E> android.content.Context.newDBNotification(
    crossinline block: com.pt.pro.notepad.data.DBNotification.() -> E,
): E {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return com.pt.pro.notepad.data.DBNotification(this).useMy {
        block(this@useMy)
    }
}

@androidx.annotation.UiThread
internal suspend inline fun newFragmentFile(
    @androidx.annotation.UiThread crossinline b: suspend com.pt.pro.file.views.FragmentFile.() -> com.pt.pro.file.views.FragmentFile,
): com.pt.pro.file.views.FragmentFile {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.file.views.FragmentFile())
}

@androidx.annotation.UiThread
internal inline fun newChooseMusicOption(
    @androidx.annotation.UiThread b: com.pt.pro.file.dialogs.ChooseMusicOption.() -> com.pt.pro.file.dialogs.ChooseMusicOption,
): com.pt.pro.file.dialogs.ChooseMusicOption {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.file.dialogs.ChooseMusicOption())
}

@androidx.annotation.UiThread
internal inline fun newPopUpForMusic(
    @androidx.annotation.UiThread b: com.pt.pro.file.dialogs.PopUpForMusic.() -> com.pt.pro.file.dialogs.PopUpForMusic,
): com.pt.pro.file.dialogs.PopUpForMusic {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.file.dialogs.PopUpForMusic())
}

@androidx.annotation.UiThread
internal suspend inline fun newFragmentGallery(
    @androidx.annotation.UiThread crossinline b: suspend com.pt.pro.gallery.activities.FragmentGallery.() -> com.pt.pro.gallery.activities.FragmentGallery,
): com.pt.pro.gallery.activities.FragmentGallery {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.activities.FragmentGallery())
}

@androidx.annotation.UiThread
internal suspend inline fun newFragmentDisPlay(
    @androidx.annotation.UiThread crossinline b: suspend com.pt.pro.gallery.activities.FragmentDisPlay.() -> com.pt.pro.gallery.activities.FragmentDisPlay,
): com.pt.pro.gallery.activities.FragmentDisPlay {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.activities.FragmentDisPlay())
}

@androidx.annotation.UiThread
internal suspend inline fun newFragmentAllGallery(
    @androidx.annotation.UiThread crossinline b: suspend com.pt.pro.gallery.activities.FragmentAllGallery.() -> com.pt.pro.gallery.activities.FragmentAllGallery,
): com.pt.pro.gallery.activities.FragmentAllGallery {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.activities.FragmentAllGallery())
}

@androidx.annotation.UiThread
internal suspend inline fun newFragmentTimeGallery(
    @androidx.annotation.UiThread crossinline b: suspend com.pt.pro.gallery.activities.FragmentTimeGallery.() -> com.pt.pro.gallery.activities.FragmentTimeGallery,
): com.pt.pro.gallery.activities.FragmentTimeGallery {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.activities.FragmentTimeGallery())
}

@androidx.annotation.UiThread
internal suspend inline fun newFragmentAlbumGallery(
    @androidx.annotation.UiThread crossinline b: suspend com.pt.pro.gallery.activities.FragmentAlbumGallery.() -> com.pt.pro.gallery.activities.FragmentAlbumGallery,
): com.pt.pro.gallery.activities.FragmentAlbumGallery {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.activities.FragmentAlbumGallery())
}

@androidx.annotation.UiThread
internal suspend inline fun newFragmentSearch(
    @androidx.annotation.UiThread crossinline b: suspend com.pt.pro.gallery.activities.FragmentSearch.() -> com.pt.pro.gallery.activities.FragmentSearch,
): com.pt.pro.gallery.activities.FragmentSearch {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.activities.FragmentSearch())
}

@androidx.annotation.UiThread
internal inline fun newFragmentImage(
    @androidx.annotation.UiThread b: com.pt.pro.gallery.fragments.FragmentImage.() -> com.pt.pro.gallery.fragments.FragmentImage,
): com.pt.pro.gallery.fragments.FragmentImage {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.fragments.FragmentImage())
}

@androidx.annotation.UiThread
internal inline fun newFragmentVideo(
    @androidx.annotation.UiThread b: com.pt.pro.gallery.fragments.FragmentVideo.() -> com.pt.pro.gallery.fragments.FragmentVideo,
): com.pt.pro.gallery.fragments.FragmentVideo {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.fragments.FragmentVideo())
}

@androidx.annotation.UiThread
internal suspend inline fun newBrowserFragment(
    @androidx.annotation.UiThread crossinline b: suspend com.pt.pro.gallery.fragments.BrowserFragment.() -> com.pt.pro.gallery.fragments.BrowserFragment,
): com.pt.pro.gallery.fragments.BrowserFragment {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.fragments.BrowserFragment())
}

@androidx.annotation.UiThread
internal inline fun newPopForClipboard(
    @androidx.annotation.UiThread b: com.pt.pro.gallery.dialogs.PopForClipboard.() -> com.pt.pro.gallery.dialogs.PopForClipboard,
): com.pt.pro.gallery.dialogs.PopForClipboard {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.dialogs.PopForClipboard())
}

@androidx.annotation.UiThread
internal inline fun newPopForDelete(
    @androidx.annotation.UiThread b: com.pt.pro.gallery.dialogs.PopForDelete.() -> com.pt.pro.gallery.dialogs.PopForDelete,
): com.pt.pro.gallery.dialogs.PopForDelete {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.dialogs.PopForDelete())
}

@androidx.annotation.UiThread
internal inline fun newRenameDialog(
    @androidx.annotation.UiThread b: com.pt.pro.gallery.dialogs.RenameDialog.() -> com.pt.pro.gallery.dialogs.RenameDialog,
): com.pt.pro.gallery.dialogs.RenameDialog {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.dialogs.RenameDialog())
}

@androidx.annotation.UiThread
internal inline fun newPopForHide(
    @androidx.annotation.UiThread b: com.pt.pro.gallery.dialogs.PopForHide.() -> com.pt.pro.gallery.dialogs.PopForHide,
): com.pt.pro.gallery.dialogs.PopForHide {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.dialogs.PopForHide())
}

@androidx.annotation.UiThread
internal inline fun newPopForProperties(
    @androidx.annotation.UiThread b: com.pt.pro.gallery.dialogs.PopForProperties.() -> com.pt.pro.gallery.dialogs.PopForProperties,
): com.pt.pro.gallery.dialogs.PopForProperties {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.gallery.dialogs.PopForProperties())
}

@androidx.annotation.UiThread
internal inline fun newViewPdfFragment(
    @androidx.annotation.UiThread b: com.pt.pro.file.views.pdf.ViewPdfFragment.() -> com.pt.pro.file.views.pdf.ViewPdfFragment,
): com.pt.pro.file.views.pdf.ViewPdfFragment {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.file.views.pdf.ViewPdfFragment())
}

@androidx.annotation.UiThread
internal inline fun newBrowserFileFragment(
    @androidx.annotation.UiThread b: com.pt.pro.file.views.pdf.BrowserFileFragment.() -> com.pt.pro.file.views.pdf.BrowserFileFragment,
): com.pt.pro.file.views.pdf.BrowserFileFragment {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.file.views.pdf.BrowserFileFragment())
}

@androidx.annotation.UiThread
internal inline fun newFragmentTextViewer(
    b: com.pt.pro.file.views.pdf.FragmentTextViewer.() -> com.pt.pro.file.views.pdf.FragmentTextViewer,
): com.pt.pro.file.views.pdf.FragmentTextViewer {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.file.views.pdf.FragmentTextViewer())
}

@androidx.annotation.UiThread
internal inline fun newPopForCreate(
    @androidx.annotation.UiThread b: com.pt.pro.file.dialogs.PopForCreate.() -> com.pt.pro.file.dialogs.PopForCreate,
): com.pt.pro.file.dialogs.PopForCreate {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.file.dialogs.PopForCreate())
}

@androidx.annotation.UiThread
internal inline fun newPopForHideFile(
    @androidx.annotation.UiThread b: com.pt.pro.file.dialogs.PopForHideFile.() -> com.pt.pro.file.dialogs.PopForHideFile,
): com.pt.pro.file.dialogs.PopForHideFile {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.file.dialogs.PopForHideFile())
}

@androidx.annotation.UiThread
internal inline fun newPopForPropertyFile(
    @androidx.annotation.UiThread b: com.pt.pro.file.dialogs.PopForPropertyFile.() -> com.pt.pro.file.dialogs.PopForPropertyFile,
): com.pt.pro.file.dialogs.PopForPropertyFile {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.file.dialogs.PopForPropertyFile())
}

@androidx.annotation.UiThread
internal inline fun newPopForVirtual(
    @androidx.annotation.UiThread b: com.pt.pro.file.dialogs.PopForVirtual.() -> com.pt.pro.file.dialogs.PopForVirtual,
): com.pt.pro.file.dialogs.PopForVirtual {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.file.dialogs.PopForVirtual())
}

@androidx.annotation.UiThread
internal suspend inline fun newFragmentNoteSus(
    @androidx.annotation.UiThread crossinline b: suspend com.pt.pro.notepad.fragments.FragmentNote.() -> com.pt.pro.notepad.fragments.FragmentNote,
): com.pt.pro.notepad.fragments.FragmentNote {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.notepad.fragments.FragmentNote())
}

@androidx.annotation.UiThread
internal suspend inline fun newFragmentCounterSus(
    @androidx.annotation.UiThread crossinline b: suspend com.pt.pro.notepad.fragments.FragmentCounter.() -> com.pt.pro.notepad.fragments.FragmentCounter,
): com.pt.pro.notepad.fragments.FragmentCounter {
    kotlin.contracts.contract {
        callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return b(com.pt.pro.notepad.fragments.FragmentCounter())
}


suspend inline fun android.content.Context.scannerLauncherGall(
    str: String, crossinline intent: suspend android.content.Intent.() -> Unit
) {
    withBack {
        android.content.Intent(
            this@scannerLauncherGall, com.pt.pro.notepad.activities.ScannerActivity::class.java
        ).apply {
            putExtra(TABLE_NAME, "")
            putExtra(SCANNER_IS_GALLERY, true)
            putExtra(SCANNER_GALLERY_PATH, str)
        }.alsoSusBack(intent)
    }
}