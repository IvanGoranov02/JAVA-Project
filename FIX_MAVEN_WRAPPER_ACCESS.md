# Как да поправиш AccessDeniedException за Maven Wrapper

## Проблем
Грешка: `AccessDeniedException: C:\Users\FAMILY\JAVA-Project2\.mvn\wrapper`

Това е проблем с правата за достъп до Maven wrapper папката.

---

## ✅ Решение 1: Използвай Application конфигурация (НАЙ-ЛЕСНО)

Вместо Maven конфигурация, използвай Application конфигурация:

### Стъпка 1: Създай Application конфигурация
1. **Run → Edit Configurations...**
2. Кликни **+** → избери **Application**
3. Настрой:
   - **Name**: `ShoeWarehouseApplication`
   - **Main class**: `com.shoewarehouse.ShoeWarehouseApplication`
   - **Module**: `shoe-warehouse`
   - **Working directory**: `C:\Users\FAMILY\JAVA-Project2`
4. Кликни **OK**

### Стъпка 2: Компилирай проекта първо
1. **Build → Build Project** (`Ctrl+F9`)
2. Изчакай да завърши
3. Провери дали има `target/classes` папка

### Стъпка 3: Стартирай
Кликни **Run** (зелената стрелка)

---

## ✅ Решение 2: Поправи правата на .mvn папката

### Стъпка 1: Отвори PowerShell като Администратор
1. Намери PowerShell в Start менюто
2. Кликни десен бутон → **Run as Administrator**

### Стъпка 2: Поправи правата
```powershell
cd C:\Users\FAMILY\JAVA-Project2
icacls ".mvn" /grant "${env:USERNAME}:(OI)(CI)F" /T
```

Или:
```powershell
$path = "C:\Users\FAMILY\JAVA-Project2\.mvn"
$acl = Get-Acl $path
$permission = "${env:USERNAME}","FullControl","ContainerInherit,ObjectInherit","None","Allow"
$accessRule = New-Object System.Security.AccessControl.FileSystemAccessRule $permission
$acl.SetAccessRule($accessRule)
Set-Acl $path $acl
```

---

## ✅ Решение 3: Използвай директно Maven (ако е инсталиран)

Ако имаш Maven инсталиран глобално:

1. **Run → Edit Configurations...**
2. Създай нова **Maven** конфигурация
3. В **Command line** напиши: `spring-boot:run`
4. Но използвай директно `mvn` вместо `mvnw.cmd`

---

## ✅ Решение 4: Изтрий и регенерирай .mvn папката

### Стъпка 1: Затвори IntelliJ

### Стъпка 2: Изтрий .mvn папката
```powershell
cd C:\Users\FAMILY\JAVA-Project2
Remove-Item -Recurse -Force .mvn -ErrorAction SilentlyContinue
```

### Стъпка 3: Регенерирай Maven wrapper
```powershell
mvn wrapper:wrapper
```

Или ако нямаш Maven:
- Копирай `.mvn` папката от друг проект
- Или използвай Application конфигурация (Решение 1)

---

## 💡 Препоръка

**Използвай Решение 1 (Application конфигурация)** - това е най-лесно и не зависи от Maven wrapper правата.

---

## След поправка

След като стартираш приложението, ще видиш:
```
Started ShoeWarehouseApplication in X.XXX seconds
Tomcat started on port(s): 8080
```

И можеш да тестваш в Postman на `http://localhost:8080/api/brands`
