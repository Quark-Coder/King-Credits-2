CREATE TABLE IF NOT EXISTS promo_code
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    code VARCHAR,
    created_at TIMESTAMP,
    end_date TIMESTAMP,
    count_users integer,
    prize DOUBLE PRECISION,
    status VARCHAR(25)
)