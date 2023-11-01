class ShopEntry {
    isbn!: string;
    title!: string;
    authors!: string[];
    editor: string | undefined;
    description: string | undefined;
    price: number | undefined;
    available!: boolean;
}