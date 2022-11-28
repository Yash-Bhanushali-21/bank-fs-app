let xsrf = sessionStorage.getItem("XSRF-TOKEN");

//this config needs to be for all the requests that are authenticated.
let defaultHttpHeaderConfig = new Headers();
if(xsrf) defaultHttpHeaderConfig.append("X-XSRF-TOKEN" , xsrf);


export default defaultHttpHeaderConfig;