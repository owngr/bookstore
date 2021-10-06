
class EditorService {
    getAll() {
        return fetch("/api/editor")
    }
}

export default new EditorService();