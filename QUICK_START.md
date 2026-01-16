# Бърз старт - Как да стартирате приложението

## ✅ Java е настроен!
Вашият Java е на: `C:\Program Files\Java\jdk-21`

---

## 🚀 Начини за стартиране:

### Метод 1: Чрез IDE (НАЙ-ЛЕСНО) ⭐

1. **Отвори проекта в IntelliJ IDEA или Eclipse**
2. **Намери файла:** `main/java/com/shoewarehouse/ShoeWarehouseApplication.java`
3. **Кликни десен бутон → Run 'ShoeWarehouseApplication.main()'**

IDE-то ще настрои всичко автоматично!

---

### Метод 2: Чрез PowerShell (с настройка за текущата сесия)

Отвори PowerShell в папката на проекта и изпълни:

```powershell
# Настройка на Java за текущата сесия
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:PATH = "C:\Program Files\Java\jdk-21\bin;$env:PATH"

# Стартиране на приложението
cd C:\Users\FAMILY\JAVA-Project
.\mvnw.cmd spring-boot:run
```

**Ако има проблем с правата**, опитай да стартираш PowerShell като **Администратор**.

---

### Метод 3: Компилиране и стартиране на JAR

```powershell
# Настройка на Java
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:PATH = "C:\Program Files\Java\jdk-21\bin;$env:PATH"

# Компилиране (ако Maven wrapper работи)
.\mvnw.cmd clean package

# Стартиране на JAR
java -jar target\shoe-warehouse-1.0.0.jar
```

---

### Метод 4: Постоянна настройка на JAVA_HOME (Препоръчително за бъдеще)

За да не настройваш Java всеки път:

1. Натисни `Win + R`
2. Напиши: `sysdm.cpl` → Enter
3. Advanced → Environment Variables
4. System variables → New:
   - Name: `JAVA_HOME`
   - Value: `C:\Program Files\Java\jdk-21`
5. Редактирай **Path** → New → Добави: `%JAVA_HOME%\bin`
6. OK на всичко и **рестартирай терминала**

---

## 🔍 Проверка дали работи

След като приложението стартира, отиди на:

```
http://localhost:8080/api/brands
```

В Postman:
- **Method**: GET
- **URL**: `http://localhost:8080/api/brands`
- **Authorization**: Basic Auth
  - Username: `admin`
  - Password: `admin123`

---

## 📋 Всички endpoints:

**Base URL:** `http://localhost:8080`

- Brands: `/api/brands`
- Categories: `/api/categories`
- Shoes: `/api/shoes`
- Suppliers: `/api/suppliers`
- Orders: `/api/orders`

Виж `POSTMAN_ENDPOINTS.md` за пълния списък!

---

## ❓ Проблеми?

### Ако има грешка с правата на Maven wrapper:
- Стартирай PowerShell като **Администратор**
- Или използвай IDE (IntelliJ IDEA / Eclipse)

### Ако приложението не стартира:
- Провери дали порт 8080 е свободен
- Провери дали Java е правилно настроен: `java -version`
- Виж логовете за грешки
