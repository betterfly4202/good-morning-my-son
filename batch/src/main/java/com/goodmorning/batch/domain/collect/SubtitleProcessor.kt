package com.goodmorning.batch.domain.collect

import org.springframework.stereotype.Component

@Component
class SubtitleProcessor {
    fun chunk(text: String): List<String> {
        return text.chunked(300) // 또는 문단 단위 등
    }

    fun embed(chunks: List<String>): List<FloatArray> {
        return chunks.map { chunk ->
            callHuggingfaceInferenceApi(chunk) // HTTP 호출
        }
    }

    private fun callHuggingfaceInferenceApi(text: String): FloatArray {
        // HTTP POST로 HuggingFace 모델 호출 후 벡터 반환
        TODO("HTTP 클라이언트 작성 필요")
    }
}