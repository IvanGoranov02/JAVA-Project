# Как да поправиш Java версията

## ⚠️ Проблем
Грешка: `Cannot compile module 'shoe-warehouse' configured for JVM target 25: the JDK Oracle OpenJDK 21.0.9`

Проектът е настроен за **Java 25**, но имаш **Java 21**!

---

## ✅ Решение: Промени Java версията в pom.xml

### Стъпка 1: Поправи pom.xml

Аз вече поправих pom.xml за теб! Променил съм:
- `java.version` от 25 на 21
- `maven.compiler.source` от 25 на 21  
- `maven.compiler.target` от 25 на 21

### Стъпка 2: Реиндексирай проекта в IntelliJ

1. **File → Invalidate Caches...**
2. Избери **Invalidate and Restart**
3. След рестарт, изчакай IntelliJ да реиндексира

### Стъпка 3: Reload Maven проекта

1. Отвори **Maven** tool window (View → Tool Windows → Maven)
2. Кликни на иконата за **Reload All Maven Projects** (двойна стрелка)
3. Или: кликни десен бутон на `pom.xml` → **Maven → Reload Project**

### Стъпка 4: Избери правилния модул в Project Structure

1. **File → Project Structure** (`Ctrl+Alt+Shift+S`)
2. Отиди на **Modules**
3. В средния панел, **кликни на `shoe-warehouse`** (не на `maven-wrapper`!)
4. Кликни **Apply** и **OK**

### Стъпка 5: Build проекта

1. **Build → Rebuild Project**
2. Изчакай да завърши
3. Провери дали има `target/classes` папка

---

## След поправката

След като направиш всичко:

1. Build → Build Project (`Ctrl+F9`)
2. Провери дали няма грешки
3. Опитай да стартираш приложението:
   - Отвори `ShoeWarehouseApplication.java`
   - Кликни зелената стрелка (▶) до `main()` метода

---

## Проверка

След Build, трябва да видиш:
- ✅ Няма грешки в Build Output
- ✅ Има `target/classes` папка
- ✅ Можеш да стартираш приложението

---

## Запомни

**Java 21** е достатъчна за Spring Boot 3.2.0! Не ти трябва Java 25.
