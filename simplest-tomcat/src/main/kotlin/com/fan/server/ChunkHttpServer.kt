package com.fan.server

import java.net.Socket

class ChunkHttpServer(port: Int) : BaseHttpServer(port) {
    override fun processSocket(socket: Socket) {
        socket.getOutputStream().write("HTTP/1.1 200 OK\r\n".toByteArray())
        socket.getOutputStream().write("Transfer-Encoding: chunked\r\n".toByteArray())
        socket.getOutputStream().write("Content-Type: text/plain\r\n".toByteArray())
        socket.getOutputStream().write("\r\n".toByteArray())
        socket.getOutputStream().write("5\r\n".toByteArray())
        socket.getOutputStream().write("123\r\n\r\n".toByteArray())
        socket.getOutputStream().flush()

        socket.getOutputStream().write("2\r\n".toByteArray())
        socket.getOutputStream().write("ab\r\n".toByteArray())
        socket.getOutputStream().flush()

        socket.getOutputStream().write("0\r\n".toByteArray())
        socket.getOutputStream().write("\r\n".toByteArray())
        socket.getOutputStream().write("13\r\n".toByteArray())
        socket.getOutputStream().write("am I ignored?\r\n".toByteArray())
        socket.getOutputStream().flush()
    }
}