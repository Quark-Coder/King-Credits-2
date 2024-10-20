CREATE TABLE IF NOT EXISTS state_image
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name_state VARCHAR,
    photo_data BYTEA
);

INSERT INTO state_image (name_state)
SELECT 'Главная страница'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Главная страница');

INSERT INTO state_image (name_state)
SELECT 'Пополнить баланс'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Пополнить баланс');

INSERT INTO state_image (name_state)
SELECT 'Профиль'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Профиль');

INSERT INTO state_image (name_state)
SELECT 'Вывод кредитов'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Вывод кредитов');

INSERT INTO state_image (name_state)
SELECT 'Купить скины'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Купить скины');

INSERT INTO state_image (name_state)
SELECT 'Кейсы и игры'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Кейсы и игры');

INSERT INTO state_image (name_state)
SELECT 'Актуальный курс'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Актуальный курс');

INSERT INTO state_image (name_state)
SELECT 'Промокод'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Промокод');

INSERT INTO state_image (name_state)
SELECT 'Таблица лидеров'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Таблица лидеров');

INSERT INTO state_image (name_state)
SELECT 'Посчитать'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Посчитать');

INSERT INTO state_image (name_state)
SELECT 'Помощь'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Помощь');

INSERT INTO state_image (name_state)
SELECT 'Продать кредиты'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Продать кредиты');

INSERT INTO state_image (name_state)
SELECT 'Отзывы'
    WHERE NOT EXISTS (SELECT 1 FROM state_image WHERE name_state = 'Отзывы');
