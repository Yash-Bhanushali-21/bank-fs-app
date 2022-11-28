export function XSRFTokenException(message : string) {
    return {
        message, 
        name : "XSRF Token Not Found"
    }
}