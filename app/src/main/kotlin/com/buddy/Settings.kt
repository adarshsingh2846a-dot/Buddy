package com.buddy

import android.content.Context
import android.content.SharedPreferences

class Settings private constructor(context: Context) {
    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences("buddy_prefs", Context.MODE_PRIVATE)

    var deviceName: String
        get() = prefs.getString("device_name", "Buddy") ?: "Buddy"
        set(value) = prefs.edit().putString("device_name", value).apply()

    var wakeWord: String
        get() = prefs.getString("wake_word", "yo buddy") ?: "yo buddy"
        set(value) = prefs.edit().putString("wake_word", value.lowercase().trim()).apply()

    var peerWakeWord: String
        get() = prefs.getString("peer_wake_word", "yo jarvis") ?: "yo jarvis"
        set(value) = prefs.edit().putString("peer_wake_word", value.lowercase().trim()).apply()

    var peerHost: String
        get() = prefs.getString("peer_host", "") ?: ""
        set(value) = prefs.edit().putString("peer_host", value.trim()).apply()

    var peerPort: Int
        get() = prefs.getInt("peer_port", 8022)
        set(value) = prefs.edit().putInt("peer_port", value).apply()

    var peerUser: String
        get() = prefs.getString("peer_user", "u0_a1") ?: "u0_a1"
        set(value) = prefs.edit().putString("peer_user", value.trim()).apply()

    var peerPass: String
        get() = prefs.getString("peer_pass", "") ?: ""
        set(value) = prefs.edit().putString("peer_pass", value).apply()

    companion object {
        @Volatile private var instance: Settings? = null
        fun getInstance(context: Context): Settings = instance ?: synchronized(this) {
            instance ?: Settings(context).also { instance = it }
        }
    }
}
