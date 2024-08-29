CREATE TABLE IF NOT EXISTS reviews
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    telegram_user_id BIGINT,
    payment_check_photo_id BIGINT,
    status VARCHAR(25),
    comment VARCHAR,
    created_at TIMESTAMP
)