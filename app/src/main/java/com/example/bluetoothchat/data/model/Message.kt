package com.example.bluetoothchat.data.model

data class Message(
    val content: String,
    val isSent: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)