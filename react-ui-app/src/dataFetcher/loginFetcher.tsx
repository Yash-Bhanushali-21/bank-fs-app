
import { LoginPayloadType } from "src/types/global/types";
import { appConstants } from "src/constants/appConstants";

export async function loginWithCredentials (  {username , password} : LoginPayloadType  ) {

  try{
    const response = await fetch(process.env.REACT_APP_BACKEND_URL+ appConstants.LOGIN_URL, {
        method: 'POST',
        mode: 'cors', 
        cache: 'no-cache',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        },
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

export async function getUserInfo({username, password} : LoginPayloadType) {


  let httpHeaders = new Headers();
  httpHeaders.append('Authorization', 'Basic ' + window.btoa(username + ':' + password));


  try{
    const response = await fetch(process.env.REACT_APP_BACKEND_URL+appConstants.USER_INFO_URL, {
        credentials: 'include',  
        headers : httpHeaders
    });
   
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
          
      });
     
      return response.json();
    }
    catch(ex) {
      console.log("User Not Found : " + ex.toString());
    }
  
  
  }