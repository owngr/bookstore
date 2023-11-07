import handleErrors from "@/helpers/httpHelper";
import { SaleList } from "@/models/SaleDto";
import { ScraperBook } from "@/models/ScraperBook";

class StockService {
  //TODO implement Page object
  getStock(params: any): Promise<any> {
    console.debug("lazy params");
    console.debug(params);
    const queryParams: string = params
      ? Object.keys(params)
          .map(
            (k) => encodeURIComponent(k) + "=" + encodeURIComponent(params[k])
          )
          .join("&")
      : "";
    return fetch("/api/stock?" + queryParams)
      .then(handleErrors)
      .then((res) => res.json());
  }

  updateStock(body: ScraperBook): Promise<ScraperBook> {
    const requestOptions: RequestInit = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    };
    return fetch("/api/stock", requestOptions)
      .then(handleErrors)
      .then((res) => res.json());
  }

  addBook(body: ScraperBook): Promise<Response> {
    const requestOptions: RequestInit = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    };
    return fetch("/api/book/stock", requestOptions).then(handleErrors);
  }

  addCover(formData: FormData, isbn: string): Promise<Response> {
    const requestOptions: RequestInit = {
      method: "POST",
      body: formData,
      // headers: {
      //     "Content-Type": "multipart/form-data"
      // }
    };
    return fetch(
      "/api/book/cover?" + new URLSearchParams({ isbn }),
      requestOptions
    ).then(handleErrors);
  }

  deleteStock(): Promise<Response> {
    const requestOptions: RequestInit = {
      method: "DELETE",
    };
    return fetch("/api/stock", requestOptions).then(handleErrors);
  }

  getMissingBooks(sales: SaleList): Promise<Response> {
    const requestOptions: RequestInit = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(sales),
    };
    return fetch("/api/stock/missing", requestOptions);
  }

  sellBooks(sales: SaleList): Promise<Response> {
    const requestOptions: RequestInit = {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(sales),
    };
    return fetch("/api/stock", requestOptions).then(handleErrors);
  }
}

export default new StockService();
