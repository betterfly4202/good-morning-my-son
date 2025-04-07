package com.goodmorning.batch.domain.collect

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EmbeddingProcessorTest {
    private val sut = EmbeddingProcessor()


    @Test
    fun `임베딩 테스트`(){
        val given = listOf(
            "안녕하십니까.",
            "소아청소년과 전문의 표진원입니다.",
            "아기와 놀아주기 아이의 뇌 발달에 놀아주기 굉장히 중요한데 부모님들이 애가 좋아하니까 장난감을 많이 사주기도 하고 장난감 대여를 해서 주는 경우도 있는데 가장 중요한 놀이기구, 가장 중요한 장난감은 부모다!",
            "아이 반응을 잘 봐서 아이가 딴청 피우고 좀 졸려하면 이제 놀이를 중단하고 아이를 쉬게 해주는 게 좋습니다.",
        )

        val result = sut.process(given)

        print(result)
    }
}