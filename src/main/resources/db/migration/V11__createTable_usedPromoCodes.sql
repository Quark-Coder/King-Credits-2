CREATE TABLE IF NOT EXISTS used_promo_codes
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    code VARCHAR,
    created_at TIMESTAMP,
    telegram_user_id BIGINT
)