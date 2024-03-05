package com.fan.bio

import com.fan.server.SocketHandler
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

class MySocketServer(private val port: Int, private val socketHandler: SocketHandler) {
    private lateinit var serverSocket: ServerSocket

    private var isRunning = true

    fun start() {
        createServerSocket(port)
        Thread {
            while (isRunning) {
                val clientSocket = acceptClientConnection(serverSocket)
                clientSocket?.let {
                    try {
                        socketHandler.processSocket(clientSocket)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        closeSocket(clientSocket)
                    }
                }
            }
        }.start()
    }

    fun stop() {
        isRunning = false
        if (::serverSocket.isInitialized) {
            serverSocket.close()
        }
    }

    private fun createServerSocket(port: Int) {
        this.serverSocket = ServerSocket(port)
        println("${currentThreadName()}Server started successfully, listening on port $port")
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

    private fun currentThreadName() = "[${Thread.currentThread().name}] "
}