package com.fan.server

import com.fan.Constants
import com.fan.RequestHelper
import com.fan.ResponseHelper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NioHttpServerTest {
    private val httpServer = NioHttpServer(Constants.TEST_PORT)

    @AfterEach
    fun tearDown() {
        httpServer.stop()
    }

    @Test
    fun `should successfully process valid HTTP request`() {
        // Given
        httpServer.start()

        // When
        val socket = RequestHelper.sendRequest()

        // Then
        assertEquals(
            "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n" + "Content-Length: 25\r\n\r\nHello, World and /sample!",
            ResponseHelper.decodeResponse(socket)
        )

    }
}