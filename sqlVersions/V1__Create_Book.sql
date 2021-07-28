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
    distributor_id  integer REFERENCES distributor(id),
    publisher integer REFERENCES publisher(id)
);

CREATE TABLE book_author
(
    book_id integer REFERENCES book(id),
    author_id integer REFERENCES author(id),
    primary key (book_id, author_id)
)