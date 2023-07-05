import handleErrors from "@/helpers/httpHelper";

class TagService {
  getTags() {
    return fetch("/api/tag")
      .then(handleErrors)
      .then((res) => res.json());
  }

  updateTag(tag) {
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(tag),
    };
    return fetch("/api/tag", requestOptions)
      .then(handleErrors)
      .then((res) => res.json());
  }

  addTag(tag) {
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(tag),
    };
    return fetch("/api/tag", requestOptions)
      .then(handleErrors)
      .then((res) => res.json());
  }
}

export default new TagService();
