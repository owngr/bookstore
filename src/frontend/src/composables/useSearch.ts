import {ref} from "vue"

export function useSearch(list: any, event: any) {
    console.log("use search")
    const filteredResult: any = ref([])
    console.log(list)
    if (!event.query.trim().length) {
        filteredResult.value = [...list]
    } else {
        list.forEach((e: string) => {
          if (e.toLowerCase().includes(event.query.toLowerCase())) {
            filteredResult.value.push(e);
          }
        });
    }
    console.log(filteredResult.value)
    return filteredResult
}
