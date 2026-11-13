-- Add a word FEEL
INSERT INTO words (sequence_number, word, translation)
VALUES (
           6,
           'to feel',             -- English word
           'чувствовать'       -- Russian translation
       );

-- Add collocations
INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'to feel happy', 'чувствовать себя счастливым', 1
FROM words WHERE sequence_number = 6;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'to feel blue', 'грустить (цвет настроения синий)', 2
FROM words WHERE sequence_number = 6;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'to feel at home', 'чувствовать себя как дома', 3
FROM words WHERE sequence_number = 6;

-- Add phrases
INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id, 'I feel like eating.', 'Мне хочется поесть.', 1
FROM words WHERE sequence_number = 6;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id, 'I feel for you.', 'Я тебе сочувствую.', 2
FROM words WHERE sequence_number = 6;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id, 'I feel sick.', 'Меня тошнит.', 3
FROM words WHERE sequence_number = 6;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id, 'I hope you feel better soon.', 'Надеюсь, ты скоро поправишься.', 4
FROM words WHERE sequence_number = 6;

-- Add image
INSERT INTO images (word_id, file_id, sort_order)
SELECT id, 'AgACAgIAAxkBAAN7auyngSqL2ltoaN7e7OqEIVl7Xo4AAqgkaxtzyGhLy5bx9HSHRsoBAAMCAAN5AAM9BA', 1
FROM words WHERE sequence_number = 6;