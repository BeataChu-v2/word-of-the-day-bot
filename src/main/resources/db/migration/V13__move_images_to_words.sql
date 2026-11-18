ALTER TABLE words
    ADD COLUMN image_file_id VARCHAR(255);

UPDATE words w
SET image_file_id = i.file_id
FROM images i WHERE i.word_id = w.id;

DROP TABLE images;