import { Book } from "../../service/models"
import "./BookComp.css"

interface BookProps {
    book: Book;
}

export default function BookComp(props: BookProps){



    return (
        <div className="book-card">
            <div className="cover-img-wrapper">
                <img src={props.book.coverPath} alt="book Cover"/>
            </div>
            <div className={"book-card-info-wrapper"}>
                <div>
                    <h1>Title</h1>
                    <p>{props.book.title.length >= 40 ? props.book.title.slice(0,35) + "...": props.book.title}</p>
                </div>
                <div>
                    <h1>Author</h1>
                    <p>{props.book.author}</p>
                </div>
                <div>
                    <h1>Genre</h1>
                    {props.book.genre.filter((genre,i)=> i < 2).map(genre=><p> {genre} </p>)}
                </div>
            </div>
        </div>
    )
}