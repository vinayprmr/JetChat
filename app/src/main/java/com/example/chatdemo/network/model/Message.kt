package com.example.chatdemo.network.model

data class Message(
    val time: String,
    val room: String,
    val msg: String,
    val user: String
)