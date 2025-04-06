package com.goodmorning.batch.domain.collect

import org.springframework.stereotype.Component

@Component
class QdrantClient {
    fun save(videoId: String, chunks: List<String>, vectors: List<FloatArray>) {
        // Qdrant REST API 호출하여 collection에 저장
        // vector + payload(text + videoId 등) 함께 저장
    }
}