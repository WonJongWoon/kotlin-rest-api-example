package com.study.kotlin.controller

import com.study.kotlin.dto.PostRequestDto
import com.study.kotlin.dto.PostResponseDto
import com.study.kotlin.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class PostController(private val postService: PostService) {


    @GetMapping("/posts/{postId}")
    fun findPost(@PathVariable postId: Long): ResponseEntity<PostResponseDto> =
        ResponseEntity.ok(postService.findPost(postId))

    @PostMapping("/posts")
    fun createPost(@RequestBody postRequestDto: PostRequestDto): ResponseEntity<PostResponseDto> =
        ResponseEntity.status(HttpStatus.CREATED).body(postService.savePost(postRequestDto))


    @DeleteMapping("/posts/{postId}")
    fun deletePost(@PathVariable postId: Long): ResponseEntity<Unit> = ResponseEntity.ok(postService.deletePost(postId))

    @PutMapping("/posts/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody postRequestDto: PostRequestDto
    ): ResponseEntity<PostResponseDto> = ResponseEntity.ok(postService.updatePost(postId, postRequestDto))

}