# Как да поправиш 400 Bad Request при създаване на поръчка

## Проблем
Получаваш 400 Bad Request при опит да създадеш поръчка.

---

## ✅ Решение: Провери дали данните съществуват

Преди да създадеш поръчка, трябва да имаш:

1. **Supplier с ID 1** - създай доставчик първо
2. **Shoe с ID 1** - създай обувка първо

---

## Стъпка по стъпка workflow:

### Стъпка 1: Създай Brand
```
POST http://localhost:8080/api/brands
{
  "name": "Nike",
  "description": "Just Do It",
  "country": "USA"
}
```
**Запиши ID-то** (обикновено ще е 1)

---

### Стъпка 2: Създай Category
```
POST http://localhost:8080/api/categories
{
  "name": "Sneakers",
  "description": "Casual footwear"
}
```
**Запиши ID-то** (обикновено ще е 1)

---

### Стъпка 3: Създай Supplier
```
POST http://localhost:8080/api/suppliers
{
  "name": "ABC Suppliers",
  "email": "info@abc.com",
  "phone": "+359888123456",
  "address": "Main Street 123",
  "city": "Sofia",
  "country": "Bulgaria"
}
```
**Запиши ID-то** (обикновено ще е 1)

---

### Стъпка 4: Създай Shoe
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
**Запиши ID-то** (обикновено ще е 1)

---

### Стъпка 5: СЕГА създай Order
```
POST http://localhost:8080/api/orders
{
  "orderNumber": "ORD-001",
  "orderDate": "2024-01-15T10:30:00",
  "status": "PENDING",
  "supplier": {
    "id": 1
  },
  "orderItems": [
    {
      "shoe": {
        "id": 1
      },
      "quantity": 5,
      "unitPrice": 120.00
    }
  ]
}
```

---

## Ако все още има грешка

Поправих контролера да показва точната грешка. Сега вместо само "400 Bad Request", ще видиш съобщението за грешка в response body-то.

Провери response body-то в Postman - там ще видиш точно каква е грешката:
- "Supplier not found with id: 1"
- "Shoe not found with id: 1"
- Или друга грешка

---

## Алтернатива: Опростена заявка

Можеш да опростиш заявката - не е нужно да подаваш всичко:

```json
{
  "supplier": {
    "id": 1
  },
  "orderItems": [
    {
      "shoe": {
        "id": 1
      },
      "quantity": 5
    }
  ]
}
```

`orderNumber`, `orderDate` и `status` се генерират автоматично ако не ги подадеш.
`unitPrice` се взима от цената на обувката автоматично.

---

## Проверка

След всяка заявка, провери response-а:
- Ако е 201 Created - успешно!
- Ако е 400 Bad Request - провери response body за точната грешка
