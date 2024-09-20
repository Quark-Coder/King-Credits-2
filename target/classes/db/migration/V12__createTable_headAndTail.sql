CREATE TABLE IF NOT EXISTS head_and_tail
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    telegram_user_id BIGINT,
    created_at TIMESTAMP,
    amount DOUBLE PRECISION,
    status VARCHAR
)