-- =========================
-- DATABASE (opcional)
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
    reference VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id) REFERENCES accounts(id)
);

--  Índices críticos
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
    reference VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transfer_source
        FOREIGN KEY (source_account_id) REFERENCES accounts(id),

    CONSTRAINT fk_transfer_target
        FOREIGN KEY (target_account_id) REFERENCES accounts(id)
);

--  Índices clave
CREATE INDEX idx_transfer_source ON transfers(source_account_id);
CREATE INDEX idx_transfer_target ON transfers(target_account_id);
CREATE INDEX idx_transfer_reference ON transfers(reference);

-- =========================
-- CONSTRAINTS IMPORTANTES
-- =========================

-- Evitar saldo negativo (MySQL 8+ soporta CHECK)
ALTER TABLE accounts
ADD CONSTRAINT chk_balance_non_negative CHECK (balance >= 0);

-- Evitar transferencias a la misma cuenta
ALTER TABLE transfers
ADD CONSTRAINT chk_different_accounts CHECK (source_account_id <> target_account_id);