package com.buddy

import android.content.Context
import android.content.Intent

class TermuxBridge(private val context: Context) {
    fun execute(command: String): Boolean {
        return try {
            val intent = Intent("com.termux.RUN_COMMAND")
            intent.setClassName("com.termux", "com.termux.app.RunCommandService")
            intent.putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash")
            intent.putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf("-c", command))
            intent.putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home")
            intent.putExtra("com.termux.RUN_COMMAND_BACKGROUND", true)
            context.startService(intent)
            Logger.i("Termux command sent: $command")
            true
        } catch (e: Exception) {
            Logger.e("Termux execution failed", e)
            false
        }
    }
}
