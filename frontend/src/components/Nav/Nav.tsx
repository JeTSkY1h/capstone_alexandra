
import { Link, useNavigate } from "react-router-dom"
import "./Nav.css"

export default function Nav(){
    
    const nav = useNavigate();

    
    return (
        <nav>
            <div className="nav-logo-wrapper">
                <Link to="/Main">
                    <div className="logo"><h1>Alexandra</h1></div>
                </Link>
            </div>

            <button className="login-action btn" onClick={()=>nav("/login")}>Einloggen</button>
        </nav>
    )
}