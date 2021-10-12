package com.study.kotlin.repository

import com.study.kotlin.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>
