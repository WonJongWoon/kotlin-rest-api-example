package com.study.kotlin.service

import com.study.kotlin.dto.PostRequestDto
import com.study.kotlin.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PostService(private val postRepository: PostRepository) {

    fun savePost(postDto: PostRequestDto) = postRepository.save(postDto.toEntity()).toResponseDto()
    fun findPost(postId: Long) =
        postRepository.findByIdOrNull(postId)?.toResponseDto() ?: throw IllegalArgumentException("존재하지 않는 게시물 번호입니다.")

    fun updatePost(postId: Long, postDto: PostRequestDto) = postRepository.findByIdOrNull(postId)?.let {
        it.title = postDto.title
        it.body = postDto.body
        it.toResponseDto()
    } ?: throw IllegalArgumentException("존재하지 않는 게시물 번호입니다.")

    fun deletePost(postId: Long) {
        postRepository.findByIdOrNull(postId)?.let {
            postRepository.delete(it)
        } ?: throw IllegalArgumentException("존재하지 않는 게시물 번호입니다.")
    }
}
