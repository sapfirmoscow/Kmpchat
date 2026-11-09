package com.github.sapfirmoscow.kmpchat.domain.validation

/**
 * Проверка логина и пароля до отправки на сервер.
 *
 * Правила повторяют серверные (`internal/handlers/validation.go`), чтобы
 * пользователь видел понятную ошибку сразу, без запроса и без траты лимита
 * попыток (`/register` и `/login` ограничены 10 запросами в минуту с IP).
 */
internal object CredentialsValidator {

    const val MIN_USERNAME_LENGTH = 3
    const val MAX_USERNAME_LENGTH = 32
    const val MIN_PASSWORD_LENGTH = 6

    /** Предел bcrypt на сервере — считается в байтах UTF-8, а не в символах. */
    const val MAX_PASSWORD_BYTES = 72

    private val usernameRegex = Regex("^[a-z0-9_.]{$MIN_USERNAME_LENGTH,$MAX_USERNAME_LENGTH}$")

    /** Приводит username к виду, в котором его хранит сервер. */
    fun normalizeUsername(username: String): String = username.trim().lowercase()

    /**
     * Проверяет данные для регистрации.
     *
     * @return Текст ошибки для пользователя или `null`, если всё корректно.
     */
    fun validateRegistration(username: String, password: String): String? =
        validateUsername(normalizeUsername(username)) ?: validatePassword(password)

    /**
     * Проверяет данные для входа.
     *
     * Набор символов username здесь не проверяется: пользователи, зарегистрированные
     * до введения правил, должны иметь возможность войти.
     *
     * @return Текст ошибки для пользователя или `null`, если всё корректно.
     */
    fun validateLogin(username: String, password: String): String? = when {
        normalizeUsername(username).isEmpty() -> "Введите имя пользователя"
        password.isEmpty() -> "Введите пароль"
        else -> null
    }

    private fun validateUsername(username: String): String? = when {
        username.length !in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH ->
            "Имя пользователя должно быть от $MIN_USERNAME_LENGTH до $MAX_USERNAME_LENGTH символов"
        !usernameRegex.matches(username) ->
            "Имя пользователя может содержать только латинские буквы, цифры, «_» и «.»"
        else -> null
    }

    private fun validatePassword(password: String): String? = when {
        password.codePointCount() < MIN_PASSWORD_LENGTH ->
            "Пароль должен быть не короче $MIN_PASSWORD_LENGTH символов"
        password.encodeToByteArray().size > MAX_PASSWORD_BYTES ->
            "Пароль слишком длинный (не больше $MAX_PASSWORD_BYTES байт; кириллица занимает 2 байта на символ)"
        else -> null
    }

    /** Число символов Unicode — так длину считает сервер (`utf8.RuneCountInString`). */
    private fun String.codePointCount(): Int {
        var count = 0
        var i = 0
        while (i < length) {
            i += if (this[i].isHighSurrogate() && i + 1 < length && this[i + 1].isLowSurrogate()) 2 else 1
            count++
        }
        return count
    }
}
