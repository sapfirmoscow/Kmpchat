package com.github.sapfirmoscow.kmpchat.data.local

interface EncryptedStorage {
    fun saveString(key: String, value: String)
    fun getString(key: String) : String?
}