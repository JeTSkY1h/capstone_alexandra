
import { Link } from "react-router-dom"
import "./Nav.css"
import LoginComp from "./LoginComp/LoginComp";


export default function Nav(){

    return (
        <nav>
            <div className="nav-logo-wrapper">
                <Link to="/Main">
                    <div className="logo"><h1>Alexandra</h1></div>
                </Link>
            </div>
            <LoginComp/>
        </nav>
    )
}