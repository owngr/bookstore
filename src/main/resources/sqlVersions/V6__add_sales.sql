create table invoice
(
    id serial PRIMARY KEY,
    payment_method varchar(10),
    time_created timestamp with time zone
);

create table sale
(
    id serial primary key,
    invoice_id integer REFERENCES invoice(id),
    book_id integer REFERENCES book(id),
    price numeric,
    full_price boolean
)