import { ScraperBook } from "@/models/ScraperBook";

export class SaleDto {
  id!: number | undefined;
  isbn: string | undefined;
  title!: string;
  price!: number;
  fullPrice!: number;
  quantity: number | undefined;
  new!: boolean;
  originalBook!: ScraperBook;

  public isEqual(sale: SaleDto) {
    return (
      this.id === sale.id &&
      this.isbn === sale.isbn &&
      this.title === sale.title &&
      this.price === sale.price &&
      this.fullPrice === sale.fullPrice &&
      this.quantity === sale.quantity &&
      this.new === sale.new
    );
  }
}

export class PaymentOptionDto {
  value!: string;
  price!: number;
}

export class SaleList {
  sales!: SaleDto[];
  paymentMethod!: PaymentOptionDto[];
  priceWithDiscount!: number;
  priceWithoutDiscount!: number;
  priceDiscountPercent!: number;
}
