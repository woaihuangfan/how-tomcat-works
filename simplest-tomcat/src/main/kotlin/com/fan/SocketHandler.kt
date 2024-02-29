package com.fan

import java.net.Socket

interface SocketHandler {
    fun processSocket(socket: Socket)
}