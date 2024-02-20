package com.fan.classloader

import java.io.File
import java.io.FileInputStream
import java.io.IOException

class MyClassLoader : ClassLoader() {

    @Throws(ClassNotFoundException::class)
    override fun findClass(name: String): Class<*> {
        try {
            FileInputStream(
                File("/path/to/MyClass.class")
            ).use { fileInputStream ->
                val bytes = fileInputStream.readAllBytes()
                return defineClass(name, bytes, 0, bytes.size)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}