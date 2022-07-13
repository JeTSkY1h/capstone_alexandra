import Nav from "../components/Nav/Nav";
import {useParams} from "react-router-dom";

export default function Reader(){

    const {id} = useParams()

    return (
        <>
            <Nav/>
            {id}
        </>
    )
}