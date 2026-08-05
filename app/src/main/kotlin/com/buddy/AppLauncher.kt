package com.buddy

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

class AppLauncher(private val context: Context) {
    private val commonApps = mapOf(
        "chrome" to "com.android.chrome",
        "youtube" to "com.google.android.youtube",
        "chatgpt" to "com.openai.chatgpt",
        "linux" to "com.termux",
        "termux" to "com.termux",
        "maps" to "com.google.android.apps.maps",
        "settings" to "com.android.settings",
        "whatsapp" to "com.whatsapp",
        "camera" to "com.android.camera",
        "gallery" to "com.android.gallery3d" 
    )

    fun openApp(appName: String): Boolean {
        val pm = context.packageManager
        var pkg = commonApps[appName.lowercase()]
        if (pkg == null) {
            val apps = pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0L))
            pkg = apps.firstOrNull { pm.getApplicationLabel(it).toString().lowercase().contains(appName.lowercase()) }?.packageName
        }
        if (pkg == null) return false
        return try {
            val intent = pm.getLaunchIntentForPackage(pkg)?.apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
            if (intent != null) {
                context.startActivity(intent)
                true
            } else false
        } catch (e: Exception) {
            Logger.e("Failed to open app $appName", e)
            false
        }
    }

    fun openUrl(url: String): Boolean {
        return try {
            val fullUrl = if (!url.startsWith("http")) "https://$url" else url
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl)).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
            context.startActivity(intent)
            true
        } catch (e: Exception) { false }
    }

    fun search(queryStr: String): Boolean {
        val parts = queryStr.split(":", limit = 2)
        if (parts.size < 2) return false
        val engine = parts[0]
        val query = Uri.encode(parts[1].trim())
        val url = when (engine) {
            "youtube" -> "https://www.youtube.com/results?search_query=$query"
            "chatgpt" -> "https://chat.openai.com/?q=$query"
            else -> "https://www.google.com/search?q=$query"
        }
        return openUrl(url)
    }

    fun call(contact: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contact")).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
            context.startActivity(intent)
            true
        } catch (e: Exception) { false }
    }
}
