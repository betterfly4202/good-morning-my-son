package com.goodmorning.batch.domain.collect

import org.junit.jupiter.api.Test

class ScriptReaderTest {
    private val sut = ScriptReader()

    @Test
    fun `python script read`() {
        val result = sut.read("H8swmfmHoqk")

        print(result)
    }
}