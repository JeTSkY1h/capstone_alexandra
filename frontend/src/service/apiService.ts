import axios, {AxiosResponse} from "axios";
import {Book, LoginData, ResumeData} from "./models";

let requestConfig = {
    headers: {
        Authorization: `Bearer ${localStorage.getItem("jwt-alexandra")}`
    }
}

export function setBookData(bookData: ResumeData){
    return axios.put("/api/user/bookdata", bookData, requestConfig).then(res=>res.data)
}

export function getBookData(){
    return axios.get("/api/user/bookdata", requestConfig).then(res=>res.data)
}

export function parseJwt(){
    let token = localStorage.getItem("jwt-alexandra")
    if(token) {
        let base64Url = token.split('.')[1];
        let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        let jsonPayload = decodeURIComponent(window.atob(base64).split("").map((c) => {
            return "%" + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } else {
        return "No Logintoken found. Are ypu logged in?"
    }
}

export function getChapters(id: string){
    return axios.get("/api/books/" + id + "/chapter", requestConfig).then(res=>res.data)
}

export function getChapter(id: string, chapter: number){
    return axios.get(`/api/books/${id}/chapter/${chapter}`, requestConfig).then(res=>res.data)
}

export function getCover(id: string){
    return axios.get("/api/books/cover/"+ id,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("jwt-alexandra")}`
            }, responseType: "blob"
        })
}

export function getBook(id: string){
    return axios.get("/api/books/" + id, requestConfig).then(res=>res.data)
}

export function getBooks(){
    return axios.get("api/books", requestConfig).then((res: AxiosResponse<Book[]>)=>res.data);
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