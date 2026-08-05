package com.buddy

import android.content.Context

class Executor(private val context: Context) {
    private val launcher = AppLauncher(context)
    private val termux = TermuxBridge(context)
    private val linux = LinuxBridge()

    fun execute(cmd: ParsedCommand): Boolean {
        Logger.i("Local Exec: ${cmd.action} -> ${cmd.args}")
        return when (cmd.action) {
            CommandParser.ACTION_APP -> launcher.openApp(cmd.args)
            CommandParser.ACTION_URL -> launcher.openUrl(cmd.args)
            CommandParser.ACTION_SEARCH -> launcher.search(cmd.args)
            CommandParser.ACTION_CALL -> launcher.call(cmd.args)
            CommandParser.ACTION_TERMUX -> termux.execute(cmd.args)
            CommandParser.ACTION_LINUX -> linux.execute(cmd.args)
            else -> false
        }
    }
}
