create table payment
(
    id             serial primary key,
    invoice_id     integer REFERENCES invoice (id),
    price          numeric,
    payment_method varchar(10)
);

INSERT INTO payment (invoice_id, price, payment_method)
SELECT id as invoice_id, price_without_discount as price, payment_method
FROM invoice;

alter table invoice
    drop column payment_method;