package com.goodmorning.batch.domain.collect

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class YoutubeApiClient(
    @Value("\${youtube.api-key}")
    private val apiKey: String,
    private val restTemplate: RestTemplate
) {
    fun fetchSubtitle(videoId: String): String {
        return ""
    }

    fun fetchAllVideoIds(channelId: String): List<String> {
        val uploadsPlaylistId = fetchUploadPlaylistId(channelId)
        return fetchVideoIdsFromPlaylist(uploadsPlaylistId)
    }

    private fun fetchUploadPlaylistId(channelId: String): String {
        val url = "https://www.googleapis.com/youtube/v3/channels" +
                "?part=contentDetails&id=$channelId&key=$apiKey"

        val response = restTemplate.getForObject(url, JsonNode::class.java)
        return response?.get("items")?.get(0)
            ?.get("contentDetails")?.get("relatedPlaylists")
            ?.get("uploads")?.asText() ?: throw IllegalStateException("Upload playlist not found")
    }

    private fun fetchVideoIdsFromPlaylist(playlistId: String): List<String> {
        val videoIds = mutableListOf<String>()
        var nextPageToken: String? = null

        do {
            val url = StringBuilder("https://www.googleapis.com/youtube/v3/playlistItems")
                .append("?part=snippet")
                .append("&maxResults=50")
                .append("&playlistId=$playlistId")
                .append("&key=$apiKey")
                .apply {
                    if (nextPageToken != null) append("&pageToken=$nextPageToken")
                }.toString()

            val response = restTemplate.getForObject(url, JsonNode::class.java)
            response?.get("items")?.forEach { item ->
                val videoId = item["snippet"]["resourceId"]["videoId"].asText()
                videoIds.add(videoId)
            }

            nextPageToken = response?.get("nextPageToken")?.asText()
        } while (nextPageToken != null)

        return videoIds
    }
}