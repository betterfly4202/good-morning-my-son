package com.goodmorning.batch.domain.collect

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

@Component
class YoutubeSubtitleEmbedderTasklet(
    private val youtubeApiClient: YoutubeApiClient,
    private val subtitleProcessor: SubtitleProcessor,
    private val qdrantClient: QdrantClient,
) : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val channelId = "UCxE..."  // 채널 ID 고정 or config로 분리 가능

        val videoIds = youtubeApiClient.fetchAllVideoIds(channelId)

        videoIds.forEach { videoId ->
            val subtitleText = youtubeApiClient.fetchSubtitle(videoId)
            val chunks = subtitleProcessor.chunk(subtitleText)
            val embeddings = subtitleProcessor.embed(chunks)

            qdrantClient.save(videoId, chunks, embeddings)
        }

        return RepeatStatus.FINISHED
    }
}