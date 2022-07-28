import {FormEvent, useState} from "react";
import "./Input.css"
import Input from "./Input";
import {Button, Group} from "@mantine/core";
import {FaSearch} from "react-icons/fa";
import {useNavigate} from "react-router-dom";




export default function Search(){
    const [query, setQuery] = useState("")
    const nav = useNavigate();


    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        nav("/main/" + query);
    }

    return (
        <form onSubmit={handleSubmit}>
            <Group className={"search"} spacing={0} noWrap>
               <Input Value={query} setValue={setQuery} label={"Search"}/><Button style={{height: "33px"}} type={"submit"}><FaSearch/></Button>
            </Group>
        </form>
    )
}