# Как да поправиш 400 Bad Request при създаване на обувка

## Проблем
Получаваш 400 Bad Request при опит да създадеш обувка.

---

## ✅ Решение: Провери JSON заявката

Поправих контролера да показва точната грешка. Сега ще видиш съобщението за грешка в response body-то.

---

## Правилна заявка за създаване на обувка

### Стъпка 1: Уверете се че имате Brand и Category

Преди да създадеш обувка, трябва да имаш:
1. **Brand** - създай марка първо (POST /api/brands)
2. **Category** - създай категория първо (POST /api/categories)

---

### Стъпка 2: Правилна JSON заявка

```json
{
  "model": "Air Max",
  "size": "42",
  "color": "Black",
  "quantity": 50,
  "price": 120.00,
  "brand": {
    "id": 1
  },
  "categories": [
    {
      "id": 1
    }
  ]
}
```

**Задължителни полета:**
- `model` - String (задължително)
- `size` - String (задължително)
- `color` - String (задължително)
- `quantity` - Integer (задължително)
- `price` - BigDecimal/Number (задължително)
- `brand.id` - Long (задължително - Brand трябва да съществува)

**Опционални полета:**
- `categories` - Array (опционално - може да е празен масив или да липсва)

---

## Често срещани грешки

### Грешка 1: "Brand not found with id: X"
**Причина:** Brand с посоченото ID не съществува в базата.

**Решение:** 
1. Първо създай Brand (POST /api/brands)
2. Запиши ID-то на създадената марка
3. Използвай това ID в заявката за обувка

---

### Грешка 2: "Category not found with id: X"
**Причина:** Category с посоченото ID не съществува в базата.

**Решение:**
1. Първо създай Category (POST /api/categories)
2. Запиши ID-то на създадената категория
3. Използвай това ID в заявката за обувка

---

### Грешка 3: Липсващи задължителни полета
**Причина:** model, size, color, quantity или price липсват или са null.

**Решение:** Уверете се че всички задължителни полета са попълнени в JSON заявката.

---

## Примерен workflow

### Стъпка 1: Създай Brand
```
POST http://localhost:8080/api/brands
{
  "name": "Nike",
  "description": "Just Do It",
  "country": "USA"
}
```
**Отговор:** `{"id": 1, "name": "Nike", ...}`

---

### Стъпка 2: Създай Category
```
POST http://localhost:8080/api/categories
{
  "name": "Sneakers",
  "description": "Casual footwear"
}
```
**Отговор:** `{"id": 1, "name": "Sneakers", ...}`

---

### Стъпка 3: Създай Shoe
```
POST http://localhost:8080/api/shoes
{
  "model": "Air Max",
  "size": "42",
  "color": "Black",
  "quantity": 50,
  "price": 120.00,
  "brand": {
    "id": 1
  },
  "categories": [
    {
      "id": 1
    }
  ]
}
```

---

## Проверка

След като опиташ отново:
1. Провери **response body-то** в Postman - ще видиш точната грешка
2. Ако е "Brand not found with id: 1" - създай Brand първо
3. Ако е "Category not found with id: 1" - създай Category първо
4. Ако е друга грешка - провери дали всички задължителни полета са попълнени

---

## Забележка

`categories` е опционално - можеш да създадеш обувка без категории:

```json
{
  "model": "Air Max",
  "size": "42",
  "color": "Black",
  "quantity": 50,
  "price": 120.00,
  "brand": {
    "id": 1
  }
}
```

Или с празен масив:

```json
{
  "model": "Air Max",
  "size": "42",
  "color": "Black",
  "quantity": 50,
  "price": 120.00,
  "brand": {
    "id": 1
  },
  "categories": []
}
```
