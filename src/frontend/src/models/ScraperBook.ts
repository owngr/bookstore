export class ScraperBook {
  isbn!: string;
  title!: string;
  authors: string[] = [];
  editor!: string | undefined;
  distributor: string | undefined;
  description!: string;
  price!: number;
  coverUrl!: string;
  hasCover!: boolean;
  amount!: number;
  tags!: TagDto[];
}
