# Как да поправиш "Could not find or load main class" в IntelliJ IDEA

## Проблем
Грешка: `ClassNotFoundException: com.shoewarehouse.ShoeWarehouseApplication`

Това означава че проектът не е компилиран или source папките не са правилно настроени.

---

## Решение 1: Компилирай проекта първо (ВАЖНО!)

### Стъпка 1: Компилирай с Maven

В IntelliJ:
1. Отвори **Maven** tool window (View → Tool Windows → Maven)
2. Разгъни проекта: `shoe-warehouse` → **Lifecycle**
3. Двойно кликни на **compile** (или **clean compile**)
4. Изчакай да завърши компилирането (ще видиш логове отдолу)

### Стъпка 2: Провери дали има target папка

След компилиране, трябва да видиш папка `target` в проекта с:
- `target/classes/com/shoewarehouse/ShoeWarehouseApplication.class`

---

## Решение 2: Настрой Source Folders в IntelliJ

### Стъпка 1: Отвори Project Structure
1. **File → Project Structure** (или `Ctrl+Alt+Shift+S`)
2. Отиди на **Modules**

### Стъпка 2: Маркирай Source папките
1. Избери модула `shoe-warehouse`
2. Под **Sources**, виж папките:
   - `main/java` → трябва да е **маркирана като Sources** (синя папка)
   - `test/java` → трябва да е **маркирана като Test Sources** (зелена папка)
   - `main/resources` → трябва да е **маркирана като Resources**
   - `test/resources` → трябва да е **маркирана като Test Resources**

3. Ако не са маркирани:
   - Избери папката → кликни десен бутон → **Mark Directory as** → избери типа
   - Или кликни на папката и използвай иконите отгоре

### Стъпка 3: Провери Output папките
1. Отиди на таб **Paths**
2. **Compiler output**: трябва да е `target` или празно
3. Кликни **Apply** и **OK**

---

## Решение 3: Build проекта от IntelliJ

### Стъпка 1: Build проекта
1. **Build → Build Project** (или `Ctrl+F9`)
2. Изчакай да завърши build-а
3. Провери дали има грешки в долната част

### Стъпка 2: Rebuild проекта
Ако има проблеми:
1. **Build → Rebuild Project**
2. Изчакай да завърши

---

## Решение 4: Провери Run Configuration

### Стъпка 1: Отвори Run Configuration
1. Кликни на dropdown до Run бутона → **Edit Configurations...**
2. Избери конфигурацията `ShoeWarehouseApplication`

### Стъпка 2: Провери настройките
1. **Main class**: трябва да е `com.shoewarehouse.ShoeWarehouseApplication`
2. **Module**: трябва да е `shoe-warehouse`
3. **Working directory**: трябва да е папката на проекта
4. Кликни **OK**

---

## Решение 5: Инвалидирай кеша и рестартирай

Ако нищо не работи:
1. **File → Invalidate Caches...**
2. Избери **Invalidate and Restart**
3. След рестарт, изчакай IntelliJ да реиндексира проекта
4. Направи **Build → Rebuild Project**

---

## Бърза проверка

След което:
1. Провери дали има `target/classes` папка
2. Провери дали има `target/classes/com/shoewarehouse/ShoeWarehouseApplication.class`
3. Опитай да стартираш отново

---

## Ако все още не работи

1. Затвори IntelliJ
2. Изтрий папка `.idea` в проекта (backup ако е нужно)
3. Отвори IntelliJ
4. **File → Open** → избери `pom.xml`
5. IntelliJ ще импортира проекта отново
6. Направи **Build → Build Project**
