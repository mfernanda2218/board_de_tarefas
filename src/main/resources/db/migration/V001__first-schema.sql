-- V1__Initial_schema.sql

CREATE TABLE board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Tabela de colunas com o nome correto
CREATE TABLE task_column (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    order_position INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    board_id BIGINT NOT NULL,
    CONSTRAINT fk_column_board FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE
);

CREATE TABLE card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    creation_date DATETIME NOT NULL,
    blocked BOOLEAN NOT NULL DEFAULT FALSE,
    column_id BIGINT NOT NULL,
    CONSTRAINT fk_card_column FOREIGN KEY (column_id) REFERENCES task_column(id)
);

CREATE TABLE card_movement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entry_time DATETIME NOT NULL,
    exit_time DATETIME,
    card_id BIGINT,
    column_id BIGINT,
    CONSTRAINT fk_movement_card FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE,
    CONSTRAINT fk_movement_column FOREIGN KEY (column_id) REFERENCES task_column(id)
);

CREATE TABLE blockage_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    `timestamp` DATETIME NOT NULL,
    reason TEXT,
    type VARCHAR(50) NOT NULL,
    card_id BIGINT,
    CONSTRAINT fk_blockage_card FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE
);