# Pontificia Universidad Javeriana
## Principios de Desarrollo de Software
### Laboratorio #3: Implementación de un CRUD para un "Zoo de Mascotas Fantásticas" en Spring Boot

![Java 21](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-brightgreen.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.4-blue.svg)
![Gitflow](https://img.shields.io/badge/Gitflow-Compliant-informational.svg)

---

## 👥 Integrantes del Equipo
* **Nicolás Cardozo Mejía**
* **Samuel Correa Morales** ([@SACOMO82](https://github.com/SACOMO82))
* **Nicolás Espinosa Soler**
* **Gustavo Mejía**

### 👨‍🏫 Docente Evaluador
* **Santiago Andrés Peña Arismendi** ([@soldash](https://github.com/soldash))

---

## 📖 Descripción General del Proyecto
Sistema transaccional de gestión para el **Zoo de Mascotas Fantásticas**, desarrollado como API RESTful sobre **Spring Boot 4**, persistencia relacional con **Spring Data JPA / Hibernate** sobre **MySQL Server 8.4** y gestión colaborativa de versiones bajo el modelo **Gitflow**.

El sistema permite la administración completa de hábitats mágicos (**Zonas**) y criaturas (**Creatures**), garantizando integridad referencial, validaciones sintácticas con Bean Validation y reglas de negocio estrictas con códigos de respuesta HTTP semánticos.

---

## 🏛️ Arquitectura en Tres Capas (SoC)

El proyecto implementa estrictamente el principio de **Separación de Responsabilidades (Separation of Concerns)**, asegurando que cada componente solo interactúe con la capa inmediatamente inferior:

```
[ Cliente HTTP / Postman ]
          │
          ▼
1. Capa de Presentación (Controller)
   - Exposición de endpoints RESTful (@RestController, @RequestMapping).
   - Validación sintáctica de entrada (@Valid, @RequestBody).
   - Retorno de códigos de estado HTTP semánticos (ResponseEntity).
          │
          ▼
2. Capa de Lógica de Negocio (Service)
   - Aplicación de restricciones de negocio (control de cupos, salud crítica).
   - Inyección de dependencias por constructor (@Autowired).
   - Mapeo y orquestación hacia DTOs (ZoneResponse).
          │
          ▼
3. Capa de Acceso a Datos (Repository)
   - Abstracción de persistencia con Spring Data JPA (JpaRepository).
   - Generación automática de sentencias SQL mediante Hibernate ORM.
          │
          ▼
[ Base de Datos: MySQL Server 8.4 (Puerto 3306) ]
```

---

## ⚖️ Reglas de Negocio Implementadas

1. **Control de Cupo Máximo en Zonas:** No se permite asignar una criatura a una zona que ya alcanzó su capacidad máxima (`capacity`). Retorna **`409 Conflict`** (*"Zone is at maximum capacity"*).
2. **Protección Veterinaria (Salud Crítica):** No se permite eliminar una criatura cuyo estado de salud sea `"critical"`. Retorna **`409 Conflict`** (*"Cannot delete a creature in critical health"*).
3. **Integridad de Hábitats:** No se permite eliminar una zona si aún tiene criaturas asignadas viviendo en ella. Retorna **`409 Conflict`** (*"Cannot delete zone with assigned creatures"*).
4. **Validación de Datos (Jakarta Bean Validation):**
   * Nombre y especie obligatorios (`@NotBlank`).
   * Tamaño de criatura mayor o igual a 0 (`@Min(0)`).
   * Nivel de peligrosidad restringido entre 1 y 10 (`@Min(1)` y `@Max(10)`).
   * Violaciones retornan **`400 Bad Request`** con la lista de campos erróneos.
5. **Manejo Centralizado de Excepciones:** Clase `@RestControllerAdvice` (`GlobalExceptionHandler`) que previene errores 500 genéricos, retornando:
   * **`400 Bad Request`**: Errores de validación de datos.
   * **`404 Not Found`**: Búsqueda de recursos por ID inexistente.
   * **`409 Conflict`**: Violación de cualquier regla de negocio.

---

## 📡 Endpoints de la API REST

### 1. Zonas (`/api/zones`)
| Método | Endpoint | Descripción | Código Éxito |
| :--- | :--- | :--- | :---: |
| `POST` | `/api/zones` | Crear un nuevo hábitat con cupo | `201 Created` |
| `GET` | `/api/zones` | Listar todas las zonas con `creatureCount` (DTO) | `200 OK` |
| `GET` | `/api/zones/{id}` | Obtener detalle de una zona por ID | `200 OK` |
| `PUT` | `/api/zones/{id}` | Modificar nombre, descripción o capacidad | `200 OK` |
| `DELETE` | `/api/zones/{id}` | Eliminar zona (bloqueado si tiene criaturas) | `204 No Content` |

### 2. Criaturas (`/api/creatures`)
| Método | Endpoint | Descripción | Código Éxito |
| :--- | :--- | :--- | :---: |
| `POST` | `/api/creatures` | Registrar criatura asignándola a una zona | `201 Created` |
| `GET` | `/api/creatures` | Listar todas las criaturas mágicas | `200 OK` |
| `GET` | `/api/creatures/{id}` | Consultar criatura por ID | `200 OK` |
| `PUT` | `/api/creatures/{id}` | Actualizar datos o reubicar a otra zona | `200 OK` |
| `DELETE` | `/api/creatures/{id}` | Eliminar criatura (bloqueado si salud es "critical") | `204 No Content` |

---

## 🧪 Pruebas y Documentación en Postman
* **Colección Oficial de Pruebas:** Archivo [`Zoo_Fantastico_Oficial.postman_collection.json`](./Zoo_Fantastico_Oficial.postman_collection.json) ubicado en la raíz del repositorio con **15 peticiones preconfiguradas** que cubren el 100% de la rúbrica.
* **Documentación Interactiva en Vivo:** [Ver Documentación Oficial en Postman Web](https://nicolascardozo0709-9620238.postman.co/workspace/044c4d62-c413-4021-8227-943de5659030/documentation/58721426-14a17f67-881b-45dc-9ac6-6611dc632603)

---

## 🌿 Gestión de Versiones (Gitflow)

El repositorio sigue estrictamente el modelo de ramificación Gitflow:
* **`main`**: Rama de producción con código estable liberado (**Release v1.0.0**).
* **`develop`**: Rama base de integración continua.
* **`feature/*`**: Ramas de características desarrolladas por el equipo:
  * `feature/creature-crud`: Entidad, repositorio, servicio y controlador de Criaturas.
  * `feature/zone-management`: Entidad Zone, relación `@ManyToOne` y DTO `ZoneResponse`.
  * `feature/exception-handling`: Manejador centralizado `@RestControllerAdvice` y validaciones.
* **Pull Request Oficial:** [PR #1: Release v1.0.0 de develop a main](https://github.com/nicolascardozo0709/laboratorio-3-zoologico/pull/1) con revisor asignado al profesor **`@soldash`**.

---

## 🚀 Requisitos y Puesta en Marcha

### Prerrequisitos
* **Java 21 LTS**
* **MySQL Server 8.4** escuchando en `localhost:3306` con base de datos `zoo_fantastico`.

### Configuración (`src/main/resources/application.properties`)
```properties
spring.application.name=zoo-fantastico
spring.datasource.url=jdbc:mysql://localhost:3306/zoo_fantastico?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=Sc@20070708
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Ejecutar la Aplicación
```bash
# En Windows (PowerShell / CMD)
.\mvnw.cmd spring-boot:run

# En Linux / macOS
./mvnw spring-boot:run
```
La aplicación iniciará en `http://localhost:8080`.

---

## 🤖 Declaración de Uso de Inteligencia Artificial
En cumplimiento de las directrices académicas (página 10 de la guía del laboratorio), se declara el uso asistido de **Google Antigravity** como soporte técnico en investigación arquitectónica, diseño de DTOs para mitigación de ciclos de serialización JSON y estructuración de respuestas globales con `@RestControllerAdvice`. Todo el código fue auditado, adaptado, validado contra MySQL 8.4 y verificado en Postman por los integrantes del equipo.
