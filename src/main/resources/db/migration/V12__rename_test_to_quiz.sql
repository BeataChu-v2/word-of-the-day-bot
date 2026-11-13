ALTER TABLE test_exercises
DROP CONSTRAINT fk_test_exercises_test;

ALTER TABLE test_exercises
DROP CONSTRAINT uq_test_exercises_order;

ALTER TABLE test_exercises
RENAME TO quiz_exercises;

ALTER TABLE tests
    RENAME TO quizzes;

ALTER TABLE quiz_exercises
RENAME COLUMN test_id TO quiz_id;

ALTER TABLE quiz_exercises
ADD CONSTRAINT fk_quiz_exercises_quiz
FOREIGN KEY (quiz_id)
REFERENCES quizzes(id)
ON DELETE CASCADE;

ALTER TABLE quiz_exercises
ADD CONSTRAINT uq_quiz_exercises_order
UNIQUE (quiz_id, sort_order);

ALTER TABLE user_progress
    RENAME COLUMN last_test_after_sequence_number
        TO last_quiz_sequence_number;
