package com.github.sapfirmoscow.kmpchat

/** JVM-реализация [Platform]. Возвращает название JVM с версией Java. */
class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()