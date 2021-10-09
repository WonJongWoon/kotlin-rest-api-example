package com.study.kotlin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.study.kotlin.dto.PostRequestDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter


@AutoConfigureRestDocs
@AutoConfigureMockMvc
@WebAppConfiguration
@Import(RestDocsConfiguration::class)
@SpringBootTest
internal class PostControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

//    @Autowired
//    private lateinit var ctx: WebApplicationContext

    companion object {
        private const val API_END_POINT = "/api/v1"
    }

    @BeforeEach
    fun setUp() {
//        mockMvc = MockMvcBuilders
//            .webAppContextSetup(ctx)
//            .addFilters<DefaultMockMvcBuilder?>(CharacterEncodingFilter("UTF-8", true))
//            .build()
//        println("TEST")
    }

    @Test
    fun `글을 작성하면 성공한다`() {

        // given
        val postRequestDto = PostRequestDto("Title", "Body")

        // when
        val result = mockMvc.perform(
            post("${API_END_POINT}/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(postRequestDto))
        )

        // then
        result
            .andExpect(status().isCreated)
            .andExpect(jsonPath("title").value(postRequestDto.title))
            .andExpect(jsonPath("body").value(postRequestDto.body))
            .andDo(print())
            .andDo(
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
            )

    }
}