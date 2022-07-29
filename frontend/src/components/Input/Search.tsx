import {FormEvent, useState} from "react";
import "./Input.css"
import Input from "./Input";
import {FaSearch} from "react-icons/fa";
import {useNavigate} from "react-router-dom";
import {Button, Flex} from "@chakra-ui/react";


export default function Search(){
    const [query, setQuery] = useState("")
    const nav = useNavigate();


    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        nav("/main/" + query);
    }

    return (
        <form onSubmit={handleSubmit}>
            <Flex className={"search"} gap={0} p={4}>
               <Input Value={query} setValue={setQuery} label={"Search"}/><Button style={{height: "33px"}} type={"submit"}><FaSearch/></Button>
            </Flex>
        </form>
    )
}