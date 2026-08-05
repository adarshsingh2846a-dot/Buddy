package com.buddy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.json.JSONObject

class RemoteCommandReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "com.buddy.action.REMOTE_COMMAND") return
        val payload = intent.getStringExtra("payload") ?: return
        try {
            val json = JSONObject(payload)
            val cmd = ParsedCommand(
                target = json.optString("target", CommandParser.TARGET_LOCAL),
                action = json.optString("action", CommandParser.ACTION_UNKNOWN),
                args = json.optString("args", ""),
                rawText = json.optString("rawText", "")
            )
            Router(context, Settings.getInstance(context)).handleIncoming(cmd)
        } catch (e: Exception) { Logger.e("JSON Parse Error", e) }
    }
}
