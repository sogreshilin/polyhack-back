-- liquibase formatted SQL


-- changeset a.boltava:create-video

CREATE TABLE video
(
    id  BIGSERIAL PRIMARY KEY,
    url TEXT UNIQUE NOT NULL
);

CREATE INDEX video__url_index ON video (url);

-- rollback DROP INDEX video__url_index;
-- rollback DROP TABLE video;


-- changeset a.boltava:create-video-index

CREATE TABLE video_lemma_time
(
    id         BIGSERIAL PRIMARY KEY,
    video_id   BIGINT         NOT NULL REFERENCES video (id),
    lemma      VARCHAR(64)    NOT NULL,
    start_time NUMERIC(10, 6) UNIQUE NOT NULL
);


CREATE INDEX video_lemma_time__time_index ON video_lemma_time (lemma);
CREATE INDEX video_lemma_time__video_id_index ON video_lemma_time (video_id);

-- rollback DROP INDEX video_lemma_time__video_id_index;
-- rollback DROP INDEX video_lemma_time__time_index;
-- rollback DROP TABLE video_lemma_time;
