CREATE TABLE IF NOT EXISTS state_payment_history
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    status VARCHAR(128),
    telegram_user_id BIGINT /*REFERENCES telegram_users (user_id)*/
)