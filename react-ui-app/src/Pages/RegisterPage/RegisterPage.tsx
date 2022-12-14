import React from "react";
import styles from "./styles/registerpage.module.css";
import {RegisterUserPayload} from "src/types/global/types";
import {registerUser} from "src/dataFetcher/registerUser";
//@ts-ignore
import {ReactComponent as GoogleLogo} from "src/images/google_logo.svg";

type RegisterPageProps = {
    
}

const DEFAULT_ROLE = "Member";

class RegisterPage extends React.Component<RegisterPageProps,RegisterUserPayload> {

    constructor(props : RegisterPageProps) {
        super(props);
        this.state = {
            name : "",
            pwd: "",
            email : "",
            mobileNumber : -1,
        
        }
        //event bindings.
        this.handleSubmit = this.handleSubmit.bind(this);
        this.onUserNameChange = this.onUserNameChange.bind(this);
        this.onPassWordChange = this.onPassWordChange.bind(this);
        this.onEmailChange = this.onEmailChange.bind(this);
        this.onMobileNoChange = this.onMobileNoChange.bind(this);

    }

    async handleSubmit() {
     await registerUser( {
        name : this.state.name,
        email : this.state.email,
        mobileNumber:this.state.mobileNumber,
        pwd:  this.state.pwd,
        role : DEFAULT_ROLE
        })
          

    }
    onUserNameChange(e :  React.ChangeEvent<HTMLInputElement>) {
        this.setState({
            ...this.state,
            name : e.target.value
        })
    }
    onPassWordChange(e: React.ChangeEvent<HTMLInputElement>) {
        this.setState({
            ...this.state,
            pwd : e.target.value
        })
    }
    onEmailChange(e :  React.ChangeEvent<HTMLInputElement>) {
        this.setState({
            ...this.state,
            email : e.target.value
        })
    }
    onMobileNoChange(e: React.ChangeEvent<HTMLInputElement>) {
        this.setState({
            ...this.state,
            mobileNumber : parseInt(e.target.value)
        })
    }


    render() {
        return (
            <div className={styles.registerContainer}>
                <div className={styles.registerTextContainer}>
                    <span>Register</span>
                </div>
                <div className={styles.bodyContainer}>
                    <input onChange={this.onUserNameChange} placeholder="Name"/>
                    <input onChange={this.onPassWordChange} placeholder="Password" />
                    <input onChange={this.onEmailChange} placeholder="Email" />
                    <input onChange={this.onMobileNoChange} placeholder="Mobile" />
                    <button onClick={this.handleSubmit} className={styles.registerButton}>Register</button>
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
            </div>
        )
    }
}

export default RegisterPage;