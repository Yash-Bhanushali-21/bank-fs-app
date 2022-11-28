import React from "react";
import styles from "./styles/homepage.module.css";

type HomePageState = {
    test : string,
}
type HomePageProps = {
    
}

class HomePage extends React.Component<HomePageProps,HomePageState> {

    constructor(props : HomePageProps) {
        super(props);
        this.state = {
            test : "hello"
        }
    }


    render() {
        return (<div className={styles.homepageContainer}>
            Hello World!
        </div>)
    }
}

export default HomePage;