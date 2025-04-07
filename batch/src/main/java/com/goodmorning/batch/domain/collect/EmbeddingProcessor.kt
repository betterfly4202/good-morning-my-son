package com.goodmorning.batch.domain.collect

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component

@Component
class EmbeddingProcessor {
    fun process(chunks: List<String>):List<EmbeddingResult> {
        val pythonExec = "$ROOT_PATH/venv/bin/python3"
        val pythonPath = "$ROOT_PATH/script/embed.py"

        val process = ProcessBuilder(pythonExec, pythonPath)
            .redirectErrorStream(false) // stderr는 무시
            .start()

        // 입력 쓰기: write → flush → close (여기서 반드시 close!)
        val writer = process.outputStream.bufferedWriter()
        writer.write(ObjectMapper().writeValueAsString(chunks))
        writer.flush()
        writer.close()

        // 결과 읽기
        val result = process.inputStream.bufferedReader().readText()

        // 종료 확인
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("Python process failed. Output:\n$result")
        }

        return ObjectMapper().readValue(result, object : TypeReference<List<EmbeddingResult>>() {})
    }

    data class EmbeddingResult @JsonCreator constructor(
        @JsonProperty("text") val text: String,
        @JsonProperty("embedding") val embedding: List<Double>
    )

    companion object{
        private val ROOT_PATH = "/Users/betterfly/repository/good-morning-my-son/"
    }
}