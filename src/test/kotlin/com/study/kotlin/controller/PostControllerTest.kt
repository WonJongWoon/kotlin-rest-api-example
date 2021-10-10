package com.study.kotlin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.study.kotlin.dto.PostRequestDto
import com.study.kotlin.dto.PostResponseDto
import io.kotest.core.extensions.Extension
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@AutoConfigureMockMvc
@Import(RestDocsConfiguration::class)
@SpringBootTest
@Suppress("BlockingMethodInNonBlockingContext")
class PostControllerTest : BehaviorSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun listeners(): List<TestListener> = listOf(SpringListener)

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        private const val API_END_POINT = "/api/v1"
    }

    init {
        given("POST") {
            val postRequestDto = PostRequestDto("Title", "Body")
            `when`("글을 작성하면") {
                val response = mockMvc.perform(
                    post("${API_END_POINT}/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequestDto))
                ).andReturn().response
                then("성공한다") {
                    with(response) {
                        val result =
                            objectMapper.readValue<PostResponseDto>(contentAsString, PostResponseDto::class.java)
                        status shouldBe HttpStatus.CREATED.value()
                        result.title shouldBe postRequestDto.title
                        result.body shouldBe postRequestDto.body
                    }
                    document(
                        "create",
                        requestFields(
                            fieldWithPath("title").description("게시글 제목"),
                            fieldWithPath("body").description("게시글 내용"),
                        ),
                        responseFields(
                            fieldWithPath("id").description("게시물 번호"),
                            fieldWithPath("title").description("게시글 제목"),
                            fieldWithPath("body").description("게시글 내용"),
                        )
                    )
                }

            }
        }
    }
}