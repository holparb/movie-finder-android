package com.holparb.moviefinder.testutils

import java.io.BufferedReader
import java.io.InputStreamReader

class JsonReader {
    companion object {
        fun readJsonFile(fileName: String): String {
            val inputStream = this::class.java.classLoader!!.getResourceAsStream(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val content = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                content.append(line)
            }
            reader.close()
            return content.toString()
        }
    }
}
