# Инструкции за стартиране на Shoe Warehouse приложението

## Начин 1: С Maven Wrapper (Препоръчително)

Отвори терминал в папката на проекта и изпълни:

```bash
.\mvnw.cmd spring-boot:run
```

Или с PowerShell:
```powershell
.\mvnw.cmd spring-boot:run
```

## Начин 2: С компилиране и стартиране на JAR

```bash
.\mvnw.cmd clean package
java -jar target\shoe-warehouse-1.0.0.jar
```

## Начин 3: С IDE (IntelliJ IDEA / Eclipse)

1. Отвори проекта в IDE
2. Намери `ShoeWarehouseApplication.java`
3. Кликни десен бутон -> Run 'ShoeWarehouseApplication.main()'

## Проверка

След стартиране, приложението ще работи на:
- **Base URL**: `http://localhost:8080`
- **H2 Console**: `http://localhost:8080/h2-console`
