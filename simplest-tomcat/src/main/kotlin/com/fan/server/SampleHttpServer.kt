package com.fan.server

import com.fan.http.HttpProcessor.buildResponse
import com.fan.http.HttpProcessor.parseBytesToHttpRequest
import com.fan.http.Request
import com.fan.http.Response
import java.net.Socket


class SampleHttpServer(port: Int) : BaseHttpServer(port) {

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

    private fun sendResponseToClient(response: Response, socket: Socket) {
        val responseBytes = response.toString().toByteArray()
        socket.getOutputStream().write(responseBytes)
        println("${currentThreadName()}Response sent to client.")
    }

    private fun currentThreadName() = "[${Thread.currentThread().name}] "


}
