package com.github.sapfirmoscow.kmpchat.data.local

/**
 * Контракт зашифрованного хранилища пар ключ-значение.
 * Реализации обязаны обеспечивать конфиденциальность данных на уровне платформы.
 */
interface EncryptedStorage {
    /**
     * Сохраняет строковое значение под указанным ключом.
     *
     * @param key   Ключ записи.
     * @param value Сохраняемое значение.
     */
    fun saveString(key: String, value: String)

    /**
     * Читает строковое значение по ключу.
     *
     * @param key Ключ записи.
     * @return Сохранённое значение или `null`, если запись отсутствует.
     */
    fun getString(key: String): String?

    /**
     * Удаляет запись по ключу. Ничего не делает, если ключ отсутствует.
     *
     * @param key Ключ удаляемой записи.
     */
    fun deleteString(key: String)
}