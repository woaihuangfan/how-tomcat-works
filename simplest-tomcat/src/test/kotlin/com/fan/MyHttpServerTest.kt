import com.fan.Constants.TEST_PORT
import com.fan.MyHttpServer
import com.fan.RequestHelper.sendRequest
import com.fan.ResponseHelper.decodeResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class MyHttpServerTest {

    private val httpServer = MyHttpServer(TEST_PORT)

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
            "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n" + "Content-Length: 25\r\n\r\nHello, World and /sample!",
            decodeResponse(socket)
        )

    }
}
