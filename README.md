### Hexlet tests and linter status:
[![Actions Status](https://github.com/pavelchervonenko/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/pavelchervonenko/java-project-72/actions) [![Java CI](https://github.com/pavelchervonenko/java-project-72/actions/workflows/main.yml/badge.svg)](https://github.com/pavelchervonenko/java-project-72/actions/workflows/main.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pavelchervonenko_java-project-72&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pavelchervonenko_java-project-72)

# Page Analyzer

Page Analyzer — веб-приложение для анализа веб-страниц.
Сервис позволяет сохранять сайты, выполнять их проверку и получать основную техническую информацию
о доступности и HTML-структуре страницы.

🌐 **Рабочая версия приложения:**  
👉 https://java-project-72-igkr.onrender.com

(размещено в облаке, работает без установки)

## Возможности

- Добавление сайтов для анализа
- Проверка доступности сайта по HTTP
- Получение:
    - HTTP-статус кода
    - тега `<title>`
    - заголовка `<h1>`
    - meta-описания (`description`)
- Хранение истории проверок
- Отображение результатов в удобном интерфейсе

## Технологии

Проект реализован на Java с использованием:

- **Javalin** — web-фреймворк
- **JTE** — шаблонизатор HTML
- **H2 / PostgreSQL** — база данных
- **HikariCP** — пул соединений
- **SLF4J** — логгирование
- **JUnit 5** — тестирование
- **MockWebServer** — тестирование HTTP-ответов
- **SonarQube** — анализ качества кода
