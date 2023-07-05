CREATE TABLE tag
(
    id serial primary key,
    name varchar(50),
    main boolean default false
);

CREATE TABLE book_tag
(
    book_id integer references book(id),
    tag_id integer references tag(id),
    primary key (book_id, tag_id)
);