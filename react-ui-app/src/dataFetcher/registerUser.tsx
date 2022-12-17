
import {RegisterUserPayload} from "src/types/global/types";
import { appConstants } from "src/constants/appConstants";
import defaultHttpHeaderConfig from "./config/headerConfig";

export async function registerUser ( payload : RegisterUserPayload  ) {

  let newHeaderConfig =defaultHttpHeaderConfig;
  newHeaderConfig.append("Content-Type" , "application/json");
  try{
    const response = await fetch(process.env.REACT_APP_BACKEND_URL + appConstants.REGISTER_URL, {
        method: 'POST',
        headers: newHeaderConfig,
        body: JSON.stringify(payload) 
    });
    return response.json();
  }
  catch(ex) {
    console.log("Something Went Wrong : " + ex.toString());
  }


}


export async function getList () {


  try{
    const response = await fetch(process.env.REACT_APP_BACKEND_URL + "/list", {
        method: 'get',
        headers: defaultHttpHeaderConfig,
    });
    return response.json();
  }
  catch(ex) {
    console.log("Something Went Wrong : " + ex.toString());
  }


}