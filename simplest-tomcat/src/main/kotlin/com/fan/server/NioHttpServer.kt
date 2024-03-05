package com.fan.server

import com.fan.http.HttpProcessor.buildResponse
import com.fan.http.HttpProcessor.parseBytesToHttpRequest
import com.fan.nio.MyServerSocketChannel
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.nio.charset.StandardCharsets

class NioHttpServer(private val port: Int) : SocketHandler {

    override fun processSocket(socketChannel: SocketChannel?) {
        val buffer = ByteBuffer.wrap(ByteArray(1024))
        socketChannel?.let { channel ->
            //将数据读取到buffer中
            channel.read(buffer)
            val request = parseBytesToHttpRequest(String(buffer.array(), StandardCharsets.UTF_8).toByteArray())
            // 输出响应
            channel.write(
                ByteBuffer.wrap(
                    buildResponse(request).toString().toByteArray()
                )
            )
        }
    }

    private lateinit var serverSocketChannel: MyServerSocketChannel

    fun start() {
        serverSocketChannel = MyServerSocketChannel(port, this)
        serverSocketChannel.start()
    }

    fun stop() {
        if (this::serverSocketChannel.isInitialized) {
            serverSocketChannel.stop()
        }
    }
}