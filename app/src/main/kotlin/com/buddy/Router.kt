package com.buddy

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Router(context: Context, settings: Settings) {
    private val executor = Executor(context)
    private val ssh = SSHManager(settings)
    private val scope = CoroutineScope(Dispatchers.IO)

    fun route(cmd: ParsedCommand, onComplete: () -> Unit) {
        if (cmd.target == CommandParser.TARGET_LOCAL) {
            executor.execute(cmd)
            onComplete()
        } else {
            scope.launch {
                ssh.send(cmd)
                withContext(Dispatchers.Main) { onComplete() }
            }
        }
    }

    fun handleIncoming(cmd: ParsedCommand) {
        executor.execute(cmd)
    }
}
