package com.github.sapfirmoscow.kmpchat

/** Абстракция целевой платформы. */
interface Platform {
    /** Человекочитаемое название платформы с версией. */
    val name: String
}

/** Возвращает реализацию [Platform] для текущей платформы. */
expect fun getPlatform(): Platform