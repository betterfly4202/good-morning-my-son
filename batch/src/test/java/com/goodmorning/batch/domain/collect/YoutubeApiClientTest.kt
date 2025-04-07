package com.goodmorning.batch.domain.collect

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class YoutubeApiClientTest{

    @Autowired
    lateinit var sut: YoutubeApiClient
    @Test
    fun `채널의 모든 영상 ID를 조회`() {
        val channelId = "UCgFupN0s5eAxvvgAFgvriRA"
        val videoIds = sut.fetchAllVideoIds(channelId)

        println("✅ 영상 수: ${videoIds.size}")
        videoIds.take(5).forEach { println("🎥 $it") }

        assert(videoIds.isNotEmpty())  // 간단한 검증
    }
}