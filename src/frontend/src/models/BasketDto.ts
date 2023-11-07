import { BasketRow } from "@/models/BasketRow";

export class BasketDto {
  id!: number;
  dateClosed!: string;
  distributor!: string;
  books!: BasketRow[];
  title!: string;
}
