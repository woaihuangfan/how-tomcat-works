package com.fan.server

import java.net.Socket
import java.nio.channels.SocketChannel

interface SocketHandler {
    fun processSocket(socket: Socket) {}
    fun processSocket(socketChannel: SocketChannel?) {}
}