package com.fan.http

import java.nio.charset.StandardCharsets

object HttpProcessor {

    fun parseBytesToHttpRequest(bytes: ByteArray): Request {
        val plainRequest = String(bytes, StandardCharsets.UTF_8)
        println("Received request:\r\n--------------\r\n$plainRequest\r\n--------------")
        val httpMethod = extractHttpMethod(plainRequest)
        val uri = extractUri(plainRequest)
        val protocol = extractProtocol(plainRequest)
        return Request(httpMethod, protocol, uri)
    }

    fun buildResponse(request: Request): Response {
        val body = "Hello, World and ${request.uri}!"
        return Response(
            statusLine = "HTTP/1.1 200 OK",
            headers = "Content-Type: text/plain\r\nContent-Length: ${body.length}",
            body = body
        )
    }

    private fun extractHttpMethod(plainRequest: String): String {
        return plainRequest.substring(0, plainRequest.indexOf(" "))
    }

    private fun extractUri(plainRequest: String): String {
        val start = plainRequest.indexOf(" ") + 1
        val end = plainRequest.indexOf("HTTP") - 1
        return plainRequest.substring(start, end)
    }

    private fun extractProtocol(plainRequest: String): String {
        val start = plainRequest.indexOf("HTTP") + 5
        val end = plainRequest.indexOf("\r\n")
        return plainRequest.substring(start, end)
    }
}