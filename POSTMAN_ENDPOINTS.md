# Postman Endpoints за Shoe Warehouse API

## Base URL
```
http://localhost:8080
```

## Authentication

### Basic Authentication в Postman:
1. Отвори заявката в Postman
2. Отиди на таб **Authorization**
3. Избери **Basic Auth** от dropdown менюто
4. Въведи:
   - **Username**: `admin`
   - **Password**: `admin123`

Или:
   - **Username**: `user`
   - **Password**: `user123`

---

## 📋 Brands (Марки)

### GET Всички марки
```
GET http://localhost:8080/api/brands
```

### GET Марка по ID
```
GET http://localhost:8080/api/brands/1
```

### POST Създаване на марка
```
POST http://localhost:8080/api/brands
Content-Type: application/json

{
  "name": "Nike",
  "description": "Just Do It",
  "country": "USA"
}
```

### PUT Актуализация на марка
```
PUT http://localhost:8080/api/brands/1
Content-Type: application/json

{
  "name": "Nike Updated",
  "description": "Updated description",
  "country": "USA"
}
```

### DELETE Изтриване на марка
```
DELETE http://localhost:8080/api/brands/1
```

---

## 📦 Categories (Категории)

### GET Всички категории
```
GET http://localhost:8080/api/categories
```

### GET Категория по ID
```
GET http://localhost:8080/api/categories/1
```

### POST Създаване на категория
```
POST http://localhost:8080/api/categories
Content-Type: application/json

{
  "name": "Sneakers",
  "description": "Casual footwear"
}
```

### PUT Актуализация на категория
```
PUT http://localhost:8080/api/categories/1
Content-Type: application/json

{
  "name": "Sneakers Updated",
  "description": "Updated description"
}
```

### DELETE Изтриване на категория
```
DELETE http://localhost:8080/api/categories/1
```

---

## 👟 Shoes (Обувки)

### GET Всички обувки
```
GET http://localhost:8080/api/shoes
```

### GET Обувка по ID
```
GET http://localhost:8080/api/shoes/1
```

### GET Обувки по марка
```
GET http://localhost:8080/api/shoes/brand/1
```

### GET Обувки по категория
```
GET http://localhost:8080/api/shoes/category/1
```

### GET Обувки с ниско количество
```
GET http://localhost:8080/api/shoes/low-stock/10
```

### POST Създаване на обувка
```
POST http://localhost:8080/api/shoes
Content-Type: application/json

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

### PUT Актуализация на обувка
```
PUT http://localhost:8080/api/shoes/1
Content-Type: application/json

{
  "model": "Air Max Updated",
  "size": "43",
  "color": "White",
  "quantity": 30,
  "price": 130.00,
  "brand": {
    "id": 1
  }
}
```

### DELETE Изтриване на обувка
```
DELETE http://localhost:8080/api/shoes/1
```

---

## 🏭 Suppliers (Доставчици)

### GET Всички доставчици
```
GET http://localhost:8080/api/suppliers
```

### GET Доставчик по ID
```
GET http://localhost:8080/api/suppliers/1
```

### POST Създаване на доставчик
```
POST http://localhost:8080/api/suppliers
Content-Type: application/json

{
  "name": "ABC Suppliers",
  "email": "info@abc.com",
  "phone": "+359888123456",
  "address": "Main Street 123",
  "city": "Sofia",
  "country": "Bulgaria"
}
```

### PUT Актуализация на доставчик
```
PUT http://localhost:8080/api/suppliers/1
Content-Type: application/json

{
  "name": "ABC Suppliers Updated",
  "email": "newemail@abc.com",
  "phone": "+359888654321"
}
```

### DELETE Изтриване на доставчик
```
DELETE http://localhost:8080/api/suppliers/1
```

---

## 📦 Orders (Поръчки)

### GET Всички поръчки
```
GET http://localhost:8080/api/orders
```

### GET Поръчка по ID
```
GET http://localhost:8080/api/orders/1
```

### GET Поръчки по доставчик
```
GET http://localhost:8080/api/orders/supplier/1
```

### GET Поръчки по статус
```
GET http://localhost:8080/api/orders/status/PENDING
```
Статуси: `PENDING`, `PROCESSING`, `COMPLETED`, `CANCELLED`

### POST Създаване на поръчка
```
POST http://localhost:8080/api/orders
Content-Type: application/json

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

### PUT Актуализация на поръчка
```
PUT http://localhost:8080/api/orders/1
Content-Type: application/json

{
  "status": "COMPLETED"
}
```

### DELETE Изтриване на поръчка
```
DELETE http://localhost:8080/api/orders/1
```

### POST Добавяне на артикул към поръчка
```
POST http://localhost:8080/api/orders/1/items
Content-Type: application/json

{
  "shoe": {
    "id": 2
  },
  "quantity": 3,
  "unitPrice": 100.00
}
```

### DELETE Премахване на артикул от поръчка
```
DELETE http://localhost:8080/api/orders/items/1
```

---

## 🔍 H2 Database Console

За да провериш данните в базата:

1. Отвори браузър и отиди на: `http://localhost:8080/h2-console`
2. JDBC URL: `jdbc:h2:mem:shoewarehouse`
3. Username: `sa`
4. Password: (празно)
5. Click Connect

---

## 📝 Примерен workflow в Postman

1. **Създай марка (Brand)** → POST `/api/brands`
2. **Създай категория (Category)** → POST `/api/categories`
3. **Създай обувка (Shoe)** → POST `/api/shoes` (с brand и categories)
4. **Създай доставчик (Supplier)** → POST `/api/suppliers`
5. **Създай поръчка (Order)** → POST `/api/orders` (с supplier и orderItems)
6. **Провери поръчката** → GET `/api/orders/1`
7. **Актуализирай статуса** → PUT `/api/orders/1`
