
import {RegisterUserPayload} from "src/types/global/types";
import { appConstants } from "src/constants/appConstants";

export async function registerUser ( payload : RegisterUserPayload  ) {
  try{
    const response = await fetch(process.env.REACT_APP_BACKEND_URL + appConstants.REGISTER_URL, {
        method: 'POST',
        mode: 'cors', 
        cache: 'no-cache',
        credentials: 'include',
        headers: {
        'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload) 
    });
    return response.json();
  }
  catch(ex) {
    console.log("Something Went Wrong : " + ex.toString());
  }


}