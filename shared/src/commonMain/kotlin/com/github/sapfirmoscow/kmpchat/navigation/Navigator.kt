package com.github.sapfirmoscow.kmpchat.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

/**
 * Сущность, инкапсулирующая работу с навигационным бэкстеком.
 *
 * Единственный экземпляр на граф ([SingleIn]); инжектится во ViewModel'и,
 * поэтому экраны навигируют через свои ViewModel без проброса колбэков.
 *
 * Поддерживает условную навигацию: при попытке перейти на маршрут с
 * [Routes.requiresLogin] = `true`, когда пользователь не авторизован,
 * вместо него в стек добавляется [Routes.Auth].
 *
 * Бэкстек создаётся напрямую, поэтому переживает смену конфигурации
 * (граф живёт в `Application`), но не смерть процесса.
 *
 * @param jwtTokenRepository Репозиторий токена; источник статуса авторизации.
 */
@SingleIn(AppScope::class)
internal class Navigator @Inject constructor(
    private val jwtTokenRepository: JWTTokenRepository,
) {
    /** Бэкстек, наблюдаемый `NavDisplay`. Стартовый маршрут зависит от наличия токена. */
    val backStack: NavBackStack<NavKey> = NavBackStack(
        if (isLoggedIn()) Routes.ChatsList else Routes.Auth
    )

    private fun isLoggedIn(): Boolean = jwtTokenRepository.getToken() != null

    /**
     * Переходит на указанный маршрут.
     *
     * Если маршрут требует авторизации, а пользователь не вошёл в систему,
     * вместо него открывается [Routes.Auth].
     */
    fun navigate(key: Routes) {
        if (key.requiresLogin && !isLoggedIn()) {
            backStack.add(Routes.Auth)
        } else {
            backStack.add(key)
        }
    }

    /** Возврат на предыдущий маршрут. */
    fun goBack() {
        backStack.removeLastOrNull()
    }

    /** Очищает стек и делает [key] единственным маршрутом. */
    fun replaceAll(key: Routes) {
        backStack.clear()
        backStack.add(key)
    }
}
