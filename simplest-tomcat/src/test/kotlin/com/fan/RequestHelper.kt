package com.fan

import java.net.Socket
import java.nio.charset.Charset

object RequestHelper {
    fun sendRequest(
        httpRequest: String = "GET /sample HTTP/1.1\r\n" + "Host: localhost\r\n" + "Accept: application/json\r\n\r\n"
    ): Socket {
        val socket = Socket(Constants.LOCAL_HOST, Constants.TEST_PORT)
        socket.getOutputStream().write(httpRequest.toByteArray(Charset.defaultCharset()))
        return socket
    }
}