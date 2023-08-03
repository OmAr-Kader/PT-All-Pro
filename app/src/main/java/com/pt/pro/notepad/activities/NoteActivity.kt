package com.pt.pro.notepad.activities

import android.content.res.Configuration
import android.os.Bundle
import com.pt.common.global.*
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.databinding.ActivityMainRecorderBinding
import com.pt.pro.notepad.dialogs.DialogNewUser
import com.pt.pro.notepad.fragments.FragmentNote
import com.pt.pro.notepad.models.TablesModelUser
import com.pt.pro.notepad.objects.countTab
import com.pt.pro.notepad.objects.noteTab

class NoteActivity : com.pt.common.mutual.life.GlobalBaseActivity() {

    private inline val binding: ActivityMainRecorderBinding get() = binder!!

    private var binder: ActivityMainRecorderBinding? = null

    private inline val colorS: Int
        get() = theme.findAttr(android.R.attr.colorPrimary)

    private inline val colorTxt: Int
        get() = theme.findAttr(R.attr.rmoText)

    private var browser: FragmentNote? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            launchImdMain {
                intiViews()
            }
        } else {
            recreateAct()
        }
    }

    private suspend fun intiViews() {

        justCoroutine {
            ActivityMainRecorderBinding.inflate(layoutInflater).alsoSus {
                @ViewAnn
                binder = it
                it.changeLayout(actionBarHeight, resources.statusBarHeight)
                if (isV_R) {
                    it.fakeStatus.framePara(-1, resources.statusBarHeight) {}
                }
            }.root.applySus(::setContentView)
        }
        withMain {
            binding.dataTab.setOnClickListener {
                binding.apply {
                    dataFragment.justVisible()
                    converterFragment.justGone()
                    dataTab.setTextColor(colorS)
                    dataLinear.setBackgroundColor(colorS)

                    counterTab.setTextColor(colorTxt)
                    counterLinear.setBackgroundColor(0)
                }
            }
            binding.counterTab.setOnClickListener {
                binding.apply {
                    dataFragment.justGone()
                    converterFragment.justVisible()

                    dataTab.setTextColor(colorTxt)
                    dataLinear.setBackgroundColor(0)

                    counterTab.setTextColor(colorS)
                    counterLinear.setBackgroundColor(colorS)
                }
                browser?.checkIfHaveFra()
            }
        }
        justScope {
            if (!findBooleanPrefDb(ENT_USER, USER_ENT, false)) {
                withMainNormal {
                    if (supportFragmentManager.findFragmentByTag(com.pt.pro.notepad.objects.DIALOG_USER) == null) {
                        DialogNewUser.newInstance(newUserListener, true, nightRider).also {
                            it.show(supportFragmentManager, com.pt.pro.notepad.objects.DIALOG_USER)
                        }
                    }
                }
            } else {
                loadFragments()
            }
        }
        withDefault {
            intent?.getBooleanExtra(SHORTCUT, false)?.alsoSusBack { i ->
                if (i && (!hasExternalReadWritePermission || !hasVoicePermission)) toFinish()
            }
        }
    }

    private fun loadFragments() {
        launchImdMain {
            justCoroutine {
                newFragmentNoteSus {
                    this@newFragmentNoteSus
                }.alsoSus {
                    browser = it
                    supportFragmentManager.fragmentLauncher(FRAGMENT_NOTE) {
                        add(binding.dataFragment.id, it, FRAGMENT_NOTE)
                    }
                }
            }
            justCoroutine {
                newFragmentCounterSus {
                    this@newFragmentCounterSus
                }.alsoSus {
                    supportFragmentManager.fragmentLauncher(FRAGMENT_COUNTER) {
                        add(binding.converterFragment.id, it, FRAGMENT_COUNTER)
                    }
                }
            }
            afterLoadFragments()
            updatePrefInt(ID_APP, DATA_KEEP)
        }
    }

    private suspend fun afterLoadFragments() {
        withMain {
            binding.dataFragment.visibleFadeSus(400)
        }
        intiOnBackPressed {
            browser?.also {
                if (it.onMyOption) {
                    toFinish()
                }
            } ?: toFinish()
        }
    }

    private var newUserListener: DialogNewUser.CreateNewListener?
        get() {
            return DialogNewUser.CreateNewListener { realName: String, dataName: String ->
                kotlin.runCatching {
                    createNewUser(realName, dataName)
                }.onFailure {
                    onDismissDia()
                }
            }
        }
        set(value) {
            value.logProvLess()
        }

    private fun onDismissDia() {
        createNewUser(
            com.pt.common.BuildConfig.JUST_NAME,
            com.pt.common.BuildConfig.JUST_NAME + System.currentTimeMillis()
        )
    }

    private fun createNewUser(realName: String, dataName: String) {
        launchDef {
            withBack {
                newDBDataUser {
                    TablesModelUser(userName = realName, userId = dataName).run {
                        insertUser()
                    }
                }
            }
            withBack {
                newDBTables(dataName.noteTab) {
                    createTable(dataName)
                }
            }
            withBack {
                newDBDataUserCounter {
                    TablesModelUser(userName = realName, userId = dataName).apply {
                        insertCounterUser()
                    }
                }
            }
            withBack {
                newDBTablesCounter(dataName.countTab) {
                    createTable(dataName)
                }
            }
            withBack {
                updatePrefBoolean(USER_ENT, true)
                loadFragments()
            }
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.apply {
            changeLayout(actionBarHeight, resources.statusBarHeight)
            dataCounterFrame.fitsSystemWindows = true
            dataKeeperCard.framePara(-1, actionBarHeight) {

            }
            dataTab.apply {
                editAppearance()
                setTextColor(theme.findAttr(R.attr.rmoText))
            }
            counterTab.apply {
                editAppearance()
                setTextColor(theme.findAttr(R.attr.rmoText))
            }
        }

    }

    private fun ActivityMainRecorderBinding.changeLayout(actBarHeight: Int, statBarHeight: Int) {
        dataKeeperMain.myStatMargin(resources.statusBarHeight)
        dataCounterFrame.framePara(
            -1,
            -1
        ) {
            topMargin = actBarHeight - statBarHeight
        }
    }

    override fun onAttachedToWindow() {
        nightRider.let {
            window?.apply {
                myBaseActivity(!it)
                statusBarColor = theme.findAttr(R.attr.rmoBackground)
            }
        }
        super.onAttachedToWindow()
    }

    @com.pt.common.global.MainAnn
    override fun onDestroy() {
        binder = null
        browser = null
        newUserListener = null
        super.onDestroy()
    }
}
