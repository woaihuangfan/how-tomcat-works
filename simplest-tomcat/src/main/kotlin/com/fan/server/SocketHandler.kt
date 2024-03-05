package com.fan.server

import java.net.Socket

interface SocketHandler {
    fun processSocket(socket: Socket)
}