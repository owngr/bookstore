import handleErrors from "@/helpers/httpHelper";

class EditorService {
    getAll() {
        return fetch("/api/editor")
            .then(handleErrors)
    }
}

export default new EditorService();