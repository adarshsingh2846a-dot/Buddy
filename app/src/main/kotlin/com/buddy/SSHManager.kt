package com.buddy

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SSHManager(private val settings: Settings) {
    suspend fun send(cmd: ParsedCommand): Boolean = withContext(Dispatchers.IO) {
        if (settings.peerHost.isBlank()) return@withContext false
        try {
            val payload = JSONObject().apply {
                put("target", CommandParser.TARGET_LOCAL)
                put("action", cmd.action)
                put("args", cmd.args)
                put("rawText", cmd.rawText)
            }.toString().replace("'", "'\\''")

            val shellCmd = "am broadcast -a com.buddy.action.REMOTE_COMMAND -n com.buddy/.RemoteCommandReceiver --es payload '$payload'"
            
            val jsch = JSch()
            val session = jsch.getSession(settings.peerUser, settings.peerHost, settings.peerPort).apply {
                setPassword(settings.peerPass)
                setConfig("StrictHostKeyChecking", "no")
                connect(5000)
            }
            val channel = session.openChannel("exec") as ChannelExec
            channel.setCommand(shellCmd)
            channel.connect(5000)
            while (!channel.isClosed) { Thread.sleep(50) }
            val success = channel.exitStatus == 0
            channel.disconnect()
            session.disconnect()
            Logger.i("SSH Command Sent. Success: $success")
            success
        } catch (e: Exception) {
            Logger.e("SSH Delivery failed", e)
            false
        }
    }
}
