import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";

export default function LoginComp(){

    const [loginState, setLoginState] = useState(false);
    const nav = useNavigate();

    useEffect(()=>{
        if(localStorage.getItem("jwt-alexandra")){
            setLoginState(true);
        } else {
            setLoginState(false);
        }
    },[loginState]);

    const handleClick = () => {
        localStorage.clear();
        nav("/login");
    }

    return (
        <>
        {loginState ? <button className="login-action btn" onClick={handleClick}> Ausloggen </button> :
        <button className="login-action btn" onClick={()=>nav("/login")}>Einloggen</button>
        }
        </>
    )
}