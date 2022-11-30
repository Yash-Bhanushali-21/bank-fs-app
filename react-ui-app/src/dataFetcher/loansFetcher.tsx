import defaultHttpHeaderConfig from "./config/headerConfig";
import { appConstants } from "src/constants/appConstants";
export async function getLoanDetails() {



    try{
      const response = await fetch(process.env.REACT_APP_BACKEND_URL+ appConstants.LOANS_URL, {
        method:  'get',
        credentials : 'include',
          headers : defaultHttpHeaderConfig
      });
  
        
      return response.json();
    }
    catch(ex) {
      console.log("User Not Found : " + ex.toString());
    }
  
  
  }