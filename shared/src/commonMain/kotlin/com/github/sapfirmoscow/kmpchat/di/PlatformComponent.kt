package com.github.sapfirmoscow.kmpchat.di

import com.github.sapfirmoscow.kmpchat.data.local.EncryptedStorage

interface PlatformComponent {
    val encryptedStorage: EncryptedStorage
}