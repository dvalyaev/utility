package com.example

object FileUtils {

    fun readResource(path: String): String = javaClass.getResource(path)!!.readText()

}