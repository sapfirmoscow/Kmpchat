package com.github.sapfirmoscow.kmpchat.domain.validation

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CredentialsValidatorTest {

    @Test
    fun validRegistrationPasses() {
        assertNull(CredentialsValidator.validateRegistration("alice_1.x", "secret1"))
    }

    @Test
    fun usernameIsNormalizedBeforeCheck() {
        assertNull(CredentialsValidator.validateRegistration("  Alice ", "secret1"))
    }

    @Test
    fun usernameLengthIsChecked() {
        assertNotNull(CredentialsValidator.validateRegistration("ab", "secret1"))
        assertNotNull(CredentialsValidator.validateRegistration("a".repeat(33), "secret1"))
        assertNull(CredentialsValidator.validateRegistration("a".repeat(32), "secret1"))
    }

    @Test
    fun nonLatinUsernameIsRejected() {
        // Кириллическая «а» — визуально не отличима от латинской.
        assertNotNull(CredentialsValidator.validateRegistration("аlice", "secret1"))
        assertNotNull(CredentialsValidator.validateRegistration("al ice", "secret1"))
        assertNotNull(CredentialsValidator.validateRegistration("al-ice", "secret1"))
    }

    @Test
    fun shortPasswordIsRejected() {
        assertNotNull(CredentialsValidator.validateRegistration("alice", "12345"))
        // 6 символов кириллицы — это 12 байт, но минимум считается в символах.
        assertNull(CredentialsValidator.validateRegistration("alice", "пароль"))
    }

    @Test
    fun passwordLimitIsInBytes() {
        assertNull(CredentialsValidator.validateRegistration("alice", "a".repeat(72)))
        assertNotNull(CredentialsValidator.validateRegistration("alice", "a".repeat(73)))
        // 37 символов кириллицы = 74 байта.
        assertNotNull(CredentialsValidator.validateRegistration("alice", "ж".repeat(37)))
    }

    @Test
    fun loginDoesNotCheckUsernameCharset() {
        assertNull(CredentialsValidator.validateLogin("старый_ник", "x"))
        assertNotNull(CredentialsValidator.validateLogin("  ", "secret1"))
        assertNotNull(CredentialsValidator.validateLogin("alice", ""))
    }
}
