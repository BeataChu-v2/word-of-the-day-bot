-- V2: Add the first word - To look

-- 1. Add the word
INSERT INTO words (sequence_number, word, translation)
VALUES (1, 'to look', 'смотреть; выглядеть');


-- 2. Add collocations
INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'look at', 'смотреть на (кого-то/что-то)', 1
FROM words
WHERE sequence_number = 1;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'look for', 'искать', 2
FROM words
WHERE sequence_number = 1;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'look up', 'смотреть наверх', 3
FROM words
WHERE sequence_number = 1;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'look happy', 'выглядеть счастливым', 4
FROM words
WHERE sequence_number = 1;


-- 3. Add phrases
INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'This street looks familiar',
       'Эта улица выглядит знакомой.',
       1
FROM words
WHERE sequence_number = 1;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'You look great today!',
       'Ты сегодня отлично выглядишь!',
       2
FROM words
WHERE sequence_number = 1;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'Look at me!',
       'Посмотри на меня',
       3
FROM words
WHERE sequence_number = 1;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'You look like your father!',
       'Ты похож на отца.',
       4
FROM words
WHERE sequence_number = 1;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'I looked for my phone in the room, but I couldn''t find it.',
       'Я поискал телефон в комнате, но не нашел его.',
       5
FROM words
WHERE sequence_number = 1;


-- 4. Add an image
INSERT INTO images (word_id, image_url, description, sort_order)
SELECT id,
       'https://upload.wikimedia.org/wikipedia/commons/e/ed/Eye_icon.png',
       'Eye icon illustrating the word look',
       1
FROM words
WHERE sequence_number = 1;