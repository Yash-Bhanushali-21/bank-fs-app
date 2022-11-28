import React from "react";
import "./styles/app.css";
import RouterConfig from "./config/RouterConfig";
class App extends React.Component {


    render() {
        return (<div className={"app-container"}>
          <RouterConfig />
        </div>)
    }
}

export default App;