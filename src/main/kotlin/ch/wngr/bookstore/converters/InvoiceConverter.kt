package ch.wngr.bookstore.converters

import ch.wngr.bookstore.entities.Invoice
import ch.wngr.bookstore.entities.Payment
import ch.wngr.bookstore.entities.Sale
import ch.wngr.bookstore.models.PaymentOptionDTO
import ch.wngr.bookstore.models.SaleDTO
import ch.wngr.bookstore.models.SaleList

fun Sale.toSaleDTO() = SaleDTO(
    id = id,
    isbn = book?.isbn,
    title = description,
    price = price,
    fullPrice = fullPrice,
    quantity = 1,
    new = !secondHand

)

fun Payment.toPaymentOptionDTO() = PaymentOptionDTO(
    value = paymentMethod.toString(),
    price = price
)


fun Invoice.toSaleList() = SaleList(
    sales = sales.toList().map { s -> s.toSaleDTO() },
    paymentMethod = payments.toList().map { p -> p.toPaymentOptionDTO() },
    priceWithDiscount = priceWithDiscount,
    priceWithoutDiscount = priceWithoutDiscount,
    priceDiscountPercent = priceDiscountPercent,
)