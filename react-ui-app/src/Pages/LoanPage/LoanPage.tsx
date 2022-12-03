import React from "react";
import styles from "./styles/loanspage.module.css";
import { getLoanDetails } from "src/dataFetcher/loansFetcher";

class LoanPage extends React.Component<any,any> {

    constructor(props : any) {
        super(props);
        this.state = {
            message : "Dummy Endpoint Payload."
        }
    }

    componentWillMount() {
        //fetch the stuff before paint.
       getLoanDetails().then(res => {
        console.log(res)
            this.setState({
                message : res.message
            })
        })
    }


    render() {
        return (<div className={styles.container}>
            {this.state.message}
        </div>)
    }
}

export default LoanPage;