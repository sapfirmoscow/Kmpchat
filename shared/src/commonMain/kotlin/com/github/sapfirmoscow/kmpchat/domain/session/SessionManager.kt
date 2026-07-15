package com.github.sapfirmoscow.kmpchat.domain.session

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Оповещает приложение о том, что сессия перестала быть действительной.
 *
 * Нужен, чтобы слой данных не зависел от навигации: HTTP-клиент, получив 401,
 * лишь сообщает о факте через [notifyExpired], а решение «разлогинить и увести
 * на экран входа» принимает корневой Composable.
 *
 * Типичный сценарий: сервер хранит данные в памяти, после его рестарта
 * пользователь исчезает, а сохранённый токен на клиенте остаётся — и любой
 * запрос начинает отвечать 401.
 */
@SingleIn(AppScope::class)
internal class SessionManager @Inject constructor() {

    // replay = 0: событие интересно только тому, кто слушает сейчас.
    // extraBufferCapacity = 1, чтобы tryEmit не терял событие из не-suspend кода.
    private val _expired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    /** Поток событий «сессия истекла». */
    val expired: SharedFlow<Unit> = _expired

    /** Сообщает, что текущая сессия отвергнута сервером. */
    fun notifyExpired() {
        _expired.tryEmit(Unit)
    }
}
