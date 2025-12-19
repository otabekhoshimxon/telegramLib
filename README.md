# 📦 Base Lib – Telegram Logging & Utilities

**Base Lib** is a shared Java library that provides common utilities and a ready-to-use **Telegram integration** for Spring Boot applications, including **Telegram-based logging via Logback**.

---

## ✨ Features

- ✅ Telegram bot integration
- ✅ Telegram Logback appender (send logs to Telegram)
- ✅ Support for Telegram groups and topics (forum threads)
- ✅ Configurable log levels (ERROR, WARN, INFO)
- ✅ Optional stack trace forwarding
- ✅ Rate limiting and async logging
- ✅ Spring Boot auto-configuration
- ✅ Common enums and web utilities

---

## 📦 Installation

### Maven

```xml
<dependency>
  <groupId>io.github.otabekhoshimxon</groupId>
  <artifactId>base-lib</artifactId>
  <version>1.1.0</version>
</dependency>


```

```yml
telegram:
  bot:
    enabled: true
    token: YOUR_TELEGRAM_BOT_TOKEN

  group:
    id: "-1001234567890"
    topicId: "10"              # optional (Telegram forum topic ID)
    errorGroupId: "-1009876543210"
    reportGroupId: "-1001122334455"
    reportGroupTopicId: 5

  logging:
    enabled: true
    level: ERROR,WARN
    includeStackTrace: true
    maxStackTraceLines: 5
    async: true
    queueSize: 1000

  message:
    maxLength: 4000
    parseMode: HTML
    disableWebPagePreview: true
    disableNotification: false
    rateLimit: 30

```