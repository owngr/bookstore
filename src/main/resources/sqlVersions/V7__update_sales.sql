alter table invoice
add price_without_discount numeric,
add price_with_discount numeric,
add price_discount_percent numeric;

alter table sale
add description varchar(100),
add second_hand boolean;

alter table sale
drop column full_price;

alter table sale
add column full_price numeric;