package com.fan.nio

import com.fan.Lifecycle
import com.fan.server.SocketHandler
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel

class MyServerSocketChannel(private val port: Int, private val socketHandler: SocketHandler) : Lifecycle {

    private var isRunning = true

    private lateinit var serverSocketChannel: ServerSocketChannel

    override fun start() {
        serverSocketChannel = ServerSocketChannel.open()
        serverSocketChannel.bind(InetSocketAddress(port))
        println("Server started successfully, listening on port $port")
        Thread {
            while (isRunning) {
                // 默认情况下accept 方法会阻塞
                // 可以通过configureBlocking来设置非阻塞模式，非阻塞模式下需要判断返回的SocketChannel是否是null
                serverSocketChannel.configureBlocking(false)
                val socketChannel = serverSocketChannel.accept()
                try {
                    socketHandler.processSocket(socketChannel)
                } catch (exception: Exception) {
                    // ignore
                } finally {
                    socketChannel?.close()
                }

            }
        }.start()
    }

    override fun stop() {
        isRunning = false
        if (::serverSocketChannel.isInitialized) {
            serverSocketChannel.close()
        }
    }
}