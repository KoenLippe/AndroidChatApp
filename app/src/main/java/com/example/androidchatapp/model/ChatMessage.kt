package com.example.androidchatapp.model

data class ChatMessage(
    val id: String,
    val content: String,
    val fromId: String,
    val toId: String,
    val timestamp: Long
) {
    constructor(): this("","", "","",12323)
}