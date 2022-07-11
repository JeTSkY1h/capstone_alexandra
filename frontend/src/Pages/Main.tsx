import { useEffect, useState } from "react"
import Nav from "../components/Nav/Nav";
import Books from "../components/Books/Books";
import { getBooks } from "../service/apiService"
import { Book } from "../service/models";

export default function Main() {
    
    const [books, setBooks] = useState<Array<Book>>();
    const [err, setErr] = useState("");

    useEffect(()=>{
        getBooks().then(data=>setBooks(data)).catch(e=>setErr(e))
    },[])

    return(
        <>
            <Nav/>
            <div>
                {books ?  
                <Books books={books}/> :
                 err ? <p>Da ist etwas schief gelaufen.</p> :
                 <div> LOADING... </div>}
            </div>

        </>
    )
}