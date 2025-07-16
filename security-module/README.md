# Security Module

Модуль безопасности с JWT аутентификацией для проекта UserEmailProject.

## Функциональность

- Регистрация пользователей
- Аутентификация с JWT токенами
- Управление ролями пользователей (USER, ADMIN, MODERATOR)
- Защищенные эндпоинты
- H2 база данных для разработки

## Эндпоинты

### Публичные эндпоинты (не требуют аутентификации)

- `POST /api/auth/register` - Регистрация нового пользователя
- `POST /api/auth/login` - Вход в систему
- `GET /api/auth/public/health` - Проверка состояния сервиса
- `GET /h2-console` - H2 консоль базы данных

### Защищенные эндпоинты (требуют JWT токен)

- `GET /api/users/profile` - Получение профиля текущего пользователя
- `GET /api/users/all` - Получение списка всех пользователей
- `GET /api/users/{id}` - Получение пользователя по ID

## Использование

### Регистрация пользователя

```bash
curl -X POST http://localhost:8084/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'
```

### Вход в систему

```bash
curl -X POST http://localhost:8084/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Использование защищенных эндпоинтов

```bash
curl -X GET http://localhost:8084/api/users/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Конфигурация

Основные настройки в `application.yml`:

- `server.port`: 8084
- `jwt.secret`: Секретный ключ для подписи JWT
- `jwt.expiration`: Время жизни токена (24 часа)

## Запуск

```bash
./gradlew :security-module:bootRun
```

Или через IDE запустить класс `SecurityModuleApplication`. 