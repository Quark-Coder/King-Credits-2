CREATE TABLE IF NOT EXISTS telegram_users
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT,
    chat_id BIGINT,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    nickname VARCHAR(255),
    created_at TIMESTAMP,
    status VARCHAR(25)
)