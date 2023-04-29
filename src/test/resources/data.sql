INSERT INTO PUBLIC.GENRE (GENRE_NAME) VALUES
	 ('Комедия'),
	 ('Драма'),
	 ('Мультфильм'),
	 ('Триллер'),
	 ('Документальный'),
	 ('Боевик');
	 
INSERT INTO PUBLIC.MPA (MPA_NAME) VALUES
	 ('G'),
	 ('PG'),
	 ('PG-13'),
	 ('R'),
	 ('NC-17');

INSERT INTO PUBLIC.FILM (NAME,DESCRIPTION,RELEASE_DATE,DURATION,MPA_ID) VALUES
	 ('Фильм1','Описание 1','1999-09-09',110,2),
	 ('Фильм2','Описание2','1990-05-05',120,3);

INSERT INTO PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES
    ('user1@dom.com', 'user1', 'name1', '2000-02-02'),
    ('user2@dom.com', 'user2', 'name2', '1995-08-08'),
    ('user3@dom.com', 'user3', 'name3', '1955-09-09');

INSERT INTO PUBLIC.FILM_GENRE (FILM_ID, GENRE_ID) VALUES
    (1, 2),
    (1, 4),
    (2, 5);

INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID) VALUES
    (1, 1),
    (1, 2),
    (2, 3);

INSERT INTO PUBLIC.FRIENDS (REQUESTER_ID, ACCEPTER_ID, STATUS) VALUES
    (1, 2, 'requested'),
    (1, 3, 'accepted'),
    (2, 3, 'accepted');

INSERT INTO PUBLIC.REVIEWS (CONTENT, IS_POSITIVE, USER_ID, FILM_ID) VALUES
    ('This film is not too bad.', true, 1, 1),
    ('This film is soo bad.', false, 2, 1),
    ('This film is beatiful.', true, 1, 2);

INSERT INTO PUBLIC.USER_ACTIVITIES (USER_ID, EVENT_TYPE, TIMESTAMP, OPERATION, ENTITY_ID) VALUES
    (1, 'REVIEW', '2023-01-01 10:00:00', 'ADD', 1),
    (2, 'REVIEW', '2023-01-01 10:30:00', 'ADD', 1),
    (1, 'REVIEW', '2023-01-01 11:00:00', 'ADD', 2),
    (1, 'REVIEW', '2023-01-01 11:30:00', 'UPDATE', 1),
    (1, 'REVIEW', '2023-01-01 12:00:00', 'REMOVE', 1);