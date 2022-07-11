import axios from "axios";
import { LoginData } from "./models";

let requestConfig = {
    headers: {
        Authorization: `Bearer ${localStorage.getItem("jwt-alexandra")}`
    }
}

export function getBooks(){
    return axios.get("api/book", requestConfig).then(res=>res.data);
}

export function login(user: LoginData){
    return axios.post("api/auth", user).then(res=>res.data)
}

export function registerUser(user: LoginData){
    return axios.post("api/user", user).then(res => res.data)
}

export function getLogedInUsername(){
    return axios.get("/api/user", requestConfig).then(res => res.data)
}