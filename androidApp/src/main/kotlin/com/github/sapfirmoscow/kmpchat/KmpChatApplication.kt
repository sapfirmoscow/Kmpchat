package com.github.sapfirmoscow.kmpchat

import android.app.Application
import com.github.sapfirmoscow.kmpchat.di.AndroidAppGraph
import com.github.sapfirmoscow.kmpchat.di.AppGraph
import dev.zacsweers.metro.createGraphFactory

/**
 * Класс приложения Android.
 *
 * Инициализирует [AndroidAppGraph] и [AppGraph] при старте процесса.
 * Ссылка на [appGraph] используется в [MainActivity] для получения
 * фабрик ViewModel.
 */
internal class KmpChatApplication : Application() {

    lateinit var appGraph: AppGraph
        private set

    override fun onCreate() {
        super.onCreate()
        val platformGraph = createGraphFactory<AndroidAppGraph.Factory>().create(this)
        appGraph = createGraphFactory<AppGraph.Factory>().create(platformGraph)
    }
}