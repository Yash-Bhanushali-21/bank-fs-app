import React from "react";
import styles from "./styles/loginpage.module.css";
import {  getUserInfo , logout , logoutFromAuthServer} from "src/dataFetcher/loginFetcher";
import {UserInfoType} from "src/types/global/types";

type LoginPageStateProps = {
    email : string,
    password: string,
    userInfo : null | UserInfoType
}

class LoginPage extends React.Component<any,LoginPageStateProps> {

    constructor(props : any) {
        super(props);
        this.state = {
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
            ...this.state,
            email : "",
            password : "",
            userInfo : null
        })
    }

    async handleLogout() {
      logout();
      logoutFromAuthServer();
     this.resetState();
       
    }
    async handleLogin() {
      
        console.log("triggering")
       await getUserInfo({
        username : this.state.email,
        password:  this.state.password
    }).then((res) => {
        
        console.log(res.data)
        this.setState({
            ...this.state,
            userInfo: res.data
        })
      
    
       });

       
        
        
        
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
        return (<div className={styles.loginPageContainer}>
            <input placeholder="email" onChange={this.onEmailChange}/>
            <input placeholder="password" onChange={this.onPasswordChange} />
            <button onClick={this.handleLogin}>Login</button>

            <a href="http://localhost:8080/oauth2/authorization/google">Google</a>

            <div>
                {this.state.userInfo && (
                    <>
                    <span>{`You are logged in as ${this.state.userInfo.name}`}</span>
                    <button onClick={this.handleLogout}>logout</button>
                    </>
                )}
            </div>
            
           
        </div>)
    }
}

export default LoginPage;