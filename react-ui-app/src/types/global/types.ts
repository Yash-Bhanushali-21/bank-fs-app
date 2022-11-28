type RegisterUserPayload = {
    name : string,
    email : string,
    mobileNumber : number,
    pwd : string,
    role? : string
}

type LoginPayloadType = {
    username : string,
     password: string
}

type UserInfoType = {
    id : number,
    name : string,
    email : string,
    mobileNumber:  string,
    role : string,
    createDt : string,
}
export {RegisterUserPayload , UserInfoType , LoginPayloadType};