import { useEffect, useState } from "react"
import Nav from "../components/Nav/Nav";
import Books from "../components/Books/Books";
import {getBooks, parseJwt} from "../service/apiService"
import { Book } from "../service/models";
import {useNavigate} from "react-router-dom";

export default function Main() {
    
    const [books, setBooks] = useState<Array<Book>>();
    const [err, setErr] = useState("");
    const nav = useNavigate();

    useEffect(()=>{
        getBooks().then(data=>setBooks(data)).catch(e=> {
            console.log(e)
            setErr(e.message);
        })
        let token = parseJwt();
        if(token.exp - (Date.now() / 1000) < 0){
            setErr("Login token is expired.")
            nav("/login")
        }
    },[nav])

    return(
        <>
            <Nav/>
            <div className={"main"}>
                {books ?  
                <Books books={books}/> :
                 err ? <p>{err}</p> :
                 <div> LOADING... </div>}
            </div>

        </>
    )
}