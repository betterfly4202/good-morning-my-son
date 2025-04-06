package com.goodmorning.batch.domain.collect

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class YoutubeBatchJobConfiguration(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val youtubeSubtitleEmbedderTasklet: YoutubeSubtitleEmbedderTasklet,
) {

    @Bean
    fun job(): Job = JobBuilder("youtubeJob", jobRepository)
        .start(embedSubtitlesStep())
        .build()
    @Bean
    fun embedSubtitlesStep(): Step = StepBuilder("embedSubtitlesStep", jobRepository)
        .tasklet(youtubeSubtitleEmbedderTasklet, transactionManager)
        .build()
}