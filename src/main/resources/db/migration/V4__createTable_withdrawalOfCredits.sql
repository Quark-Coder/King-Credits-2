CREATE TABLE IF NOT EXISTS withdrawal_of_credits
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    telegram_user_id BIGINT,
    first_name VARCHAR,
    last_name VARCHAR,
    nickname VARCHAR,
    nick_in_game VARCHAR,
    price DOUBLE PRECISION,
    photo BYTEA,
    created_at TIMESTAMP,
    status VARCHAR(50)
)