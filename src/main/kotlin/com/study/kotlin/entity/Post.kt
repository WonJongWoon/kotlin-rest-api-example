package com.study.kotlin.entity

import com.study.kotlin.dto.PostResponseDto
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "POSTS")
@Entity
class Post(var title: String, var body: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun toResponseDto(): PostResponseDto = PostResponseDto(id!!, title, body)
}
