import {FormEvent, useEffect, useState} from "react";
import { useNavigate } from "react-router-dom";
import Input from "../components/Input/Input";
import Nav from "../components/Nav/Nav";
import "./Landing.css"

export default function Landing(){

    const [mail, setMail] = useState("");
    let nav = useNavigate();

    useEffect(()=>{
        if(localStorage.getItem("jwt-alexandra")) {
            nav("/main")
        }
    },[])


    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        nav({
            pathname: "/register",
            search: "?mail=" + mail
        })
    }

    return (
        <>
        <Nav />
            <div className="react-content-wrapper">
                <div className="landing-card">
                    <h1 className="landing-heading">Unendlich viele Bücher und Zeitschriften</h1>
                    <h3 className="landing-slogan">Lese Alle deine Lieblingsbücher, zu jeder Zeit, an jedem Ort</h3>
                    <p className="landing-desc">Bereit zu starten? gib einfach deine E-Mail ein und es geht los!</p>
                    <form className="landing-form" onSubmit={handleSubmit}>
                        <Input Value={mail} setValue={setMail} label="E-Mail"/><input className="landing-form-btn btn" type={"submit"} value={"Starten!"}/>
                    </form>
                </div>
            </div>
        </>
    )
}