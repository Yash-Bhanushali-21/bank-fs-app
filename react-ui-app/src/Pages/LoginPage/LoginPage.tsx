import React from "react";
import styles from "./styles/loginpage.module.css";
import {  getUserInfo , logout , logoutFromAuthServer} from "src/dataFetcher/loginFetcher";
import {UserInfoType} from "src/types/global/types";
import classnames from "classnames";
import { Link  } from "react-router-dom";

//@ts-ignore
import {ReactComponent as GoogleLogo} from "src/images/google_logo.svg";
import dataStoreConstants from "src/dataStore/dataStoreConstants";

type LoginPageStateProps = {
        email : string,
    password: string,
    userInfo : null | UserInfoType,
    error : boolean
}

class LoginPage extends React.Component<any,LoginPageStateProps> {

    constructor(props : any) {
        super(props);
        this.state = {
            error : false,
            email : "",
            password : "",
            userInfo : null
        }

        this.handleLogin = this.handleLogin.bind(this);
        this.onPasswordChange = this.onPasswordChange.bind(this);
        this.onEmailChange = this.onEmailChange.bind(this);
        this.handleLogout = this.handleLogout.bind(this);


    }

    resetState() {
        this.setState({
            email : "",
            password : "",
            userInfo : null,
            error:false
        })
    }

    async handleLogout() {
      logout();
      logoutFromAuthServer();
      this.resetState();
     window.location.href= "http://localhost:3000/login";

       
    }
    async handleLogin() {
      
       await getUserInfo({
        username : this.state.email,
        password:  this.state.password
    }).then((res) => {
        console.log("response after getUserInfo");
        console.log(res);
        if(res.status === 200) {
            this.setState({
                ...this.state,
                userInfo: res.data
            })
            console.log("user is logged in.");
            sessionStorage.setItem(dataStoreConstants.LOGIN_PROVIDER_KEY , res.data.provider);
            window.location.href= "http://localhost:3000/";

        }
        else {
            this.setState({
                ...this.state,
                error : true
            })
            sessionStorage.clear();
        }
            
    })
 

        
        
        
    }
    onPasswordChange(e :React.ChangeEvent<HTMLInputElement>) {
        this.setState({
            ...this.state,
            password : e.target.value
        })
    }
    onEmailChange(e : React.ChangeEvent<HTMLInputElement>) {
        this.setState({
            ...this.state,
            email : e.target.value
        }) 

    }

 

    render() {

        const errorClassNames = classnames(styles.error , {
            [styles.show] : this.state.error
        })

        
        return (<div className={styles.loginPageContainer}>
            <div className={styles.loginTextContainer}>
                <span>Sign In</span>
               <span>Or, <Link to="/register">Create</Link> an Account</span>
            </div>
            <div className={styles.bodyContainer}>
                <input placeholder="email" onChange={this.onEmailChange}/>
                <input placeholder="password" onChange={this.onPasswordChange} />
                <div className={errorClassNames}>Wrong Credentials</div>
                <button onClick={this.handleLogin} className={styles.loginButton}>Sign In</button>
                <div className={styles.lineSeperator}>
                    <span className={styles.lineSeperatorContent}>
                        OR
                    </span>
                </div>
                <a   href={process.env.REACT_APP_BACKEND_URL+"/oauth2/authorization/google"} className={styles.buttonContainer}>
                    <span><GoogleLogo /></span>
                    <span>Sign In With Google</span>
                </a>
            </div>
            {/**
             * 
             * 
            <div>
                {this.state.userInfo && (
                    <>
                    <span>{`You are logged in as ${this.state.userInfo.name}`}</span>
                    <button onClick={this.handleLogout}>logout</button>
                    </>
                )}
            </div>
             */}
            
        </div>)
    }
}

export default LoginPage;