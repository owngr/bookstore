import {ref,Ref} from "vue"

export function useSearch(list: never[], event: any) : Ref<string[]> {
    console.log("use search")
    const filteredResult = ref<string[]>([])
    console.log(list)
    if (!event.query.trim().length) {
        filteredResult.value = [...list]
    } else {
        list.forEach((e: string) : void => {
          if (e.toLowerCase().includes(event.query.toLowerCase())) {
            filteredResult.value.push(e);
          }
        });
    }
    console.log(filteredResult.value)
    return filteredResult
}
