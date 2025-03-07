DROP TABLE IF EXISTS POSTS CASCADE;
DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS IMAGES CASCADE;
DROP TABLE IF EXISTS CATEGORIES CASCADE;
DROP TABLE IF EXISTS LIKES CASCADE;
DROP TYPE IF EXISTS USER_ROLE CASCADE;
--DROP USER IF EXISTS BLOGGER;
--DROP DATABASE IF EXISTS BLOG;

CREATE TYPE USER_ROLE AS ENUM('AUTHOR', 'ADMIN');

--CREATE USER BLOGGER WITH PASSWORD 'password';
--CREATE DATABASE BLOG WITH TEMPLATE=TEMPLATE0 OWNER=BLOGGER;

ALTER DEFAULT PRIVILEGES GRANT ALL ON TABLES TO BLOGGER;
ALTER DEFAULT PRIVILEGES GRANT ALL ON SEQUENCES TO BLOGGER;

CREATE TABLE IF NOT EXISTS USERS (
	ID SERIAL PRIMARY KEY,
	USERNAME VARCHAR(50) UNIQUE NOT NULL,
	EMAIL VARCHAR(100) UNIQUE NOT NULL,
	PASSWORD TEXT NOT NULL,
	IMAGE_URL VARCHAR(256) DEFAULT NULL,
	ROLE USER_ROLE DEFAULT 'AUTHOR',
	IS_DELETED BOOLEAN DEFAULT FALSE,
	CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS IMAGES (
    ID SERIAL PRIMARY KEY,
    NAME TEXT NOT NULL,
    URL TEXT NOT NULL,
    ASSET_ID VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS CATEGORIES (
	ID SERIAL PRIMARY KEY,
	NAME VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS POSTS (
	ID SERIAL PRIMARY KEY,
	TITLE VARCHAR(255) NOT NULL,
	EXCERPT VARCHAR(255) NULL,
	CONTENT TEXT NOT NULL,
	POSTED_ON TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	LAST_UPDATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	READ_TIME SMALLINT DEFAULT 0,
	CATEGORY INTEGER NOT NULL,
	TAGS TEXT,
	STATUS VARCHAR(30) DEFAULT 'DRAFT',
	FEATURED BOOLEAN DEFAULT FALSE,
	IS_DELETED BOOLEAN DEFAULT FALSE,
	POSTER_CARD INTEGER NOT NULL,
	AUTHOR_ID INTEGER NOT NULL,
	FOREIGN KEY (AUTHOR_ID) REFERENCES USERS (ID),
	FOREIGN KEY (POSTER_CARD) REFERENCES IMAGES (ID),
	FOREIGN KEY (CATEGORY) REFERENCES CATEGORIES (ID)
);

CREATE TABLE IF NOT EXISTS LIKES (
   ID SERIAL PRIMARY KEY,
   POST_ID INTEGER NOT NULL,
   USER_ID INTEGER NOT NULL,
   FOREIGN KEY (POST_ID) REFERENCES POSTS (ID),
   FOREIGN KEY (USER_ID) REFERENCES USERS (ID)
);

INSERT INTO CATEGORIES (NAME) VALUES ('Technology');
INSERT INTO CATEGORIES (NAME) VALUES ('Programming');
INSERT INTO CATEGORIES (NAME) VALUES ('Design');
INSERT INTO CATEGORIES (NAME) VALUES ('Career');
INSERT INTO CATEGORIES (NAME) VALUES ('Productivity');
INSERT INTO CATEGORIES (NAME) VALUES ('Tutorial');
INSERT INTO CATEGORIES (NAME) VALUES ('Architecture');
INSERT INTO CATEGORIES (NAME) VALUES ('Best Practices');
INSERT INTO CATEGORIES (NAME) VALUES ('Development');

--CREATE TABLE COMMENTS (
--	ID SERIAL PRIMARY KEY,
--	CONTENT TEXT NOT NULL,
--	CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--	AUTHOR_ID INT NOT NULL,
--	POST_ID INT NOT NULL,
--	FOREIGN KEY (AUTHOR_ID) REFERENCES USERS (ID),
--	FOREIGN KEY (POST_ID) REFERENCES POSTS (ID)
--);
--
--CREATE TABLE TAGS (
--	ID SERIAL PRIMARY KEY,
--	NAME VARCHAR(100) UNIQUE NOT NULL
--);
--
--CREATE TABLE POST_TAGS (
--	POST_ID INT NOT NULL,
--	TAG_ID INT NOT NULL,
--	PRIMARY KEY (POST_ID, TAG_ID),
--	FOREIGN KEY (POST_ID) REFERENCES POSTS (ID),
--	FOREIGN KEY (TAG_ID) REFERENCES TAGS (ID)
--);
