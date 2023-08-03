package com.pt.common.global

typealias InvokingMutable = MutableList<DSackV<com.pt.common.mutual.base.Invoker?, Int>>?
typealias InvokingBackMutable = MutableList<com.pt.common.mutual.base.InvokerBag>?

typealias DSack<T, R, I> = com.pt.common.moderator.cross.GlobalData<T, R, I>
typealias DSackT<T, R> = com.pt.common.moderator.cross.GlobalDataTwo<T, R>
typealias DSackV<T, R> = com.pt.common.moderator.cross.GlobalVarDataTwo<T, R>

typealias AlarmSack = com.pt.common.objects.Alarm

typealias MusicSack = com.pt.common.objects.MusicHolder

typealias FileSack = com.pt.common.objects.FileHolder
typealias FileUpdateSack = com.pt.common.objects.FileUpdate

typealias MediaSack = com.pt.common.objects.MediaHolder
typealias MediaFolderSack = com.pt.common.objects.FolderMedia

typealias FileLate = java.io.File
typealias FInStream = java.io.FileInputStream
typealias CalendarLate = java.util.Calendar

typealias MainAnn = androidx.annotation.MainThread
typealias UiAnn = androidx.annotation.UiThread
typealias WorkerAnn = androidx.annotation.WorkerThread

typealias ViewAnn = androidx.annotation.ContentView
typealias CurAnn = androidx.annotation.BinderThread
typealias APIAnn = androidx.annotation.RequiresApi

typealias Bring = androidx.activity.result.ActivityResultLauncher<android.content.Intent>?
typealias BringPer = androidx.activity.result.ActivityResultLauncher<Array<String>>?
