CREATE SCHEMA IF NOT EXISTS mini_netflix;

CREATE TABLE mini_netflix.video_metadata (
    videoId UUID PRIMARY KEY,
    title VARCHAR(256) NOT NULL,
    description VARCHAR(256),
    url VARCHAR(256) NOT NULL,
    thumbnailUrl VARCHAR(256),
    genre VARCHAR(256),
    author VARCHAR(256),
    publisher VARCHAR(256),
    createdTime BIGINT,
    updatedTime BIGINT,
    showingFromTime BIGINT,
    showingToTime BIGINT
)