import handleErrors from "@/helpers/httpHelper";

class EditorService {
  getAll(): Promise<Response> {
    return fetch("/api/editor").then(handleErrors);
  }
}

export default new EditorService();
