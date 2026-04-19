# NovoBanco - Account Microservice

## 📑 Índice

- 📌 Descripción
- ⚙️ Instrucciones de ejecución
- 🗄️ Base de Datos
- 🧠 Restricciones y Escenarios de Negocio
- 📘 Architecture Decision Records (ADR)

---
## 📌 Descripción

Este microservicio forma parte de la arquitectura de NovoBanco y se encarga de la gestión de cuentas, transacciones y transferencias.

Está diseñado siguiendo principios de arquitectura hexagonal, aplicando buenas prácticas de programación y diseño de software como **SOLID**, **KISS**, **DRY** y **YAGNI**, además del uso consistente de **camelCase** en la nomenclatura del código para mantener legibilidad y estándares de desarrollo.

Asimismo, se consideran principios de consistencia, concurrencia e idempotencia en operaciones bancarias críticas.

---

## ⚙️ Instrucciones de ejecución

1. Clonar el repositorio
```
git clone https://github.com/Aczambrano/banco_internacional.git
```

---
2. Ubicarse en la raíz del proyecto y ejecutar
```
docker-compose -f docker/docker-compose.yml up --build
```
---
3. Abrir postman e importar la colección de postman ubicada en la raíz del proyecto

---

## ⚙️ Restricciones y Escenarios de Negocio

A continuación se documentan los principales escenarios de negocio y las decisiones tomadas para resolverlos.

---

### 1. 💰 Saldo negativo

**Requerimiento:**  Debe prevenirse el saldo negativo.

**Decisión de diseño:**  
La validación de saldo se implementó en el **dominio (entidad Account)**, específicamente en la operación `withdraw`.

**Justificación:**  
Se decidió encapsular la regla de negocio dentro del modelo de dominio para garantizar la integridad del estado de la cuenta.  
Esto evita duplicación de lógica en la capa de aplicación y asegura que cualquier operación que modifique el saldo respete la invariante del negocio.

El dominio lanza una excepción `InsufficientFundsException` cuando el monto a debitar es mayor al saldo disponible, evitando así estados inválidos.

---

### 2. 🚫 Cuenta inactiva

**Requerimiento:**  Las operaciones sobre cuentas bloqueadas o cerradas deben rechazarse con un error específico y descriptivo, distinto al error genérico.

---

**Decisión de diseño:**  
La validación del estado de la cuenta se implementa en dos niveles del dominio:

1. En la entidad `Account`, como validación interna en operaciones individuales (`deposit` y `withdraw`).
2. En el `AccountDomainService`, para operaciones de negocio más complejas como transferencias.

---

**Justificación:**

Se decidió centralizar la validación de cuentas inactivas en la capa de dominio para garantizar consistencia en todas las operaciones financieras.

- En operaciones simples (depósitos y retiros), la validación se realiza directamente en la entidad `Account`, asegurando que ninguna modificación del saldo pueda ejecutarse si la cuenta no está activa.
- En operaciones compuestas (como transferencias), la validación se refuerza en el `AccountDomainService`, verificando tanto la cuenta origen como la destino antes de ejecutar la operación.

Esto evita duplicación de lógica en la capa de aplicación y asegura que cualquier flujo de negocio respete la regla de cuenta activa.

En caso de incumplimiento, se lanza la excepción `AccountNotActiveException`, proporcionando un error específico y entendible para el dominio financiero.

---

### 3. 🔄 Transferencia parcial

**Requerimiento:**  
Si el débito se aplica pero el crédito falla, el estado final debe ser consistente. El candidato decide el mecanismo y debe documentarlo.

---

**Decisión de diseño:**  
Se implementó la transferencia utilizando una transacción única controlada por `@Transactional`, junto con bloqueo pesimista a nivel de base de datos (`PESSIMISTIC_WRITE`) sobre las cuentas involucradas.

La lógica de negocio de la transferencia se encapsula en el `AccountDomainService`, el cual valida previamente las reglas de negocio antes de ejecutar la operación.

---

**Justificación:**

Se garantiza consistencia mediante los siguientes mecanismos:

- **Atomicidad transaccional (@Transactional):**  
  Toda la operación de transferencia (débito, crédito y registro de movimientos) se ejecuta dentro de una única transacción.  
  Si cualquier paso falla, se realiza rollback automático, evitando estados intermedios inconsistentes.

- **Bloqueo pesimista (PESSIMISTIC_WRITE):**  
  Se bloquean las cuentas origen y destino durante la operación para evitar modificaciones concurrentes.

- **Separación de responsabilidades:**  
  La validación de reglas de negocio (cuentas activas, saldo suficiente) se realiza en el `AccountDomainService`, asegurando consistencia antes de la ejecución.

---

**Resultado:**  
Se asegura que no exista el escenario de débito sin crédito, ya que cualquier fallo en el proceso revierte completamente la transacción.

---

### 4. ⚔️ Concurrencia básica

**Requerimiento:**  
Dos retiros simultáneos no deben permitir saldo negativo.

---

**Decisión de diseño:**  
Se utiliza bloqueo pesimista (`PESSIMISTIC_WRITE`) sobre la cuenta y ejecución dentro de una transacción (`@Transactional`).

---

**Justificación:**  
El bloqueo evita que dos transacciones lean y modifiquen el mismo saldo al mismo tiempo con datos obsoletos  
La transacción garantiza consistencia y aislamiento hasta el commit.

---

**Resultado:**  
Se asegura que no existan sobre-débidos en escenarios concurrentes.

---

### 5. 🔁 Control de duplicidad

**Requerimiento:**  
El endpoint de depósito o transferencia puede recibir el mismo request dos veces por errores de red. El sistema debe evitar duplicar la operación.

---

**Decisión de diseño:**  
Se implementó un control de duplicidad utilizando el campo `reference`, el cual es enviado desde el cliente en cada operación.

---

**Justificación:**

En un sistema bancario, un mismo request repetido puede generar inconsistencias como duplicación de movimientos o dinero si no se controla adecuadamente.

Para evitar esto, se realiza una validación previa utilizando el campo `reference`:

- Antes de procesar una operación, se verifica si ya existe una transacción con ese `reference`.
- Si existe, se rechaza la operación para evitar duplicidad.

Adicionalmente, el campo `reference` cumple un rol importante en transferencias:

- El campo **no es único en la tabla `transactions`**, ya que una transferencia genera **dos registros**:
    - un débito (cuenta origen)
    - un crédito (cuenta destino)
- Ambos registros comparten el mismo `reference`, que también se guarda en la tabla `transfers`.

Esto permite:

- Agrupar las transacciones que pertenecen a una misma transferencia
- Mantener trazabilidad entre la operación (`transfers`) y sus efectos (`transactions`)

En este diseño:
- `reference` → se usa tanto para validación de duplicidad como para trazabilidad de operaciones relacionadas

---

**Resultado esperado:**  
Se garantiza que una misma operación no se ejecute más de una vez, incluso ante reintentos por fallos de red o timeout.

---

## 📘 Architecture Decision Records (ADR)

### 🔹 ADR 1: Uso de Arquitectura Hexagonal

**Contexto:**  
Se necesitaba un diseño que permitiera mantener el dominio desacoplado de frameworks y tecnologías externas, facilitando pruebas y evolución del sistema.

**Opciones consideradas:**
- Arquitectura en capas tradicional
- Arquitectura hexagonal
- Arquitectura con CQRS/DDD más avanzada

**Decisión:**  
Se eligió **arquitectura hexagonal (Ports & Adapters)** para separar claramente el dominio de la infraestructura.

**Consecuencias:**
- ✅ El dominio no depende de frameworks (Spring, JPA, etc.)
- ✅ Facilita testing (se pueden mockear repositorios y servicios externos)
- ✅ Permite cambiar infraestructura sin afectar la lógica de negocio
- 🔄 A futuro se puede extender a **CQRS** o **DDD con Value Objects** sin rehacer el diseño
- ❌ Mayor cantidad de clases y estructura más compleja al inicio

---

### 🔹 ADR 2: Alcance del microservicio y comunicación con clientes

**Contexto:**  
El sistema requiere manejar cuentas, pero también depende de información de clientes.

**Opciones consideradas:**
- Incluir clientes dentro del mismo microservicio
- Crear un microservicio independiente de clientes
- Simular el servicio externo

**Decisión:**  
Se decidió que este microservicio maneje solo **accounts, transactions y transfers**, dejando clientes como un servicio externo.  
Para efectos de la prueba, se utilizó **WireMock** para simular las respuestas HTTP y validar si el cliente existe al momento de crear una cuenta.

**Consecuencias:**
- ✅ Separación clara de responsabilidades (cada microservicio hace una cosa)
- ✅ Diseño alineado a microservicios reales
- ✅ Permite reemplazar fácilmente el mock por un servicio real
- 🔄 En un escenario real se podría integrar con mensajería (**Kafka o RabbitMQ**)
- ❌ Dependencia de un servicio externo (aunque aquí está mockeado)

---

### 🔹 ADR 3: Elección de base de datos

**Contexto:**  
Se requería una base de datos que soporte transacciones, consistencia y control de concurrencia.

**Opciones consideradas:**
- MySQL
- PostgreSQL
- Base de datos NoSQL

**Decisión:**  
Se utilizó **MySQL 8 (InnoDB)**.

**Consecuencias:**
- ✅ Soporte de transacciones ACID
- ✅ Permite usar `SELECT FOR UPDATE` (locking pesimista) para evitar inconsistencias en saldo
- ✅ Buen rendimiento con índices para consultas frecuentes
- 🔄 Si se implementara **CQRS**, se podría agregar una segunda base de datos (por ejemplo, **SingleStore**) optimizada para reportes y lecturas
- ❌ Menor flexibilidad que NoSQL para ciertos escenarios  


---

### 🔹 ADR 4: Diseño de entidades de base de datos

**Contexto:**  
Se necesitaba modelar correctamente las operaciones financieras del sistema, considerando depósitos, retiros y transferencias entre cuentas, manteniendo trazabilidad y consistencia.

**Opciones consideradas:**
- Usar una sola tabla de movimientos para todo (incluyendo transferencias)
- Separar `transactions` y `transfers`
- Modelar transferencias como eventos sin persistencia propia

**Decisión:**  
Se decidió separar las entidades en **accounts, transactions y transfers**, donde:

- `transactions` representa movimientos individuales (débito/crédito)
- `transfers` representa la operación de negocio entre dos cuentas

Además, se utiliza el campo `reference` para relacionar los registros de una misma transferencia.

**Consecuencias:**
- ✅ Mejor modelado del dominio (movimientos vs operación de negocio)
- ✅ Permite trazabilidad clara entre transferencias y sus efectos en cuentas
- ✅ Escalable para futuras reglas (fraude, auditoría, estados de transferencia)
- ✅ Facilita consultas de historial por cuenta (`transactions`)
- 🔄 Se puede extender agregando relación directa (`transfer_id`) en `transactions` si se requiere mayor trazabilidad
- ❌ Ligera duplicidad de información (reference en ambas tablas)
- ❌ Mayor complejidad que un modelo simplificado con una sola tabla

---