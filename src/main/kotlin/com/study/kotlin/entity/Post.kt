package com.study.kotlin.entity

import com.study.kotlin.dto.PostResponseDto
import javax.persistence.*

@Table(name = "POSTS")
@Entity
class Post(var title: String, var body: String) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun toResponseDto() : PostResponseDto = PostResponseDto(id!!, title, body)
}