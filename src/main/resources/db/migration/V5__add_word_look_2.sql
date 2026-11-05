-- V5: Add the word - a look

-- 1. Add the word
INSERT INTO words (sequence_number, word, translation)
VALUES (2, 'look', 'взгляд, вид');


-- 2. Add collocations
INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'to have a look', 'посмотреть', 1
FROM words
WHERE sequence_number = 2;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'to take a look', 'взглянуть, чтобы проверить; изучить', 2
FROM words
WHERE sequence_number = 2;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'fresh look', 'свежий взгляд (на проблему); свежий внешний вид', 3
FROM words
WHERE sequence_number = 2;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'surprised look', 'удивленный взгляд', 4
FROM words
WHERE sequence_number = 2;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'New Look', 'стиль, придуманный Кристианом Диором в конце 40-ых годов. Женщины стали выглядеть по-новому после строгой, маскулинной моды времён Второй мировой войны.', 5
FROM words
WHERE sequence_number = 2;


-- 3. Add phrases
INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'Can I have a look?',
       'Можно посмотреть?',
       1
FROM words
WHERE sequence_number = 2;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'Take a closer look at this photo.',
       'Взгляни внимательнее на эту фотографию',
       2
FROM words
WHERE sequence_number = 2;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'She likes a natural look with little makeup.',
       'Ей нравится выглядеть естественно с минимумом макияжа.',
       3
FROM words
WHERE sequence_number = 2;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id,
       'He gave me a strange look.',
       'Он как-то странно посмотрел на меня.',
       4
FROM words
WHERE sequence_number = 2;

-- 4. Add an image
INSERT INTO images (word_id, file_id, description, sort_order)
SELECT id,
       'AgACAgIAAxkBAAM1aut-taw_dHPv7Rl0acb20nmmzU0AAnkkaxv1c1lLck5sehJg0H8BAAMCAAN4AAM9BA',
       'Eye icon illustrating the word look',
       1
FROM words
WHERE sequence_number = 2;



