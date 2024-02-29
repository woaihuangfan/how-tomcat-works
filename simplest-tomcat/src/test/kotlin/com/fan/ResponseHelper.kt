package com.fan

import java.net.Socket

object ResponseHelper {
    fun decodeResponse(socket: Socket): String {
        val inputStream = socket.getInputStream()
        return inputStream.readAllBytes().decodeToString()
    }
}