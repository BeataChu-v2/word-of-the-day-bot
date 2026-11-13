-- Add a word GAME
INSERT INTO words (sequence_number, word, translation)
VALUES (
           4,
           'game',             -- English word
           'игра'       -- Russian translation
       );

-- Add collocations
INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'lose a game', 'проиграть', 1
FROM words WHERE sequence_number = 4;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'board game', 'настольная игра', 2
FROM words WHERE sequence_number = 4;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'video game', 'компьютерная игра', 3
FROM words WHERE sequence_number = 4;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, '"Game of Thrones"', '"Игра престолов"', 4
FROM words WHERE sequence_number = 4;

INSERT INTO collocations (word_id, text, translation, sort_order)
SELECT id, 'game over', 'игра окончена', 5
FROM words WHERE sequence_number = 4;

-- Add phrases
INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id, 'They hope to win the game.', 'Они надеются победить в игре', 1
FROM words WHERE sequence_number = 4;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id, 'My favorite game is chess', 'Моя любимая игра - шахматы.', 2
FROM words WHERE sequence_number = 4;

INSERT INTO phrases (word_id, text, translation, sort_order)
SELECT id, 'Who wants to play a game?', 'Кто хочет поиграть?', 3
FROM words WHERE sequence_number = 4;

-- Add image
INSERT INTO images (word_id, file_id, sort_order)
SELECT id, 'AgACAgIAAxkBAANzauxwaNzDOiEIcNS8ejY9Z6goQYsAAnMjaxtzyGhLITm7u7I5o00BAAMCAAN5AAM9BA', 1
FROM words WHERE sequence_number = 4;