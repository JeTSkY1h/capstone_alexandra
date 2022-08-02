import { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import Input from "../components/Input/Input";
import Nav from "../components/Nav/Nav";
import { login } from "../service/apiService";
import "./Login.css"

export default function Login(){
    const [username, setUsername] = useState("");
    const [pw, setPw] = useState("");

    const nav = useNavigate();

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        login({username: username, password: pw}).then((data)=>localStorage.setItem("jwt-alexandra", data.token)).then(()=>nav("/main"))
        .catch(e=>console.log(e))
    }

    return (
            <>  
                <Nav noSearch={true}/>
                <div className="react-content-wrapper">
                    <div className="login-card">
                        <h1>Einloggen</h1>
                        <form onSubmit={handleSubmit}>
                            <Input Value={username} setValue={setUsername} label={"Username"}/>
                            <Input Value={pw} setValue={setPw} label={"Password"} type="password"/>
                            <input className="btn login-btn" type={"submit"} value="Login"/>
                        </form>
                    </div>
                </div>
            </>
    )
}