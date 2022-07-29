import { useEffect, useState } from "react"
import Nav from "../components/Nav/Nav";
import Books from "../components/Books/Books";
import {getBooks, parseJwt, searchBook} from "../service/apiService"
import { Book } from "../service/models";
import {useNavigate, useParams} from "react-router-dom";
import {Box} from "@chakra-ui/react";

export default function Main() {
    
    const [books, setBooks] = useState<Array<Book>>();
    const [err, setErr] = useState("");
    const nav = useNavigate();
    const {query} = useParams()

    useEffect(()=>{
        if(query){
            searchBook(query).then(data=>setBooks(data)).catch(e=> {
                console.log(e);
                setErr(e.message);
            })
        } else {
            getBooks().then(data => setBooks(data)).catch(e => {
                console.log(e)
                setErr(e.message);
            })
        }

        let token = parseJwt();
        if(token.exp - (Date.now() / 1000) < 0){
            setErr("Login token is expired.")
            nav("/login")
        }
    },[nav, query])

    return(
        <Box>
            <Nav/>
            <div className={"main"}>
                {books ?  
                <Books books={books}/> :
                 err ? <p>{err}</p> :
                 <div> LOADING... </div>}
            </div>

        </Box>
    )
}