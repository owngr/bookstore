CREATE TABLE basket
(
    id serial PRIMARY KEY,
    time_closed timestamp with time zone null
);

CREATE TABLE basket_book
(
    basket_id integer REFERENCES basket(id),
    book_id integer REFERENCES book(id),
    quantity integer,
    primary key (basket_id, book_id)
);

insert into basket
default values;