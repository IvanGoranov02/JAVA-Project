# Стъпки след инсталация на Java

## Стъпка 1: Рестартирайте терминала
**ВАЖНО:** След инсталация на Java, рестартирайте терминала/PowerShell, за да се обновят променливите на средата.

---

## Стъпка 2: Намерете къде е инсталиран Java

Отворете File Explorer и проверете:
- `C:\Program Files\Java\`
- `C:\Program Files\Eclipse Adoptium\`
- `C:\Program Files\Microsoft\`

Запишете пълния път (пример: `C:\Program Files\Eclipse Adoptium\jdk-17.0.9-hotspot`)

---

## Стъпка 3: Настройте JAVA_HOME

### Опция A: Чрез GUI (Препоръчително)

1. Натиснете `Win + R`
2. Напишете: `sysdm.cpl` и натиснете Enter
3. Отидете на таб **Advanced**
4. Кликнете **Environment Variables**
5. Под **System variables** кликнете **New**:
   - Variable name: `JAVA_HOME`
   - Variable value: пътя към Java (пример: `C:\Program Files\Eclipse Adoptium\jdk-17.0.9-hotspot`)
6. Редактирайте **Path** променливата и добавете: `%JAVA_HOME%\bin`
7. Кликнете **OK** на всички прозорци

### Опция B: Чрез PowerShell (като администратор)

Отворете PowerShell като **Администратор** и изпълнете:

```powershell
# Заменете с вашия път към Java
$javaPath = "C:\Program Files\Eclipse Adoptium\jdk-17.0.9-hotspot"

# Задайте JAVA_HOME
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", $javaPath, [System.EnvironmentVariableTarget]::Machine)

# Добавете към PATH
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::Machine)
$newPath = "$javaPath\bin;$currentPath"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, [System.EnvironmentVariableTarget]::Machine)
```

**ВАЖНО:** След това рестартирайте терминала!

---

## Стъпка 4: Проверете дали работи

Отворете **нов** терминал и изпълнете:

```powershell
java -version
echo $env:JAVA_HOME
```

Трябва да видите версията на Java и пътя към JAVA_HOME.

---

## Стъпка 5: Стартирайте приложението

След като Java е настроен правилно:

```powershell
cd C:\Users\FAMILY\JAVA-Project
.\mvnw.cmd spring-boot:run
```

---

## Ако все още не работи

1. Рестартирайте компютъра (това гарантира обновяване на променливите)
2. Проверете дали Java е инсталиран правилно
3. Уверете се, че добавяте правилния път към JAVA_HOME
