package com.github.sapfirmoscow.kmpchat.di

import com.github.sapfirmoscow.kmpchat.data.local.EncryptedStorage

/**
 * Платформенный компонент DI, предоставляющий зависимости,
 * специфичные для конкретной платформы (Android, JVM и др.).
 */
interface PlatformComponent {
    /** Зашифрованное хранилище, реализованное для текущей платформы. */
    val encryptedStorage: EncryptedStorage
}