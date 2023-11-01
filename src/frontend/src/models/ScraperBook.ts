
class ScraperBook {
    isbn!: string;
    title!: string;
    authors!: string[];
    editors!: string | undefined;
    distributor: string | undefined;
    description!: string;
    price!: number;
    coverUrl!: string;
    hasCover!: boolean;
    amount!: number;
    tags!: TagDto[];
}