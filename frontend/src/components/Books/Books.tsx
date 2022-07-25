import { Book } from "../../service/models";
import BookComp from "./BookComp";
import {Group} from "@mantine/core";

interface BooksProps {
    books: Book[]
}

export default function Books(props: BooksProps){

    return (
        <>
            <Group p={8}>
                {props.books.map(book=>{
                    return <BookComp book={book}/>
                })}
            </Group>
        </>
        
    )
}