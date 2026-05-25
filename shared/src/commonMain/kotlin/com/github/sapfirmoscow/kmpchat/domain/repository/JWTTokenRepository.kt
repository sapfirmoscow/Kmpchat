package com.github.sapfirmoscow.kmpchat.domain.repository

interface JWTTokenRepository {
    fun getToken(): String?
    fun saveToken(token: String)
    fun deleteToken()
}