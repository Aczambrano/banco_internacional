
## 🗄️ Base de Datos

### SGBD seleccionado

Se utilizó **MySQL 8** con el motor de almacenamiento InnoDB.

---

### Justificación

MySQL 8 es una buena elección para este dominio principalmente por:

- Su soporte de transacciones **ACID**, lo cual es clave en operaciones bancarias donde no se pueden permitir inconsistencias (por ejemplo, en depósitos o transferencias).
- Se utiliza locking para evitar que varias operaciones al mismo tiempo trabajen sobre la misma cuenta, ya que esto podría generar saldos incorrectos.- Su buen rendimiento en consultas gracias al uso de **índices**, especialmente útil para consultas frecuentes como el historial de transacciones.
- Además, permite definir **constraints** (CHECK, UNIQUE, FK), lo que refuerza la integridad de los datos directamente desde la base de datos.

---

### Esquema / Modelo de datos

```sql
-- =========================
-- DATABASE
-- =========================
CREATE DATABASE IF NOT EXISTS bank_db;
USE bank_db;

-- =========================
-- TABLE: accounts
-- =========================
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(100) NOT NULL UNIQUE,
    client_id BIGINT NOT NULL, 
    type VARCHAR(20) NOT NULL,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    currency VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice para búsquedas por cliente
CREATE INDEX idx_accounts_client_id ON accounts(client_id);

-- =========================
-- TABLE: transactions
-- =========================
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    reference VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id) REFERENCES accounts(id)
);


--  Índices para consultas por cuenta y fecha de creacion para historial de transacciones
CREATE INDEX idx_tx_account_date ON transactions(account_id, created_at DESC);
CREATE INDEX idx_tx_reference ON transactions(reference);

-- =========================
-- TABLE: transfers
-- =========================
CREATE TABLE transfers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_account_id BIGINT NOT NULL,
    target_account_id BIGINT NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    reference VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transfer_source
        FOREIGN KEY (source_account_id) REFERENCES accounts(id),

    CONSTRAINT fk_transfer_target
        FOREIGN KEY (target_account_id) REFERENCES accounts(id)
);

-- Índices para transferencias
CREATE INDEX idx_transfer_source ON transfers(source_account_id);
CREATE INDEX idx_transfer_target ON transfers(target_account_id);
CREATE INDEX idx_transfer_reference ON transfers(reference);

-- =========================
-- CONSTRAINTS IMPORTANTES
-- =========================

-- Evitar saldo negativo con CHECK
ALTER TABLE accounts
ADD CONSTRAINT chk_balance_non_negative CHECK (balance >= 0);

-- Evitar transferencias a la misma cuenta
ALTER TABLE transfers
ADD CONSTRAINT chk_different_accounts CHECK (source_account_id <> target_account_id);
```

### Decisiones de diseño

El modelo está **normalizado**, separando las entidades en `accounts`, `transactions` y `transfers`. Esto evita duplicidad de datos y facilita mantener la consistencia.

El campo `client_id` no tiene clave foránea porque pertenece a otro microservicio, lo que permite mantener el desacoplamiento entre servicios.

En cuanto a índices, se definieron sobre los campos más usados en consultas. El más importante es el índice compuesto en `transactions (account_id, created_at)`, ya que permite obtener el historial de una cuenta ordenado por fecha de forma eficiente.

Gracias a este índice, la consulta de historial paginado funciona correctamente con `LIMIT` y `OFFSET`, evitando recorrer toda la tabla y permitiendo escalar incluso con grandes volúmenes de datos.

También se incluyeron índices por referencia para evitar duplicados (idempotencia) y en transferencias para consultas por cuenta origen y destino.
