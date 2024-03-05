package com.fan

import com.fan.server.ChunkHttpServer
import com.fan.server.NioHttpServer
import com.fan.server.SampleHttpServer


fun main() {
//    SampleHttpServer(8080).start()
//    ChunkHttpServer(8080).start()
    NioHttpServer(8080).start()
}