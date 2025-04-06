package com.goodmorning.batch.domain.collect

import org.springframework.stereotype.Component

@Component
class YoutubeApiClient {
    fun fetchSubtitle(videoId: String): String {
        return ""
    }

    fun fetchAllVideoIds(channelId: String): List<String> {
        return emptyList()
    }
}