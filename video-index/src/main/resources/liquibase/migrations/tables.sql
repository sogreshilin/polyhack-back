-- liquibase formatted SQL


-- changeset a.boltava:create-multi_media_file

CREATE TABLE multi_media_file
(
    id                 BIGSERIAL PRIMARY KEY,
    external_video_url TEXT UNIQUE NOT NULL,
    internal_audio_url TEXT UNIQUE,
    status             TEXT        NOT NULL,
    message            TEXT
);

CREATE INDEX multi_media_file__external_video_url_index ON multi_media_file (external_video_url);

-- rollback DROP INDEX multi_media_file__external_video_url_index;
-- rollback DROP TABLE multi_media_file;


-- changeset a.boltava:create-video-index

CREATE TABLE video_lemma_time
(
    id                  BIGSERIAL PRIMARY KEY,
    multi_media_file_id BIGINT         NOT NULL REFERENCES multi_media_file (id),
    lemma               VARCHAR(64)    NOT NULL,
    start_time          NUMERIC(10, 6) NOT NULL
);


CREATE INDEX video_lemma_time__time_index ON video_lemma_time (lemma);
CREATE INDEX video_lemma_time__multi_media_file_id_index ON video_lemma_time (multi_media_file_id);

-- rollback DROP INDEX video_lemma_time__multi_media_file_id_index;
-- rollback DROP INDEX video_lemma_time__time_index;
-- rollback DROP TABLE video_lemma_time;
