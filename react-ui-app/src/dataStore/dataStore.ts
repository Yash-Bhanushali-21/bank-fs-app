
const globalDataStore = new Map();


export function setDataInStore(app_key : string , app_data : any) {
    globalDataStore.set(app_key , app_data);
}
export function getDataFromStore(app_key : string) {
    return globalDataStore.get(app_key);
}
