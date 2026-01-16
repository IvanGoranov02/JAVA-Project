# Инструкции за инсталация на Java и настройка на JAVA_HOME

## Проблем
Грешка: `JAVA_HOME not found in your environment`

## Решение

### Стъпка 1: Инсталиране на Java

Имате няколко опции:

#### Опция A: Инсталиране на Java 17/21 (Препоръчително за Spring Boot 3.2.0)

1. **Свалете Java:**
   - Отидете на: https://adoptium.net/ (Eclipse Temurin) ИЛИ
   - Отидете на: https://www.oracle.com/java/technologies/downloads/
   - Изберете **JDK 17** или **JDK 21** (LTS версии)
   - Изберете **Windows x64 Installer**

2. **Инсталирайте Java:**
   - Стартирайте инсталатора
   - Следвайте стъпките и запомнете къде е инсталиран (обикновено `C:\Program Files\Java\jdk-17` или `C:\Program Files\Eclipse Adoptium\jdk-17.0.x`)

#### Опция B: Използване на Chocolatey (бързо)

Ако имате Chocolatey инсталиран:
```powershell
choco install openjdk17
```

#### Опция C: Използване на winget (Windows Package Manager)

```powershell
winget install Microsoft.OpenJDK.17
```

---

### Стъпка 2: Настройка на JAVA_HOME

#### Метод 1: Чрез PowerShell (временно за текущата сесия)

```powershell
# Намерете пътя към Java (пример)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Проверете
java -version
```

#### Метод 2: Постоянна настройка (препоръчително)

1. **Намерете пътя към Java:**
   - Отидете в папката където е инсталиран Java
   - Копирайте пълния път (пример: `C:\Program Files\Eclipse Adoptium\jdk-17.0.9.9-hotspot`)

2. **Задайте JAVA_HOME чрез GUI:**
   - Натиснете `Win + R`
   - Напишете: `sysdm.cpl` и натиснете Enter
   - Отидете на таб **Advanced**
   - Кликнете **Environment Variables**
   - Под **System variables** кликнете **New**
   - Variable name: `JAVA_HOME`
   - Variable value: пътят към Java (пример: `C:\Program Files\Eclipse Adoptium\jdk-17.0.9.9-hotspot`)
   - Кликнете **OK**

3. **Добавете Java към PATH:**
   - В същия прозорец, намерете променливата **Path** под **System variables**
   - Кликнете **Edit**
   - Кликнете **New**
   - Добавете: `%JAVA_HOME%\bin`
   - Кликнете **OK** на всички прозорци

4. **Рестартирайте терминала/PowerShell**

#### Метод 3: Чрез PowerShell като администратор (бързо)

```powershell
# Заместите с вашия път към Java
$javaPath = "C:\Program Files\Eclipse Adoptium\jdk-17.0.9.9-hotspot"

# Задайте JAVA_HOME системна променлива
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", $javaPath, [System.EnvironmentVariableTarget]::Machine)

# Добавете към PATH
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::Machine)
$newPath = "$javaPath\bin;$currentPath"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, [System.EnvironmentVariableTarget]::Machine)
```

**Важно:** Трябва да изпълните PowerShell като администратор!

---

### Стъпка 3: Проверка

След настройка, отворете **нов** терминал и проверете:

```powershell
java -version
echo $env:JAVA_HOME
```

Трябва да видите нещо като:
```
openjdk version "17.0.9" 2024-01-16
OpenJDK Runtime Environment Temurin-17.0.9+9 (build 17.0.9+9)
OpenJDK 64-Bit Server VM Temurin-17.0.9+9 (build 17.0.9+9, mixed mode, sharing)
```

---

### Стъпка 4: Стартиране на приложението

След като Java е инсталиран и JAVA_HOME е настроен:

```powershell
cd C:\Users\FAMILY\JAVA-Project
.\mvnw.cmd spring-boot:run
```

---

## Автоматично намиране на Java (ако е инсталиран)

Ако смятате, че Java вече е инсталиран, но не е в PATH, опитайте:

```powershell
# Проверете в стандартните места
Get-ChildItem "C:\Program Files\Java" -ErrorAction SilentlyContinue
Get-ChildItem "C:\Program Files\Eclipse Adoptium" -ErrorAction SilentlyContinue
Get-ChildItem "C:\Program Files\Microsoft" -Filter "*jdk*" -Directory -ErrorAction SilentlyContinue
```

---

## Забележка

- Spring Boot 3.2.0 изисква **Java 17 или по-нова версия**
- Препоръчително е да използвате **Java 17 LTS** или **Java 21 LTS**
- След промяна на системни променливи, **задължително рестартирайте терминала**
