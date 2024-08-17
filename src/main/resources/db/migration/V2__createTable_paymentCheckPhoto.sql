CREATE TABLE IF NOT EXISTS payment_check_photo
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    telegram_user_id BIGINT,
    price DOUBLE PRECISION,
    photo_data BYTEA,
    created_at TIMESTAMP,
    status VARCHAR(25)
)