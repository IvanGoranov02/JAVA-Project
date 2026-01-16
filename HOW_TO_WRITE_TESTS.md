# Стъпка по стъпка ръководство за писане на тестове

## Видове тестове в проекта

Проектът използва 3 типа тестове:
1. **Repository тестове** - тестват базата данни (@DataJpaTest)
2. **Service тестове** - тестват бизнес логиката (Mockito)
3. **Controller тестове** - тестват HTTP endpoints (MockMvc)
4. **Integration тестове** - тестват цялостен workflow

---

## 📚 Тип 1: Repository тестове (@DataJpaTest)

### Цел
Тестват директно базата данни и JPA операциите.

### Пример: BrandRepositoryTest

#### Стъпка 1: Създай тест клас

```java
package com.shoewarehouse.repository;

import com.shoewarehouse.entity.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest  // ← ВАЖНО: Зарежда само JPA компонентите
@ActiveProfiles("test")  // ← Използва test профила
class BrandRepositoryTest {

    @Autowired  // ← Spring инжектира реалния repository
    private BrandRepository brandRepository;

    private Brand brand;

    @BeforeEach  // ← Изпълнява се преди всеки тест
    void setUp() {
        brand = new Brand();
        brand.setName("Nike");
        brand.setDescription("Sportswear brand");
        brand.setCountry("USA");
    }
}
```

#### Стъпка 2: Напиши тест метод

```java
@Test
void testSaveBrand() {
    // 1. Arrange (Подготовка)
    // brand вече е готов от setUp()
    
    // 2. Act (Изпълнение)
    Brand savedBrand = brandRepository.save(brand);
    
    // 3. Assert (Проверка)
    assertThat(savedBrand.getId()).isNotNull();  // Проверяваме че има ID
    assertThat(savedBrand.getName()).isEqualTo("Nike");
}
```

#### Стъпка 3: Напиши още тестове

```java
@Test
void testFindBrandById() {
    // Arrange
    Brand savedBrand = brandRepository.save(brand);
    
    // Act
    Optional<Brand> foundBrand = brandRepository.findById(savedBrand.getId());
    
    // Assert
    assertThat(foundBrand).isPresent();  // Проверяваме че е намерен
    assertThat(foundBrand.get().getName()).isEqualTo("Nike");
}

@Test
void testDeleteBrand() {
    // Arrange
    Brand savedBrand = brandRepository.save(brand);
    
    // Act
    brandRepository.delete(savedBrand);
    
    // Assert
    Optional<Brand> foundBrand = brandRepository.findById(savedBrand.getId());
    assertThat(foundBrand).isEmpty();  // Проверяваме че е изтрит
}
```

### Шаблон за нов Repository тест

1. Копирай съществуващия `BrandRepositoryTest.java`
2. Промени името на класа (например `SupplierRepositoryTest`)
3. Промени `BrandRepository` на `SupplierRepository`
4. Промени `Brand` на `Supplier`
5. Настрой `setUp()` за новия entity
6. Адаптирай тестовете за новия entity

---

## 📝 Тип 2: Service тестове (Mockito)

### Цел
Тестват бизнес логиката БЕЗ база данни (използват мокове).

### Пример: BrandServiceTest

#### Стъпка 1: Създай тест клас

```java
package com.shoewarehouse.service;

import com.shoewarehouse.entity.Brand;
import com.shoewarehouse.repository.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;  // ← За инжектиране на service
import org.mockito.Mock;  // ← За мок на repository
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // ← ВАЖНО: Активира Mockito
class BrandServiceTest {

    @Mock  // ← Създава мок на repository
    private BrandRepository brandRepository;

    @InjectMocks  // ← Инжектира моковете в service
    private BrandService brandService;

    private Brand brand;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setId(1L);
        brand.setName("Nike");
        brand.setDescription("Sportswear brand");
        brand.setCountry("USA");
    }
}
```

#### Стъпка 2: Напиши тест за успешен сценарий

```java
@Test
void testGetAllBrands() {
    // 1. Arrange (Подготовка) - настройка на мок
    List<Brand> brands = Arrays.asList(brand);
    when(brandRepository.findAll()).thenReturn(brands);  // ← Мок връща данни
    
    // 2. Act (Изпълнение)
    List<Brand> result = brandService.getAllBrands();
    
    // 3. Assert (Проверка)
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("Nike");
    verify(brandRepository).findAll();  // ← Проверяваме че методът е извикан
}
```

#### Стъпка 3: Напиши тест за грешен сценарий

```java
@Test
void testCreateBrandWithDuplicateName() {
    // Arrange - мок връща че вече съществува
    when(brandRepository.existsByName("Nike")).thenReturn(true);
    
    // Act & Assert - очакваме грешка
    assertThatThrownBy(() -> brandService.createBrand(brand))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("already exists");
    
    // Проверяваме че save НЕ е извикан
    verify(brandRepository, never()).save(any(Brand.class));
}
```

#### Стъпка 4: Напиши тест за update

```java
@Test
void testUpdateBrand() {
    // Arrange
    Brand updatedBrand = new Brand();
    updatedBrand.setName("Nike Updated");
    
    when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
    when(brandRepository.existsByName("Nike Updated")).thenReturn(false);
    when(brandRepository.save(any(Brand.class))).thenReturn(brand);
    
    // Act
    Brand result = brandService.updateBrand(1L, updatedBrand);
    
    // Assert
    assertThat(result).isNotNull();
    verify(brandRepository).findById(1L);
    verify(brandRepository).save(any(Brand.class));
}
```

### Mockito команди

- `when(...).thenReturn(...)` - настройва какво да върне мокът
- `verify(...)` - проверява дали метод е извикан
- `verify(..., never())` - проверява че метод НЕ е извикан
- `any(...)` - мачва всеки аргумент
- `eq(...)` - мачва конкретен аргумент

### Шаблон за нов Service тест

1. Копирай `BrandServiceTest.java`
2. Промени името на класа
3. Промени `BrandService` на новия Service
4. Промени `BrandRepository` на новия Repository
5. Настрой `setUp()` за новия entity
6. Адаптирай тестовете

---

## 🌐 Тип 3: Controller тестове (@WebMvcTest)

### Цел
Тестват HTTP endpoints БЕЗ пълно Spring Boot стартиране.

### Пример: BrandControllerTest

#### Стъпка 1: Създай тест клас

```java
package com.shoewarehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoewarehouse.entity.Brand;
import com.shoewarehouse.service.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;  // ← ВАЖНО
import org.springframework.boot.test.mock.mockito.MockBean;  // ← Мок на service
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;  // ← За HTTP заявки

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BrandController.class)  // ← Зарежда само контролера
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;  // ← За правене на HTTP заявки

    @MockBean  // ← Мок на service
    private BrandService brandService;

    @Autowired
    private ObjectMapper objectMapper;  // ← За JSON сериализация

    private Brand brand;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setId(1L);
        brand.setName("Nike");
        brand.setDescription("Sportswear brand");
        brand.setCountry("USA");
    }
}
```

#### Стъпка 2: Тест за GET заявка

```java
@Test
@WithMockUser  // ← Security мок
void testGetAllBrands() throws Exception {
    // Arrange
    List<Brand> brands = Arrays.asList(brand);
    when(brandService.getAllBrands()).thenReturn(brands);
    
    // Act & Assert - изпълняваме HTTP заявка
    mockMvc.perform(get("/api/brands")  // ← GET заявка
                    .with(httpBasic("admin", "admin123")))  // ← Basic Auth
            .andExpect(status().isOk())  // ← Очакваме 200 OK
            .andExpect(jsonPath("$[0].name").value("Nike"));  // ← Проверяваме JSON
    
    verify(brandService).getAllBrands();
}
```

#### Стъпка 3: Тест за POST заявка

```java
@Test
@WithMockUser
void testCreateBrand() throws Exception {
    // Arrange
    when(brandService.createBrand(any(Brand.class))).thenReturn(brand);
    
    // Act & Assert
    mockMvc.perform(post("/api/brands")  // ← POST заявка
                    .with(httpBasic("admin", "admin123"))
                    .contentType(MediaType.APPLICATION_JSON)  // ← JSON content type
                    .content(objectMapper.writeValueAsString(brand)))  // ← JSON body
            .andExpect(status().isCreated())  // ← Очакваме 201 Created
            .andExpect(jsonPath("$.name").value("Nike"));
    
    verify(brandService).createBrand(any(Brand.class));
}
```

#### Стъпка 4: Тест за PUT заявка

```java
@Test
@WithMockUser
void testUpdateBrand() throws Exception {
    // Arrange
    when(brandService.updateBrand(eq(1L), any(Brand.class))).thenReturn(brand);
    
    // Act & Assert
    mockMvc.perform(put("/api/brands/1")  // ← PUT заявка
                    .with(httpBasic("admin", "admin123"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(brand)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Nike"));
    
    verify(brandService).updateBrand(eq(1L), any(Brand.class));
}
```

#### Стъпка 5: Тест за DELETE заявка

```java
@Test
@WithMockUser
void testDeleteBrand() throws Exception {
    // Arrange
    doNothing().when(brandService).deleteBrand(1L);
    
    // Act & Assert
    mockMvc.perform(delete("/api/brands/1")  // ← DELETE заявка
                    .with(httpBasic("admin", "admin123")))
            .andExpect(status().isNoContent());  // ← 204 No Content
    
    verify(brandService).deleteBrand(1L);
}
```

### MockMvc команди

- `get(...)` - GET заявка
- `post(...)` - POST заявка
- `put(...)` - PUT заявка
- `delete(...)` - DELETE заявка
- `.contentType(MediaType.APPLICATION_JSON)` - задава Content-Type
- `.content(...)` - задава body
- `.with(httpBasic(...))` - задава Basic Auth
- `.andExpect(status().isOk())` - проверява статус код
- `.andExpect(jsonPath("$...").value(...))` - проверява JSON

### Шаблон за нов Controller тест

1. Копирай `BrandControllerTest.java`
2. Промени името на класа
3. Промени `BrandController` на новия Controller
4. Промени `BrandService` на новия Service
5. Настрой `setUp()` за новия entity
6. Адаптирай тестовете

---

## 🔗 Тип 4: Integration тестове

### Цел
Тестват цялостен workflow с реална база данни.

### Пример: OrderIntegrationTest

```java
@DataJpaTest  // ← Използва реална тестова база
@ActiveProfiles("test")
class OrderIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private ShoeRepository shoeRepository;
    
    @Autowired
    private BrandRepository brandRepository;
    
    // ... тестове за цялостен workflow
}
```

---

## 🎯 Стъпка по стъпка: Как да напишеш нов тест

### Пример: Тест за SupplierController

#### Стъпка 1: Създай файл

Създай файл: `test/java/com/shoewarehouse/controller/SupplierControllerTest.java`

#### Стъпка 2: Напиши основната структура

```java
package com.shoewarehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoewarehouse.entity.Supplier;
import com.shoewarehouse.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupplierController.class)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService supplierService;

    @Autowired
    private ObjectMapper objectMapper;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("ABC Suppliers");
        supplier.setEmail("info@abc.com");
        supplier.setPhone("+359888123456");
        supplier.setAddress("Main Street 123");
        supplier.setCity("Sofia");
        supplier.setCountry("Bulgaria");
    }
}
```

#### Стъпка 3: Добави тест методи

```java
    @Test
    @WithMockUser
    void testGetAllSuppliers() throws Exception {
        List<Supplier> suppliers = Arrays.asList(supplier);
        when(supplierService.getAllSuppliers()).thenReturn(suppliers);

        mockMvc.perform(get("/api/suppliers")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ABC Suppliers"));

        verify(supplierService).getAllSuppliers();
    }

    @Test
    @WithMockUser
    void testCreateSupplier() throws Exception {
        when(supplierService.createSupplier(any(Supplier.class))).thenReturn(supplier);

        mockMvc.perform(post("/api/suppliers")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("ABC Suppliers"));

        verify(supplierService).createSupplier(any(Supplier.class));
    }
    // ... още тестове
}
```

---

## 🚀 Как да стартираш тестовете

### В IntelliJ IDEA:

1. Кликни десен бутон на тест файла или метода
2. Избери **Run 'TestMethod'** или **Run 'TestClass'**
3. Виж резултатите в Run конзолата

### Чрез Maven:

```bash
# Всички тестове
mvn test

# Конкретен тест клас
mvn test -Dtest=BrandControllerTest

# Конкретен тест метод
mvn test -Dtest=BrandControllerTest#testGetAllBrands
```

---

## ✅ Проверен лист за добър тест

- ✅ Има `@Test` анотация
- ✅ Има Arrange-Act-Assert структура
- ✅ Тества както успешни, така и неуспешни сценарии
- ✅ Има проверки с `assertThat()`
- ✅ Има `verify()` за моковете (ако се използват)
- ✅ Има `@BeforeEach` за общи данни
- ✅ Използва подходящия тип тест (@DataJpaTest, @WebMvcTest, и т.н.)

---

## 📚 Ресурси

- **AssertJ**: `assertThat()`, `hasSize()`, `isEqualTo()`, и т.н.
- **Mockito**: `when()`, `verify()`, `any()`, `eq()`
- **MockMvc**: `perform()`, `andExpect()`, `jsonPath()`

---

## 💡 Съвети

1. **Именуване**: `testGetAllBrands`, `testCreateBrandWithDuplicateName`
2. **Един тест = едно нещо**: Всеки тест тества един сценарий
3. **AAA Pattern**: Arrange-Act-Assert
4. **Тествай границите**: празни списъци, null стойности, и т.н.
5. **Използвай примерни тестове**: копирай и адаптирай съществуващи тестове
