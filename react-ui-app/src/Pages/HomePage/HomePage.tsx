import React from "react";
import styles from "./styles/homepage.module.css";
import { getUser , logUserOut, logoutFromAuthServer} from "src/dataFetcher/loginFetcher";
import classnames from "classnames";
import { Link } from "react-router-dom";
import dataStoreConstants from "src/dataStore/dataStoreConstants";

type HomePageState = {
    user : any,
}
type HomePageProps = {
  
}

class HomePage extends React.Component<HomePageProps,HomePageState> {

    constructor(props : HomePageProps) {
        super(props);
        this.state = {
            user : null
        }
        this.handleLogout = this.handleLogout.bind(this);
    }

    async handleLogout() {
        
        if(sessionStorage.getItem(dataStoreConstants.LOGIN_PROVIDER_KEY) === "GOOGLE"){
            //trigger a logout request from the authorization server.
            await logoutFromAuthServer();
        }
        try{
            await logUserOut();
            sessionStorage.clear();
        }
        catch(e) {
            console.log(e);
        }

        this.setState({
            user : null
        })
        sessionStorage.clear();
        

   
    }

    componentDidMount(): void {

          getUser().then(response => {
            if(response.status === 200) {
                console.log(response.data)
                this.setState({
                    ...this.state,
                    user : response.data
                })
                
            }
        })
      
    }


    render() {


        const notLoggedInClasses =  classnames(styles.notLoggedIn,{
            [styles.show] : this.state.user === null
        })
        const loggedInClasses =  classnames(styles.loggedIn,{
            [styles.show] : this.state.user !== null
        })

        return (<div className={styles.homepageContainer}>
                    <div className={notLoggedInClasses}>
                        You are not Signed-In. Click<Link to={"/login"} className={notLoggedInClasses}>here</Link>to Sign-In.
                    </div>
                    <div className={loggedInClasses}>
                        Welcome, {this.state.user ?  this.state.user.email : ""}
                        <button onClick={this.handleLogout}>Logout</button>
                    </div>
               </div>)
    }
}

export default HomePage;