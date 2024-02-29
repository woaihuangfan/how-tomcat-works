package com.fan

import com.fan.RequestHelper.sendRequest
import com.fan.ResponseHelper.decodeResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChunkHttpServerTest {
    private val httpServer = ChunkHttpServer(Constants.TEST_PORT)

    @AfterEach
    fun tearDown() {
        httpServer.stop()
    }

    @Test
    fun `should successfully process valid HTTP request`() {
        // Given
        httpServer.start()

        // When
        val socket = sendRequest()

        // Then
        assertEquals(
            "HTTP/1.1 200 OK\r\n" +
                    "Transfer-Encoding: chunked\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "\r\n" +
                    "5\r\n" +
                    "123\r\n\r\n" +
                    "2\r\n" +
                    "ab\r\n" +
                    "0\r\n" +
                    "\r\n" +
                    "13\r\n" +
                    "am I ignored?\r\n",
            decodeResponse(socket)
        )

    }
}