function handleErrors(response) {
    if (!response.ok) {
        return response.json()
         .then((res) => {
             if (res.localizedMessage) {
                 throw res.localizedMessage
             } else {
                 throw response.statusText
             }
         })
    }
    return response;
}

export default handleErrors;