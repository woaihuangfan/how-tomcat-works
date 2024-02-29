package com.fan

import java.net.Socket
import java.nio.charset.StandardCharsets


class MyHttpServer(port: Int) : BaseHttpServer(port) {

    override fun processSocket(socket: Socket) {
        val requestBytes = readBytesFromSocketInputStream(socket)
        println("${currentThreadName()}Received bytes from client:${requestBytes.contentToString()}")
        val httpRequest = parseBytesToHttpRequest(requestBytes)
        val httpResponse = buildResponse(httpRequest)
        sendResponseToClient(httpResponse, socket)
    }

    private fun readBytesFromSocketInputStream(socket: Socket): ByteArray {
        return socket.getInputStream().let {
            val bytes = ByteArray(it.available())
            it.read(bytes)
            bytes
        }
    }

    private fun parseBytesToHttpRequest(bytes: ByteArray): Request {
        val plainRequest = String(bytes, StandardCharsets.UTF_8)
        println("${currentThreadName()}Received request:\r\n--------------\r\n$plainRequest\r\n--------------")
        val httpMethod = extractHttpMethod(plainRequest)
        val uri = extractUri(plainRequest)
        val protocol = extractProtocol(plainRequest)
        return Request(httpMethod, protocol, uri)
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

    private fun buildResponse(request: Request): Response {
        val body = "Hello, World and ${request.uri}!"
        return Response(
            statusLine = "HTTP/1.1 200 OK",
            headers = "Content-Type: text/plain\r\nContent-Length: ${body.length}",
            body = body
        )
    }


    private fun sendResponseToClient(response: Response, socket: Socket) {
        val responseBytes = response.toString().toByteArray()
        socket.getOutputStream().write(responseBytes)
        println("${currentThreadName()}Response sent to client.")
    }

    private fun currentThreadName() = "[${Thread.currentThread().name}] "


}
