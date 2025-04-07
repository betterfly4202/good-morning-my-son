package com.goodmorning.batch.domain.collect

import org.springframework.stereotype.Component

@Component
class ScriptReader {
    fun read(videoId: String): String{
        val pythonExec = "$ROOT_PATH/venv/bin/python3"
        val pythonPath = "$ROOT_PATH/script/transcript.py"
        val process = ProcessBuilder(pythonExec, pythonPath, videoId)
            .redirectErrorStream(true)
            .start()
        val result = process.inputStream.bufferedReader().readText()

        return result
    }

    companion object{
        private val ROOT_PATH = "/Users/betterfly/repository/good-morning-my-son/"
    }
}