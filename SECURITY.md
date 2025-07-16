# Рекомендации по безопасности

## JWT Токены

### Текущие меры защиты:
- ✅ HMAC-SHA256 подпись с секретным ключом
- ✅ Проверка подписи в Gateway
- ✅ Проверка срока действия (exp)
- ✅ Время жизни токена: 1 час

### Рекомендации для продакшена:

#### 1. **Секретный ключ**
```yaml
# Использовать переменные окружения
jwt:
  secret: ${JWT_SECRET:your-super-secret-key-here}
```

#### 2. **HTTPS**
```yaml
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: ${SSL_KEYSTORE_PASSWORD}
```

#### 3. **Refresh токены**
```java
// Добавить refresh токены для безопасного обновления
@PostMapping("/refresh")
public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
    // Валидация refresh токена
    // Генерация нового access токена
}
```

#### 4. **Rate Limiting**
```java
// Добавить ограничение запросов
@RateLimiter(name = "auth")
@PostMapping("/login")
public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    // ...
}
```

#### 5. **CORS настройки**
```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://yourdomain.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        // ...
    }
}
```

#### 6. **Логирование безопасности**
```yaml
logging:
  level:
    org.springframework.security: INFO
    org.example.security: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```

#### 7. **Заголовки безопасности**
```java
@Configuration
public class SecurityHeadersConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers
                .frameOptions().deny()
                .contentTypeOptions().and()
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .maxAgeInSeconds(31536000)
                    .includeSubdomains(true)
                )
            );
        return http.build();
    }
}
```

## Защита от атак

### 1. **SQL Injection**
- ✅ Используется JPA/Hibernate (защищено)
- ✅ Параметризованные запросы

### 2. **XSS**
- Использовать httpOnly cookies для JWT
- CSP заголовки
- Валидация входных данных

### 3. **CSRF**
- ✅ Отключено для API (stateless)
- Для веб-приложений: включить CSRF защиту

### 4. **Brute Force**
- Rate limiting на /api/auth/**
- Account lockout после неудачных попыток
- CAPTCHA для веб-форм

## Мониторинг

### 1. **Логирование**
```yaml
# Логировать попытки аутентификации
logging:
  level:
    org.example.security.service.AuthService: DEBUG
```

### 2. **Метрики**
```java
// Добавить метрики для мониторинга
@Component
public class SecurityMetrics {
    private final MeterRegistry meterRegistry;
    
    public void recordLoginAttempt(String username, boolean success) {
        meterRegistry.counter("auth.login.attempts", 
            "username", username, 
            "success", String.valueOf(success)
        ).increment();
    }
}
```

## Рекомендации по развертыванию

1. **Переменные окружения** для всех секретов
2. **Docker secrets** для контейнеризации
3. **Vault** для управления секретами
4. **Регулярное обновление** зависимостей
5. **Сканирование уязвимостей** (OWASP ZAP, Snyk)
6. **Backup** базы данных
7. **Мониторинг** и алерты

## Тестирование безопасности

```bash
# Тестирование JWT
curl -X POST http://localhost:8083/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "test", "password": "test"}'

# Тестирование с невалидным токеном
curl -X GET http://localhost:8083/api/users/profile \
  -H "Authorization: Bearer invalid.token.here"

# Тестирование без токена
curl -X GET http://localhost:8083/api/users/profile
``` 