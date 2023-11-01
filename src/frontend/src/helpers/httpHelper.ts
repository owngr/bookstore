function handleErrors(response: any) {
    if (!response.ok) {
        return response.json()
         .then((res: any) => {
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