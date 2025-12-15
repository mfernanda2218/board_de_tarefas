-- V1__Initial_schema.sql

-- Tabela board
CREATE TABLE IF NOT EXISTS board (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);
-- Tabela de colunas
CREATE TABLE IF NOT EXISTS task_column (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    order_position INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    board_id BIGINT NOT NULL,
    CONSTRAINT fk_column_board FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE
);
-- Tabela card
CREATE TABLE IF NOT EXISTS card (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    order_position INT NOT NULL,
    column_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_card_column FOREIGN KEY (column_id) REFERENCES task_column(id) ON DELETE CASCADE
);
-- Tabela de usuários (se necessário)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- Tabela de relacionamento entre usuários e cards (se necessário)
CREATE TABLE IF NOT EXISTS user_card (
    user_id BIGINT NOT NULL,
    card_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, card_id),
    CONSTRAINT fk_user_card_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_card_card FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE
);