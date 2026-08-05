package com.buddy

class LinuxBridge {
    fun execute(command: String): Boolean {
        return try {
            Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            Logger.i("Linux command executed: $command")
            true
        } catch (e: Exception) {
            Logger.e("Linux execution failed", e)
            false
        }
    }
}
