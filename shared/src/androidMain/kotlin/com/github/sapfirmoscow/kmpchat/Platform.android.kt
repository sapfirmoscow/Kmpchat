package com.github.sapfirmoscow.kmpchat

import android.os.Build

/** Android-реализация [Platform]. Возвращает название ОС с номером SDK. */
class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()