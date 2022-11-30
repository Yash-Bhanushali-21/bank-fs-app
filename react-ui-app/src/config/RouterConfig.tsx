import { Route,Routes, BrowserRouter as Router } from 'react-router-dom'
import RegisterPage from '../Pages/RegisterPage/RegisterPage'
import LoginPage from 'src/Pages/LoginPage/LoginPage';
import HomePage from '../Pages/HomePage/HomePage';
import LoanPage from "../Pages/LoanPage/LoanPage";
import React from "react";

export default class RouterConfig extends React.Component {

    render() {
        return (
            <Router>
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/loans" element={<LoanPage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                </Routes>
          </Router>
        )
    }
}