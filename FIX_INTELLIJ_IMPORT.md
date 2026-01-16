# Как да поправиш "cannot resolve shoewarehouse" в IntelliJ IDEA

## Проблем
IntelliJ IDEA не разпознава проекта като Maven проект.

---

## Решение 1: Импортирай проекта като Maven проект

### Стъпка 1: Затвори текущия проект
- File → Close Project

### Стъпка 2: Отвори проекта като Maven проект
1. **File → Open**
2. Намери папката: `C:\Users\FAMILY\JAVA-Project`
3. Избери **pom.xml** файла (не папката!)
4. Кликни **Open as Project**
5. IntelliJ ще попита "Open as Project" - кликни **OK**

### Стъпка 3: Изчакай IntelliJ да импортира Maven проекта
- Ще видиш "Maven projects need to be imported" в долния десен ъгъл
- Кликни **Import Maven Project** или **Enable Auto-Import**
- Изчакай да завърши индексирането (ще видиш логове отдолу)

---

## Решение 2: Ако проектът вече е отворен

### Стъпка 1: Добави Maven проект
1. Кликни десен бутон на **pom.xml** в Project view
2. Избери **Add as Maven Project**
3. Или отиди на: **View → Tool Windows → Maven**
4. В Maven панела, кликни иконата за **Reload** (двойна стрелка)

### Стъпка 2: Reload Maven проекта
- В Maven tool window (дясната страна), кликни на иконата за **Reload All Maven Projects**
- Или: кликни десен бутон на проекта → **Maven → Reload Project**

### Стъпка 3: Редефинирай Source Folders
1. File → Project Structure (Ctrl+Alt+Shift+S)
2. Отиди на **Modules**
3. Избери проекта
4. Под **Sources**, кликни на папките:
   - Маркирай `main/java` като **Sources**
   - Маркирай `test/java` като **Test Sources**
   - Маркирай `main/resources` като **Resources**
   - Маркирай `test/resources` като **Test Resources**
5. Кликни **Apply** и **OK**

---

## Решение 3: Инвалидирай кеша и рестартирай

1. **File → Invalidate Caches...**
2. Избери **Invalidate and Restart**
3. Изчакай IntelliJ да рестартира
4. След рестарт, IntelliJ ще реиндексира проекта

---

## Решение 4: Провери JDK настройките

1. **File → Project Structure** (Ctrl+Alt+Shift+S)
2. Отиди на **Project**
3. Провери **Project SDK** - трябва да е **Java 21** или **Java 17**
4. Ако не е, кликни на dropdown и избери **JDK**
5. Намери: `C:\Program Files\Java\jdk-21`
6. Кликни **Apply** и **OK**

---

## След поправката

След като IntelliJ разпознае проекта:
- Всички червени линии под `shoewarehouse` ще изчезнат
- Ще можеш да стартираш приложението с зелената стрелка
- Всички импорти ще работят

---

## Бърза проверка

Отвори `ShoeWarehouseApplication.java` и ако:
- ✅ Няма червени линии под `shoewarehouse` = готово!
- ❌ Все още има червени линии = използвай Решение 1 или 2

---

## Ако нищо не работи

1. Затвори IntelliJ
2. Изтрий папката `.idea` в проекта (ако съществува)
3. Отвори IntelliJ
4. File → Open → избери `pom.xml`
5. Open as Project
