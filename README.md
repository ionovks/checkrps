# Spring Boot + PostgreSQL Highload Demo

Данное приложение демонстрирует работу с двумя таблицами (**accounts** и **transactions**) в PostgreSQL, где можно:
- Хранить сведения об учётной записи пользователя (баланс, валюту, время создания/обновления).
- Сохранять каждую операцию (дебет/кредит), позволяя рассчитывать итоговый баланс на лету.

## Содержание
1. [Описание](#описание)
2. [Требования](#требования)
3. [Структура БД](#структура-бд)
4. [Сборка и запуск](#сборка-и-запуск)
5. [Endpoints (REST)](#endpoints-rest)
6. [Нагрузочное тестирование (Locust)](#нагрузочное-тестирование-locust)
7. [Контакты / Ссылки](#контакты--ссылки)

---

## Описание

- **Таблица `accounts`**: хранит базовые сведения о счёте (ID, userId, currency, начальный баланс и метки времени).
- **Таблица `transactions`**: отражает каждую операцию (дебет/кредит), её сумму, ссылку на account, статус и т.д.
- **Логика**:
    - При дебетовой операции (списание) можно хранить число либо как отрицательное (через `negate()`), либо делать это на уровне запроса (`CASE WHEN t.type='debit' THEN -t.amount`).
    - При кредитной операции (пополнение) обычно сохраняем положительное значение.

---

## Требования

- **Java** 21
- **PostgreSQL** 14/15 (или выше)
-  **Gradle** 
- _(Опционально)_ **Locust** для нагрузочных тестов

---

## Структура БД

Ниже **DDL-сценарий** для PostgreSQL. Скопируйте в pgAdmin или psql, чтобы создать те же таблицы:

```sql
-- Таблица ACCOUNTS
CREATE TABLE accounts (
    account_id     BIGSERIAL PRIMARY KEY,
    user_id        BIGINT       NOT NULL,
    balance        DECIMAL(18, 2) NOT NULL DEFAULT 0.00,
    currency       VARCHAR(10)    NOT NULL,
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_accounts_user_id ON accounts(user_id);

CREATE OR REPLACE FUNCTION set_updated_at_accounts() 
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_accounts_updated_at
BEFORE UPDATE ON accounts
FOR EACH ROW
EXECUTE FUNCTION set_updated_at_accounts();

-- Создаём ENUM'ы (для транзакций)
CREATE TYPE transaction_type AS ENUM ('debit', 'credit');
CREATE TYPE transaction_status AS ENUM ('pending', 'completed', 'failed', 'canceled');

-- Таблица TRANSACTIONS
CREATE TABLE transactions (
    transaction_id BIGSERIAL PRIMARY KEY,
    account_id     BIGINT       NOT NULL,
    amount         DECIMAL(18, 2) NOT NULL,
    type           transaction_type NOT NULL,
    reference_id   BIGINT       DEFAULT NULL,
    status         transaction_status NOT NULL DEFAULT 'pending',
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

CREATE INDEX idx_transactions_account_id ON transactions(account_id);

CREATE OR REPLACE FUNCTION set_updated_at_transactions() 
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_transactions_updated_at
BEFORE UPDATE ON transactions
FOR EACH ROW
EXECUTE FUNCTION set_updated_at_transactions();
