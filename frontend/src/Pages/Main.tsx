import { useEffect, useState } from "react"
import Nav from "../components/Nav/Nav";
import { getLogedInUsername } from "../service/apiService"

export default function Main() {
    
    const [username, setUsername] = useState("");
    const [err, setErr] = useState("");



    useEffect(()=>{
        getLogedInUsername().then(data => {
            console.log(data);
            setUsername(data);
        }).catch((e)=>setErr(e));
    },[])

    return(
        <>
            <Nav/>
            <div>
                {username}
            </div>

        </>
    )
}