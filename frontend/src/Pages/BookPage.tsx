import Nav from "../components/Nav/Nav";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getBook, getCover} from "../service/apiService";
import {Book} from "../service/models";
import "./BookPage.css";

export default function BookPage(){
    const {id} = useParams();
    const [book, setBook] = useState<Book>();
    const [cover, setCover] = useState("");

    useEffect(()=>{
        if(id) {
            console.log(id);
            getBook(id).then(data => {
                console.log(data);
                setBook(data)
            });

            getCover(id).then(res => {
                let image = URL.createObjectURL(res.data);
                setCover(image);
            });
        }
    },[id])


    return (
        <>
        <Nav/>
            <div className={"content-wrapper book-page"}>
                <div>
                    <div className={"img-wrapper"}>
                        <img src={cover} alt={"book cover"}/>
                    </div>
                    <div className={"book-page-info"}>
                        <h1>{book?.title}</h1>
                        <p>{book?.author}</p>
                    </div>
                </div>
            </div>
            <div  className={"desc book-page"}>{book?.description}</div>

        </>
    )
}