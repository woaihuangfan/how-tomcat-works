package com.fan.http

data class Request(
    val method: String,
    val protocol: String,
    val uri: String
)