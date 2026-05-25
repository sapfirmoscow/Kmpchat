package com.github.sapfirmoscow.kmpchat.domain.model

data class Chat(
    val id: String,
    val members: List<User>,
    val createdAt: String
)

data class User(
    val id: String,
    val username: String,
    val createdAt: String
)