package com.study.kotlin.dto

import com.study.kotlin.entity.Post

data class PostRequestDto(val title: String, val body: String) {
    fun toEntity(): Post {
        return Post(title, body)
    }
}
