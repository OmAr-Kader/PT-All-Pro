package com.pt.common.global

@get:androidx.annotation.ChecksSdkIntAtLeast(api = android.os.Build.VERSION_CODES.S)
inline val isV_S: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S

@get:androidx.annotation.ChecksSdkIntAtLeast(api = android.os.Build.VERSION_CODES.R)
inline val isV_R: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R

@get:androidx.annotation.ChecksSdkIntAtLeast(api = android.os.Build.VERSION_CODES.P)
inline val isV_P: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P

@get:androidx.annotation.ChecksSdkIntAtLeast(api = android.os.Build.VERSION_CODES.Q)
inline val isV_Q: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

@get:androidx.annotation.ChecksSdkIntAtLeast(api = android.os.Build.VERSION_CODES.O)
inline val isV_O: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O

@get:androidx.annotation.ChecksSdkIntAtLeast(api = android.os.Build.VERSION_CODES.O_MR1)
inline val isV_O_M: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1

@get:androidx.annotation.ChecksSdkIntAtLeast(api = android.os.Build.VERSION_CODES.N)
inline val isV_N: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N

@get:androidx.annotation.ChecksSdkIntAtLeast(api = android.os.Build.VERSION_CODES.TIRAMISU)
inline val isV_T: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU

@get:androidx.annotation.ChecksSdkIntAtLeast(api = android.os.Build.VERSION_CODES.M)
inline val isV_M: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M

inline val isV_NOT_M: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M

inline val android.content.Context.hasNotificationPermission: Boolean
    @com.pt.common.global.WorkerAnn
    get() {
        return !isV_T || androidx.core.content.ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

inline val android.content.Context.hasExternalReadWritePermission: Boolean
    get() {
        return if (isV_R)
            android.os.Environment.isExternalStorageManager()
        else
            hasExternalPermissionRestOfVersions
    }

inline val android.content.Context.hasExternalPermissionRestOfVersions: Boolean
    @com.pt.common.global.WorkerAnn
    get() {
        return isV_NOT_M ||
                (androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED &&
                        androidx.core.content.ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED)
    }

inline val android.content.Context.hasVoicePermission: Boolean
    @com.pt.common.global.WorkerAnn
    get() {
        return isV_NOT_M ||
                androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.RECORD_AUDIO
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

inline val android.content.Context.isNotificationPolicyAccessGranted: Boolean
    @com.pt.common.global.APIAnn(android.os.Build.VERSION_CODES.M)
    @com.pt.common.global.WorkerAnn
    get() {
        return com.pt.common.stable.catchy(true) {
            fetchSystemService(notificationService)?.isNotificationPolicyAccessGranted == true
        }
    }

inline val android.content.Context.canFloat: Boolean
    @com.pt.common.global.WorkerAnn
    get() {
        return (isV_NOT_M) ||
                (isV_M &&
                        android.provider.Settings.canDrawOverlays(this))
    }

inline val android.content.Context.canFloatForQ: Boolean
    @com.pt.common.global.WorkerAnn
    get() {
        return (!isV_Q) ||
                (isV_Q &&
                        android.provider.Settings.canDrawOverlays(this))
    }

inline val storageSecondWayHigh: (pac: String) -> android.content.Intent
    @com.pt.common.global.WorkerAnn
    get() = { pac ->
        android.content.Intent().apply {
            action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            addCategory(android.content.Intent.CATEGORY_OPENABLE)
            data = (com.pt.common.BuildConfig.PACK_AGE + pac).toUri
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(android.content.Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
    }


inline val android.content.Context.isCanWriteSetting: Boolean
    @com.pt.common.global.WorkerAnn
    get() {
        return if (isV_M) {
            android.provider.Settings.System.canWrite(this)
        } else {
            (androidx.core.content.ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_SETTINGS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED)
        }
    }
