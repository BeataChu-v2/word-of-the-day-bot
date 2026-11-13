-- Add a word GAME
INSERT INTO words (sequence_number, word, translation)
VALUES (
           5,
           'bus',             -- English word
           'автобус'       -- Russian translation
       );

-- Add collocations
INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'take a bus', 'поехать на автобусе', 1
FROM words WHERE sequence_number = 5;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'get on a bus', 'сесть на автобус', 2
FROM words WHERE sequence_number = 5;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'change buses', 'сделать пересадку с автобуса на автобус', 3
FROM words WHERE sequence_number = 5;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'bus stop', 'автобусная остановка', 4
FROM words WHERE sequence_number = 5;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'bus ticket', 'автобусный билет', 5
FROM words WHERE sequence_number = 5;

-- Add phrases
INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id, 'The bus is coming.', 'Автобус подъезжает.', 1
FROM words WHERE sequence_number = 5;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id, 'Where is the bus stop?', 'Где автобусная остановка', 2
FROM words WHERE sequence_number = 5;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id, 'I take a bus to get to work', 'Я езжу на автобусе, чтобы добраться до работы', 3
FROM words WHERE sequence_number = 5;

-- Add image
INSERT INTO images (word_id, file_id, sort_order)
SELECT id, 'AgACAgIAAxkBAAN5auymnxdTI3DSSSHZ4AqVkysu4R8AAqEkaxtzyGhLizlK6AhpL-0BAAMCAAN5AAM9BA', 1
FROM words WHERE sequence_number = 5;