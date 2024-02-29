package com.fan

abstract class BaseHttpServer(private val port: Int) : SocketHandler {

    private lateinit var socketServer: MySocketServer

    fun start() {
        socketServer = MySocketServer(port, this)
        socketServer.start()
    }

    fun stop() {
        if (this::socketServer.isInitialized) {
            socketServer.stop()
        }
    }
}