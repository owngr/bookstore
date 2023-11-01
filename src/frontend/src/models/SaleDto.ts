class SaleDto {
    id: number | undefined;
    isbn: string | undefined;
    title!: string;
    price!: number;
    fullPrice!: number;
    quantity: number | undefined;
    new!: boolean;
}

class PaymentOptionDto {
    value!: string;
    price!: number;
}
class SaleList {
    sales!: SaleDto[];
    paymentMethod!: PaymentOptionDto[];
    priceWithDiscount!: number;
    priceWithoutDiscount!: number;
    priceDiscountPercent!: number;
}