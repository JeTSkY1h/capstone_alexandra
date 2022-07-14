import { Book } from "../../service/models"
import "./BookComp.css"
import {useEffect, useState} from "react";
import {getCover} from "../../service/apiService";
import {Link, NavLink} from "react-router-dom";


interface BookProps {
    book: Book;
}

export default function BookComp(props: BookProps){

    const [cover, setCover] = useState("");

    useEffect(()=>{
        getCover(props.book.id).then(res=>{
                let image = URL.createObjectURL(res.data);
                setCover(image);
        });
    },[props])

    return (
        <NavLink to={"/book/"+ props.book.id}>
            <div className="book-card">
                <div className="cover-img-wrapper">
                    <img src={cover} alt={"Buch Cover"}/>
                </div>
                <div className={"book-card-info-wrapper"}>
                    <div>
                        <h1>{props.book.title.length >= 40 ? props.book.title.slice(0,35) + "...": props.book.title}</h1>
                    </div>
                </div>

                <Link to={"/reader/" + props.book.id}>
                    <div>
                        Open Reader
                    </div>
                </Link>
            </div>

        </NavLink>




    )
}