CREATE TABLE author
(
    id   serial PRIMARY KEY,
    name varchar(50)
);

CREATE TABLE distributor
(
    id   serial PRIMARY KEY,
    name varchar(50)
);

CREATE TABLE publisher
(
    id   serial PRIMARY KEY,
    name varchar(50)
);



CREATE TABLE book
(
    id   serial PRIMARY KEY,
    isbn varchar(13),
    title varchar(100),
    distributor_id serial REFERENCES distributor(id),
    publisher serial REFERENCES publisher(id)
);

CREATE TABLE book_author
(
    id      serial PRIMARY KEY,
    book_id serial REFERENCES book(id),
    author_id serial REFERENCES author(id)
)