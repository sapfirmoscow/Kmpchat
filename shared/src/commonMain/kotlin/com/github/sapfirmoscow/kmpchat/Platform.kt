package com.github.sapfirmoscow.kmpchat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform