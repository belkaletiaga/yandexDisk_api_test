# Автотесты для Yandex Disk REST API
*Пример проекта автотестов для сервиса **Яндекс.Диск**(GET/POST/PUT/DELETE) *
*Тестовое задание на позицию Стажер "Инженер по автоматизации тестирования" в Финтех*

Используется публичный REST API: `https://cloud-api.yandex.net`

## Стек
- Java 25
- Gradle
- JUnit 5
- Rest Assured
- Lombok
- Allure Report

## Тест-кейсы

Подробное описание — в файле [TEST_CASES.md](TEST_CASES.md).

| TC ID | Описание | Метод | Эндпоинт        |
|-------|----------|-------|-----------------|
| TC-01 | Получение информации о диске | GET | `/v1/disk`|
| TC-02 | Запрос без токена | GET | `/v1/disk`      |
| TC-03 | Создание папки | PUT | `/v1/disk/resources`|
| TC-04 | Получение метаинформации папки | GET | `/v1/disk/resources`|
| TC-05 | Обновление custom_properties | PATCH | `/v1/disk/resources`|
| TC-06 | Копирование папки | POST | `/v1/disk/resources/copy`|
| TC-07 | Создание существующей папки | PUT | `/v1/disk/resources`|
| TC-08 | Удаление папки | DELETE | `/v1/disk/resources`|

## 1. Получение OAuth-токен

Получить токен можно следующим образом:
1. Перейдите на [полигон Яндекс.Диска](https://yandex.ru/dev/disk/poligon/) и получите готовый токен
2. Или создайте тестовый аккаунт и получите токен по [инструкции](https://yandex.ru/dev/disk-api/doc/ru/concepts/quickstart#quickstart__oauth).

**Не используйте свой основной аккаунт Яндекса.**

## 2. Установка и запуск тестов

### 1. Клонировать репозиторий
```bash
git clone https://github.com/belkaletiaga/yandexDisk.git 
```
### 2. Установить токен (один из вариантов):

Через файл src/test/resources/default.properties: 
oauthToken=ваш_токен

### 3. Запустить тесты

```bash
#Запуск всех тестов
./gradlew test

#Запуск конкретного тестового класса
./gradlew test --tests ResourceTest
```
## 3. Результат запуска

Cгенерировать отчет:
```bash
./gradlew clean test allureReport
# Открыть отчёт в браузере:
./gradlew allureServe
```
