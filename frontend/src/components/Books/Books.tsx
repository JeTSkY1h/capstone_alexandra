import { Book } from "../../service/models";
import BookComp from "./BookComp";
import {Flex} from "@chakra-ui/react";

interface BooksProps {
    books: Book[]
}

export default function Books(props: BooksProps){

    return (
        <>
            <Flex p={8}>
                {props.books.map(book=>{
                    return <BookComp book={book}/>
                })}
            </Flex>
        </>
        
    )
}