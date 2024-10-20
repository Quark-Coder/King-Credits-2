CREATE TABLE IF NOT EXISTS payment_details
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name_bank_and_user VARCHAR,
    card_number VARCHAR,
    other_payment_details VARCHAR
);

INSERT INTO payment_details (name_bank_and_user, card_number, other_payment_details)
SELECT 'Сбербанк (Иван. А)', '2202203605740234', '@DreamCredits'
    WHERE NOT EXISTS (
    SELECT 1 FROM payment_details
);