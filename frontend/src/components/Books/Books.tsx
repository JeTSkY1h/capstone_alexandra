import { Book } from "../../service/models";
import BookComp from "./BookComp";

interface BooksProps {
    books: Book[]
}

export default function Books(props: BooksProps){
    return (
        <>
            {props.books.forEach(book=>{
                return <BookComp book={book}/>
            })}
        </>
        
    )
}