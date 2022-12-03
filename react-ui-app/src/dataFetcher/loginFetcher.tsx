
import { LoginPayloadType } from "src/types/global/types";
import { appConstants } from "src/constants/appConstants";
import defaultHttpHeaderConfig from "src/dataFetcher/config/headerConfig";
import { getCookie } from "typescript-cookie";
import { XSRFTokenException } from "src/exceptions/XSRFTokenException";


export async function getUserInfo({username, password} : LoginPayloadType) {


  let httpHeaders = new Headers();
  //sending the Authorization of type Basic, so that it hits Authorization Filter in the BE for first
  //processing req. of login via. authentication and then getting the info.
  httpHeaders.append('Authorization', 'Basic ' + window.btoa(username + ':' + password));



  try{
    const response = await fetch(process.env.REACT_APP_BACKEND_URL+appConstants.USER_INFO_URL, {
        credentials: 'include',  
        headers : httpHeaders
    });

      //store the XSRF token in session storage.
      const xsrf = getCookie("XSRF-TOKEN");
      if(xsrf) {
        window.sessionStorage.setItem("XSRF-TOKEN" , xsrf);
      }
      else {
       throw XSRFTokenException("not found!")
      }

      //get the response JWT token and store it.
      let JwtToken= response.headers.get("Authorization");
      if(JwtToken) {
        window.sessionStorage.setItem("Authorization", JwtToken);
      }

      console.log(response.json())

   
    return response.json();
  }
  catch(ex) {
    console.log("User Not Found : " + ex.toString());
  }


}


export async function logout() {


    try{
      const response = await fetch(process.env.REACT_APP_BACKEND_URL+appConstants.LOGOUT_URL, {
          method: 'get',
          mode: 'cors', 
          cache: 'no-cache',
          credentials: 'include',
          headers : defaultHttpHeaderConfig
          
      });
     
      return response.json();
    }
    catch(ex) {
      console.log("User Not Found : " + ex.toString());
    }
  
  
  }


//Presently Dyfunct loginWithCredentials()

export async function loginWithCredentials (  {username , password} : LoginPayloadType  ) {


  let httpHeaderConfig  = new Headers();
  httpHeaderConfig.append("Content-Type" , "application/x-www-form-urlencoded;charset=UTF-8");

  console.log("HERE");


  try{
    const response = await fetch(process.env.REACT_APP_BACKEND_URL+ appConstants.LOGIN_URL, {

        method: 'POST',
        mode: 'cors', 
        cache: 'no-cache',
        credentials: 'include',
        headers: httpHeaderConfig,
        body: new URLSearchParams({
            'username': username,
            'password': password,
        })
    });

    

    return response.json();
  }
  catch(ex) {
    console.log("Something Went Wrong : " + ex.toString());
  }


}
