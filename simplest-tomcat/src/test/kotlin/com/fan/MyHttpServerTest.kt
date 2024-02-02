import com.fan.MyHttpServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.Socket
import java.nio.charset.Charset

private const val LOCAL_HOST = "localhost"
private const val TEST_PORT = 8081

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
            "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n" +
                    "Content-Length: 25\r\n\r\nHello, World and /sample!", decodeResponse(socket)
        )

    }

    private fun sendRequest(): Socket {
        val socket = Socket(LOCAL_HOST, TEST_PORT)
        val httpRequest = "GET /sample HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Accept: application/json\r\n\r\n"
        socket.getOutputStream().write(httpRequest.toByteArray(Charset.defaultCharset()))
        return socket
    }

    private fun decodeResponse(socket: Socket): String {
        val inputStream = socket.getInputStream()
        return inputStream.readAllBytes().decodeToString()
    }
}
