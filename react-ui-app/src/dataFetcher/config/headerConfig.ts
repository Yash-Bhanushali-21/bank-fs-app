let xsrf = sessionStorage.getItem("XSRF-TOKEN");

let JWTToken = sessionStorage.getItem("Authorization");

//this config needs to be for all the requests that are authenticated.
let defaultHttpHeaderConfig = new Headers();
if(xsrf) defaultHttpHeaderConfig.append("X-XSRF-TOKEN" , xsrf);
if(JWTToken) defaultHttpHeaderConfig.append("Authorization" , JWTToken);

export default defaultHttpHeaderConfig;