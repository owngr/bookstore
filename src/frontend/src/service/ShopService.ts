import handleErrors from "@/helpers/httpHelper";

class ShopService {
  getShop(): Promise<ShopEntry[]> {
    return fetch("/api/shop")
      .then(handleErrors)
      .then((res) => res.json());
  }

  getPicture(isbn: string): Promise<Response> {
    return fetch("/api/shop/" + isbn).then(handleErrors);
  }
}

export default new ShopService();
