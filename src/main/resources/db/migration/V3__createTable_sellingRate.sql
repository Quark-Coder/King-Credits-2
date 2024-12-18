CREATE TABLE IF NOT EXISTS selling_rate
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    zero_to_thousands DOUBLE PRECISION,
    thousand_to_five_thousand DOUBLE PRECISION,
    five_thousand_to_ten_thousand DOUBLE PRECISION,
    ten_thousand_or_more DOUBLE PRECISION
);

INSERT INTO selling_rate (zero_to_thousands, thousand_to_five_thousand, five_thousand_to_ten_thousand, ten_thousand_or_more)
SELECT 0.21, 0.21, 0.21, 0.21
    WHERE NOT EXISTS (
    SELECT 1 FROM selling_rate
);