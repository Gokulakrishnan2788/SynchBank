package com.architect.banking.feature.login

import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings

internal object SecurityChecker {

    fun isUsbDebuggingEnabled(context: Context): Boolean = try {
        Settings.Global.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) == 1
    } catch (_: Exception) {
        false
    }

    /**
     * Returns display names of any known screen-recorder / mirroring apps that are installed.
     * Package list covers the most common screen-recording, remote-desktop, and cast apps.
     */
    fun getInstalledScreenRecorders(context: Context): List<String> {
        val knownRecorders = mapOf(
            "com.mobizen.mirroring" to "Mobizen Screen Recorder",
            "com.kimcy929.screenrecorder" to "Screen Recorder",
            "com.hecorat.screenrecorder.free" to "AZ Screen Recorder (free)",
            "com.hecorat.screenrecorder" to "AZ Screen Recorder",
            "jp.ne.necsoft.app.screenrecorder" to "Screen Recorder (NEC)",
            "com.duapps.recorder" to "DU Recorder",
            "com.ilos.screenrecorder" to "ilos Screen Recorder",
            "com.fennifith.adb.lolrecorder" to "LolliRecord",
            "com.callrecorder.screen.recorder" to "Screen Recorder & Video Editor",
            "teamviewer.host" to "TeamViewer Host",
            "com.teamviewer.teamviewer" to "TeamViewer",
            "com.anydesk.anydeskandroid" to "AnyDesk",
            "net.anydesk" to "AnyDesk",
            "com.google.android.apps.screenrecording" to "Google Screen Recording",
            "com.samsung.android.app.screenrecorder" to "Samsung Screen Recorder",
            "com.miui.screenrecorder" to "MIUI Screen Recorder",
            "com.huawei.screenrecorder" to "Huawei Screen Recorder",
            "com.oneplus.screenrecorder" to "OnePlus Screen Recorder",
            "com.xrecorder.video" to "X Screen Recorder",
            "com.vidma.screen" to "Vidma Screen Recorder",
            "com.nll.screenrecorderpro" to "Screen Recorder Pro",
        )

        val pm = context.packageManager
        return knownRecorders.entries
            .filter { (pkg, _) ->
                try {
                    pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES)
                    true
                } catch (_: PackageManager.NameNotFoundException) {
                    false
                }
            }
            .map { (_, name) -> name }
    }
}
