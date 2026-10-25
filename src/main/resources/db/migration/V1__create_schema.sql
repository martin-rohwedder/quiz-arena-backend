-- =========================================================
-- Quiz Arena - Initial database schema
-- =========================================================

-- ---------------------------------------------------------
-- Quizzes
-- Stores quizzes that can be used to start a game.
-- ---------------------------------------------------------
CREATE TABLE quizzes
(
    id          UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    title       VARCHAR(100) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_quizzes_status
        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);

-- ---------------------------------------------------------
-- Questions
-- Each question belongs to a quiz.
-- question_order defines the order of questions in a quiz.
-- ---------------------------------------------------------
CREATE TABLE questions
(
    id                 UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    quiz_id            UUID         NOT NULL,
    question_text      VARCHAR(500) NOT NULL,
    question_order     INTEGER      NOT NULL,
    time_limit_seconds INTEGER      NOT NULL DEFAULT 20,
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_questions_quiz
        FOREIGN KEY (quiz_id)
            REFERENCES quizzes (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_questions_quiz_order
        UNIQUE (quiz_id, question_order),

    CONSTRAINT chk_questions_order
        CHECK (question_order > 0),

    CONSTRAINT chk_questions_time_limit
        CHECK (time_limit_seconds > 0)
);

-- ---------------------------------------------------------
-- Answer options
-- Each question has multiple possible answers.
-- The correct answer must never be exposed to players
-- before they have answered.
-- ---------------------------------------------------------
CREATE TABLE answer_options
(
    id           UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    question_id  UUID         NOT NULL,
    answer_text  VARCHAR(300) NOT NULL,
    option_order INTEGER      NOT NULL,
    is_correct   BOOLEAN      NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_answer_options_question
        FOREIGN KEY (question_id)
            REFERENCES questions (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_answer_options_question_order
        UNIQUE (question_id, option_order),

    CONSTRAINT chk_answer_options_order
        CHECK (option_order > 0)
);

-- ---------------------------------------------------------
-- Game rooms
-- Stores the room and its selected quiz.
-- Live player state and scores can initially stay in memory.
-- ---------------------------------------------------------
CREATE TABLE game_rooms
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    room_code   VARCHAR(8)  NOT NULL,
    quiz_id     UUID        NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'WAITING',
    created_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at  TIMESTAMPTZ,
    finished_at TIMESTAMPTZ,

    CONSTRAINT uq_game_rooms_room_code
        UNIQUE (room_code),

    CONSTRAINT fk_game_rooms_quiz
        FOREIGN KEY (quiz_id)
            REFERENCES quizzes (id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_game_rooms_status
        CHECK (status IN ('WAITING', 'IN_PROGRESS', 'FINISHED'))
);

-- ---------------------------------------------------------
-- Indexes
-- ---------------------------------------------------------
CREATE INDEX idx_game_rooms_quiz_id
    ON game_rooms (quiz_id);

CREATE INDEX idx_game_rooms_status
    ON game_rooms (status);

CREATE INDEX idx_game_rooms_created_at
    ON game_rooms (created_at);
