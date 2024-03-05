package com.fan.http

data class Response(
    val statusLine: String,
    val headers: String,
    val body: String
) {
    override fun toString(): String {
        return statusLine + "\r\n" + headers + "\r\n\r\n" + body
    }
}