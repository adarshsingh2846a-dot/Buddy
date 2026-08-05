package com.buddy

data class ParsedCommand(
    val target: String,
    val action: String,
    val args: String,
    val rawText: String
)
