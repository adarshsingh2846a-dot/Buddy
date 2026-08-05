package com.buddy

class CommandParser(private val settings: Settings) {
    companion object {
        const val ACTION_APP = "APP"
        const val ACTION_URL = "URL"
        const val ACTION_SEARCH = "SEARCH"
        const val ACTION_TERMUX = "TERMUX"
        const val ACTION_LINUX = "LINUX"
        const val ACTION_CALL = "CALL"
        const val ACTION_UNKNOWN = "UNKNOWN"
        const val TARGET_LOCAL = "LOCAL"
        const val TARGET_REMOTE = "REMOTE"
    }

    fun parse(text: String): ParsedCommand {
        val lowerText = text.lowercase().trim()
        
        var target = TARGET_LOCAL
        var cleanText = lowerText

        val remoteIndicators = listOf("on black ass", "on ${settings.peerWakeWord.replace("yo ", "")}", "remotely")
        for (ind in remoteIndicators) {
            if (lowerText.contains(ind)) {
                target = TARGET_REMOTE
                cleanText = cleanText.replace(ind, "").trim()
            }
        }

        val urlRegex = Regex("(https?://\\S+|\\S+\\.(com|org|net|io|co))")
        urlRegex.find(cleanText)?.let {
            return ParsedCommand(target, ACTION_URL, it.value, text)
        }

        if (cleanText.startsWith("search google for ") || cleanText.startsWith("google ")) {
            val q = cleanText.removePrefix("search google for ").removePrefix("google ").trim()
            return ParsedCommand(target, ACTION_SEARCH, "google:$q", text)
        }
        if (cleanText.startsWith("search youtube for ") || cleanText.startsWith("youtube ")) {
            val q = cleanText.removePrefix("search youtube for ").removePrefix("youtube ").trim()
            return ParsedCommand(target, ACTION_SEARCH, "youtube:$q", text)
        }
        if (cleanText.startsWith("search chatgpt for ") || cleanText.startsWith("ask chatgpt ")) {
            val q = cleanText.removePrefix("search chatgpt for ").removePrefix("ask chatgpt ").trim()
            return ParsedCommand(target, ACTION_SEARCH, "chatgpt:$q", text)
        }

        if (cleanText.startsWith("open ") || cleanText.startsWith("launch ")) {
            val app = cleanText.removePrefix("open ").removePrefix("launch ").trim()
            return ParsedCommand(target, ACTION_APP, app, text)
        }

        if (cleanText.startsWith("call ")) {
            val contact = cleanText.removePrefix("call ").trim()
            return ParsedCommand(target, ACTION_CALL, contact, text)
        }

        if (cleanText.startsWith("execute termux ") || cleanText.startsWith("run termux ")) {
            val cmd = cleanText.removePrefix("execute termux ").removePrefix("run termux ").trim()
            return ParsedCommand(target, ACTION_TERMUX, cmd, text)
        }

        if (cleanText.startsWith("execute linux ") || cleanText.startsWith("run linux ")) {
            val cmd = cleanText.removePrefix("execute linux ").removePrefix("run linux ").trim()
            return ParsedCommand(target, ACTION_LINUX, cmd, text)
        }

        return ParsedCommand(target, ACTION_UNKNOWN, "", text)
    }
}
