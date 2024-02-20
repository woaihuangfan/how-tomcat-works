package com.fan.classloader

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MyClassLoaderTest {

    @Test
    fun findClass() {
        val myClassLoader = MyClassLoader()
        val loadedClass = myClassLoader.loadClass("MyClass")
        val instance = loadedClass.getDeclaredConstructor().newInstance()
        val method = loadedClass.getMethod("hi")
        assertEquals("Hello class loader!", method.invoke(instance))
    }
}