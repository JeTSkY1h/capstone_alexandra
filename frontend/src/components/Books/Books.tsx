import { Book } from "../../service/models";
import BookComp from "./BookComp";

interface BooksProps {
    books: Book[]
}

export default function Books(props: BooksProps){

    return (
        <>
            <div className="books">
                {props.books.map(book=>{
                    return <BookComp book={book}/>
                })}
            </div>
        </>
        
    )
}