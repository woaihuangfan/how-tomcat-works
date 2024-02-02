package com.fan

import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.nio.charset.StandardCharsets


class MyHttpServer(private val port: Int) {

    private lateinit var serverSocket: ServerSocket

    private var isRunning = true

    fun start() {
        createServerSocket(port)
        println("${currentThreadName()}Server started successfully, listening on port $port")
        Thread {
            while (isRunning) {
                val clientSocket = acceptClientConnection(serverSocket)
                clientSocket?.let {
                    try {
                        handleClientRequest(clientSocket)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        closeSocket(clientSocket)
                    }
                }
            }
        }.start()
    }

    private fun closeSocket(clientSocket: Socket) {
        checkIfSocketClosed(clientSocket)
        if (!clientSocket.isClosed) {
            clientSocket.close()
            checkIfSocketClosed(clientSocket)
        }
    }

    private fun checkIfSocketClosed(clientSocket: Socket) {
        println("${currentThreadName()}client socket closed? ${clientSocket.isClosed}")
    }

    private fun createServerSocket(port: Int) {
        this.serverSocket = ServerSocket(port)
    }

    private fun acceptClientConnection(serverSocket: ServerSocket): Socket? {
        try {
            val clientSocket = serverSocket.accept()
            println("${currentThreadName()}Accepted client connection from: ${clientSocket.inetAddress}")
            return clientSocket
        } catch (e: SocketException) {
            //socket maybe closed from client, ignore
            println("${currentThreadName()} ${e.message}")
        }
        return null
    }

    private fun handleClientRequest(clientSocket: Socket) {
        val requestBytes = readBytesFromSocketInputStream(clientSocket)
        println("${currentThreadName()}Received bytes from client:${requestBytes.contentToString()}")
        val httpRequest = parseBytesToHttpRequest(requestBytes)
        val httpResponse = buildResponse(httpRequest)
        sendResponseToClient(httpResponse, clientSocket)
    }

    private fun readBytesFromSocketInputStream(socket: Socket): ByteArray {
        return socket.getInputStream().let {
            val bytes = ByteArray(it.available())
            it.read(bytes)
            bytes
        }
    }

    private fun parseBytesToHttpRequest(bytes: ByteArray): Request {
        val plainRequest = String(bytes, StandardCharsets.UTF_8)
        println("${currentThreadName()}Received request:\r\n--------------\r\n$plainRequest\r\n--------------")
        val httpMethod = extractHttpMethod(plainRequest)
        val uri = extractUri(plainRequest)
        val protocol = extractProtocol(plainRequest)
        return Request(httpMethod, protocol, uri)
    }


    private fun extractHttpMethod(plainRequest: String): String {
        return plainRequest.substring(0, plainRequest.indexOf(" "))
    }

    private fun extractUri(plainRequest: String): String {
        val start = plainRequest.indexOf(" ") + 1
        val end = plainRequest.indexOf("HTTP") - 1
        return plainRequest.substring(start, end)
    }

    private fun extractProtocol(plainRequest: String): String {
        val start = plainRequest.indexOf("HTTP") + 5
        val end = plainRequest.indexOf("\r\n")
        return plainRequest.substring(start, end)
    }

    private fun buildResponse(request: Request): Response {
        val body = "Hello, World and ${request.uri}!"
        return Response(
            statusLine = "HTTP/1.1 200 OK",
            headers = "Content-Type: text/plain\r\nContent-Length: ${body.length}",
            body = body
        )
    }


    private fun sendResponseToClient(response: Response, socket: Socket) {
        val responseBytes = response.toString().toByteArray()
        socket.getOutputStream().write(responseBytes)
        println("${currentThreadName()}Response sent to client.")
    }

    private fun currentThreadName() = "[${Thread.currentThread().name}] "

    fun stop() {
        isRunning = false
        if (::serverSocket.isInitialized) {
            serverSocket.close()
        }
    }
}
