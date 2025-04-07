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
        val channelId = "UCgFupN0s5eAxvvgAFgvriRA"

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