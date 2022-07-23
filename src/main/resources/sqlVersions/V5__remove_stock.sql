/* We amount will be saved in book and therefore the stock table isn't used anymore */

ALTER table book
    add amount integer;

update book
set amount = s.amount
    from stock s
where book.id = s.book_id;

drop table stock;