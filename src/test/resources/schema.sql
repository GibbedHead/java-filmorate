DROP TABLE IF EXISTS PUBLIC.FRIENDS;
DROP TABLE IF EXISTS PUBLIC.FILM_LIKES;
DROP TABLE IF EXISTS PUBLIC.FILM_GENRE;
DROP TABLE IF EXISTS PUBLIC.DIRECTORS_FILMS;
DROP TABLE IF EXISTS DIRECTORS;
DROP TABLE IF EXISTS PUBLIC.USERS;
DROP TABLE IF EXISTS PUBLIC.FILM;
DROP TABLE IF EXISTS PUBLIC.MPA;
DROP TABLE IF EXISTS PUBLIC.GENRE;

CREATE TABLE PUBLIC.GENRE (
	GENRE_ID BIGINT NOT NULL AUTO_INCREMENT,
	GENRE_NAME CHARACTER VARYING(100) NOT NULL,
	CONSTRAINT GENRE_PK PRIMARY KEY (GENRE_ID)
);

CREATE TABLE PUBLIC.MPA (
	MPA_ID BIGINT NOT NULL AUTO_INCREMENT,
	MPA_NAME CHARACTER VARYING(50) NOT NULL,
	CONSTRAINT MPA_PK PRIMARY KEY (MPA_ID)
);

CREATE TABLE PUBLIC.FILM (
	FILM_ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(100) NOT NULL,
	DESCRIPTION CHARACTER VARYING(200),
	RELEASE_DATE DATE NOT NULL,
	DURATION INTEGER NOT NULL,
	MPA_ID BIGINT,
	CONSTRAINT FILM_PK PRIMARY KEY (FILM_ID),
	CONSTRAINT FILM_FK FOREIGN KEY (MPA_ID) REFERENCES PUBLIC.MPA(MPA_ID) ON DELETE SET NULL
);

CREATE TABLE PUBLIC.USERS (
	USER_ID BIGINT NOT NULL AUTO_INCREMENT,
	EMAIL CHARACTER VARYING(100) NOT NULL,
	LOGIN CHARACTER VARYING(100) NOT NULL,
	NAME CHARACTER VARYING(100) NOT NULL,
	BIRTHDAY DATE,
	CONSTRAINT USERS_PK PRIMARY KEY (USER_ID)
);

CREATE TABLE PUBLIC.FILM_GENRE (
	FILM_ID BIGINT NOT NULL,
	GENRE_ID BIGINT NOT NULL,
	CONSTRAINT FILM_GENRE_PK PRIMARY KEY (FILM_ID,GENRE_ID),
	CONSTRAINT FILM_GENRE_FILM_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILM(FILM_ID) ON DELETE CASCADE,
	CONSTRAINT FILM_GENRE_GENRE_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRE(GENRE_ID) ON DELETE CASCADE
);


CREATE TABLE PUBLIC.FILM_LIKES (
	FILM_ID BIGINT NOT NULL,
	USER_ID BIGINT NOT NULL,
	CONSTRAINT FILM_LIKES_PK PRIMARY KEY (FILM_ID,USER_ID),
	CONSTRAINT FILM_LIKES_FILM_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILM(FILM_ID) ON DELETE CASCADE,
	CONSTRAINT FILM_LIKES_USERS_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID) ON DELETE CASCADE
);

CREATE TABLE PUBLIC.FRIENDS (
	REQUESTER_ID BIGINT NOT NULL,
	ACCEPTER_ID BIGINT NOT NULL,
	STATUS CHARACTER VARYING(50) NOT NULL,
	CONSTRAINT FRIENDS_PK PRIMARY KEY (REQUESTER_ID,ACCEPTER_ID),
	CONSTRAINT FRIENDS_REQ_FK FOREIGN KEY (REQUESTER_ID) REFERENCES PUBLIC.USERS(USER_ID) ON DELETE CASCADE,
	CONSTRAINT FRIENDS_ACC_FK FOREIGN KEY (ACCEPTER_ID) REFERENCES PUBLIC.USERS(USER_ID) ON DELETE CASCADE
);

CREATE TABLE PUBLIC.DIRECTORS(
    director_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE PUBLIC.DIRECTORS_FILMS (
    director_id BIGINT,
    film_id BIGINT,
    FOREIGN KEY (film_id) REFERENCES PUBLIC.FILM (film_id) ON DELETE CASCADE,
    FOREIGN KEY (director_id) REFERENCES PUBLIC.DIRECTORS (director_id) ON DELETE CASCADE
)