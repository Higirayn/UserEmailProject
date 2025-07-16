# API Gateway

API Gateway для проекта UserEmailProject с интеграцией Security модуля.

## Функциональность

- Проксирование запросов к различным микросервисам
- **Делегирование JWT аутентификации** к Security Service
- Маршрутизация запросов на основе путей

## Архитектура аутентификации

```
1. Клиент → Gateway (8083) → Security Service (8084)
   POST /api/auth/login
   
2. Security Service возвращает JWT токен
   
3. Клиент → Gateway (8083) → Security Service (8084) [валидация]
   GET /api/users/profile
   Authorization: Bearer <token>
   
4. Gateway проверяет токен через HTTP запрос к Security Service
   POST /api/auth/validate
   
5. Если токен валиден → Gateway проксирует запрос к Security Service
   Если токен невалиден → Gateway возвращает 401
```

## Преимущества нового подхода

- ✅ **Централизованная валидация** - только Security Service знает секрет
- ✅ **Безопасность** - Gateway не хранит секреты JWT
- ✅ **Гибкость** - можно легко изменить логику валидации
- ✅ **Мониторинг** - все попытки аутентификации логируются в Security Service

## Маршруты

### Публичные маршруты (без JWT проверки)

- `/gateway/**` → API Gateway (тестовые эндпоинты)
- `/api/auth/**` → Security Module (8084) - регистрация и аутентификация

### Защищенные маршруты (требуют JWT токен)

- `/api/users/**` → Security Module (8084) - управление пользователями
- `/user-module/**` → User Module (8080) - бизнес-логика пользователей
- `/analytics/**` → Analytics Module (8082) - аналитика

## JWT Аутентификация

Gateway **делегирует проверку JWT** токенов Security Service:

1. **Извлечение токена**: `Authorization: Bearer <token>`
2. **HTTP запрос к Security Service**: `POST /api/auth/validate`
3. **Валидация**: Security Service проверяет подпись, срок действия, пользователя
4. **Ответ**: `{"valid": true, "username": "user"}` или `{"valid": false, "username": null}`

### Примеры использования

#### Регистрация пользователя (публичный)
```bash
curl -X POST http://localhost:8083/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'
```

#### Вход в систему (публичный)
```bash
curl -X POST http://localhost:8083/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### Доступ к защищенному ресурсу
```bash
curl -X GET http://localhost:8083/api/users/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Тест gateway
```bash
curl -X GET http://localhost:8083/gateway/health
```

## Конфигурация

Основные настройки в `config/api-gateway.yml`:

- `server.port`: 8083
- `security.service.url`: http://localhost:8084 (URL Security Service)
- Маршруты настроены для проксирования на соответствующие сервисы

## Запуск

```bash
./gradlew :api-gateway:bootRun
```

Или через IDE запустить класс `ApiGateway`.

## Архитектура

```
Client → API Gateway (8083) → Security Service (8084) [JWT валидация]
                           → User Module (8080)
                           → Analytics Module (8082)
```

Gateway обеспечивает:
- Единую точку входа для всех клиентов
- **Делегирование аутентификации** к Security Service
- Маршрутизацию запросов к микросервисам
- **Безопасность** - не хранит секреты JWT 