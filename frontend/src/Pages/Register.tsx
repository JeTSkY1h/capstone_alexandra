import { FormEvent, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom"
import Input from "../components/Input/Input";
import { registerUser } from "../service/apiService";
import "./Register.css"

export default function Register(){
    
    let location = useLocation();
    
    const mail = location.search.replace("?mail=", "").trim();
    const [eMail, setEMail] = useState(mail? mail : "");
    const [pw, setPw] = useState("")
    const nav = useNavigate();
    
    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        registerUser({username: eMail, password: pw})
            .then(data=>localStorage.setItem("jwt-alexandra", data.token)).then(()=>nav("/main"))
            .catch((e)=>console.log(e));
        
    }

    return (
        <div className="content-wrapper">
            <div className="register-wrapper">
                <h1>Um deine Mitgliedschaft zu beginnen, brauchen wir noch ein Passwort von dir.</h1>
                <p>Du bist nurnoch wenige Schritte von deinem Lieblingsbuch entfernt.</p>
                <form onSubmit={handleSubmit}>
                    <Input Value={eMail} setValue={setEMail} label="E-Mail"/>
                    <Input Value={pw} setValue={setPw} label="Password" type="password" autofocus={true}/>
                    <input className="btn register-btn" type={"submit"} value={"Los Geht's!"}/>
                </form>
            </div>
        </div>
    )
}