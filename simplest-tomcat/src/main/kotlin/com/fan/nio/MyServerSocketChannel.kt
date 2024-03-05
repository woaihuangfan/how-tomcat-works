package com.fan.nio

import com.fan.Lifecycle
import com.fan.server.SocketHandler
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

class MyServerSocketChannel(private val port: Int, private val socketHandler: SocketHandler) : Lifecycle {

    private var isRunning = true

    private lateinit var serverSocketChannel: ServerSocketChannel

    override fun start() {
        serverSocketChannel = ServerSocketChannel.open()
        serverSocketChannel.bind(InetSocketAddress(port))
        //与Selector一起使用时，Channel必须处于非阻塞模式下
        serverSocketChannel.configureBlocking(false)
        println("Server started successfully, listening on port $port")
        val selector = Selector.open()
        val selectionKey = serverSocketChannel.register(selector, 0)
        selectionKey.interestOps(SelectionKey.OP_ACCEPT)
        Thread {
            while (isRunning) {
                // select()会阻塞，我们可以用selectNow()的方式让它立即返回，但是会造成大量轮询，造成CPU空转
                selector.select()
                val iterator = selector.selectedKeys().iterator()
                while (iterator.hasNext()) {
                    val key = iterator.next()
                    if (key.isAcceptable) {
                        println("Accept Event!")
                        val channel = key.channel() as ServerSocketChannel
                        // 默认情况下accept 方法会阻塞
                        // 可以通过configureBlocking来设置非阻塞模式，非阻塞模式下需要判断返回的SocketChannel是否是null
                        val socketChannel = channel.accept()
                        socketChannel.configureBlocking(false)
                        socketChannel.register(selector, 0).interestOps(SelectionKey.OP_READ or SelectionKey.OP_WRITE)
                        iterator.remove()
                    }
                    if (key.isReadable && key.isWritable) {
                        val socketChannel = key.channel() as SocketChannel
                        try {
                            socketHandler.processSocket(socketChannel)
                        } catch (exception: Exception) {
                            // ignore
                        } finally {
                            socketChannel.close()
                        }
                        iterator.remove()
                    }

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