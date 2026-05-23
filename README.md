# KmpChat

Kotlin Multiplatform чат-приложение для Android и Desktop (JVM).

## Сборка и запуск

**Android:**
```
./gradlew :androidApp:assembleDebug
```

**Desktop:**
```
./gradlew :desktopApp:run
```

## Стек

- Kotlin Multiplatform + Compose Multiplatform
- Ktor (HTTP клиент)
- Kotlin Serialization