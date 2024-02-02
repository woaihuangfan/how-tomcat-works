package com.fan

data class Request(
    val method: String,
    val protocol: String,
    val uri: String
)