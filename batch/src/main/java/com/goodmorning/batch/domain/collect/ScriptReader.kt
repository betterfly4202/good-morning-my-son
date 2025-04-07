package com.goodmorning.batch.domain.collect

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component

@Component
class ScriptReader {
    fun read(videoId: String): List<Chunk> {
        val pythonExec = "$ROOT_PATH/venv/bin/python3"
        val pythonPath = "$ROOT_PATH/script/transcript.py"
        val process = ProcessBuilder(pythonExec, pythonPath, videoId)
            .redirectErrorStream(true)
            .start()
        val result = process.inputStream.bufferedReader().readText()

        return parseChunks(result)
    }

    private fun parseChunks(json: String): List<Chunk> {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(json)
    }

    data class Chunk(
        val text: String
    )

    companion object{
        private val ROOT_PATH = "/Users/betterfly/repository/good-morning-my-son/"
    }
}