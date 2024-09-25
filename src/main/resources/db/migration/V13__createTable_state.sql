CREATE TABLE IF NOT EXISTS state_image
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name_state VARCHAR,
    photo_data BYTEA
);

INSERT INTO state_image (name_state) values ('Главная страница');
INSERT INTO state_image (name_state) values ('Пополнить баланс');
INSERT INTO state_image (name_state) values ('Профиль');
INSERT INTO state_image (name_state) values ('Вывод кредитов');
INSERT INTO state_image (name_state) values ('Купить скины');
INSERT INTO state_image (name_state) values ('Кейсы и игры');
INSERT INTO state_image (name_state) values ('Актуальный курс');
INSERT INTO state_image (name_state) values ('Промокод');
INSERT INTO state_image (name_state) values ('Таблица лидеров');
INSERT INTO state_image (name_state) values ('Посчитать');
INSERT INTO state_image (name_state) values ('Помощь');
INSERT INTO state_image (name_state) values ('Продать кредиты');
INSERT INTO state_image (name_state) values ('Отзывы');