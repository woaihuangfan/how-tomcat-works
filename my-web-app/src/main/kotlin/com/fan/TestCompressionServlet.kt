package com.fan

import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class TestCompressionServlet : HttpServlet() {
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val stringBuilder = StringBuilder()
        for (i in 1 until 10000000) {
            stringBuilder.append("Hi,")
        }
        response.contentType="text/html"
        val result = stringBuilder.toString()
        response.setContentLength(result.length)
        response.writer.write(result)
    }
}