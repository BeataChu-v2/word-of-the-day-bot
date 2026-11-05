-- V7: Add the word - a chair

-- 1. Add the word
INSERT INTO words (sequence_number, word, translation)
VALUES (3, 'chair', 'стул');


-- 2. Add collocations
INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'wooden chair', 'деревянный стул', 1
FROM words
WHERE sequence_number = 3;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'electric chair', 'электрический стул', 2
FROM words
WHERE sequence_number = 3;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'high chair', 'детский стульчик для кормления', 3
FROM words
WHERE sequence_number = 3;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'favourite chair', 'любимый стул', 4
FROM words
WHERE sequence_number = 3;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'pull up a chair', 'усесться, присесть (дословно: подтянуть стул)', 5
FROM words
WHERE sequence_number = 3;


-- 3. Add phrases
INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'You can sit on this chair.',
       'Можете сесть на этот стул.',
       1
FROM words
WHERE sequence_number = 3;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'Let''s pull up chairs and relax.',
       'Давайте присядем и отдохнём.',
       2
FROM words
WHERE sequence_number = 3;

-- 4. Add an image
INSERT INTO images (word_id, file_id, sort_order)
SELECT id,
       'AgACAgIAAxkBAANRauuMYw48drohEBjypQABxWfHGqEBAAKuJGsb9XNZS1YH-OTiSuLHAQADAgADeAADPQQ',       1
FROM words
WHERE sequence_number = 3;