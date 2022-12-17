import React from "react";
import styles from "./styles/homepage.module.css";
import { getUser , logUserOut, logoutFromAuthServer} from "src/dataFetcher/loginFetcher";
import { withCookies } from 'react-cookie';


type HomePageState = {
    user : any,
}
type HomePageProps = {
    cookies : any
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
        await logoutFromAuthServer();
        try{
            await logUserOut();
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
                this.setState({
                    user : response.data
                })
            }
        })
      
    }


    render() {
        return (<div className={styles.homepageContainer}>
            <span>{this.state?.user?.email ? this.state.user.email : "Logged Out."}</span>
            {this.state.user && <button onClick={this.handleLogout}>LOGOUT</button>}
        </div>)
    }
}

export default withCookies(HomePage);