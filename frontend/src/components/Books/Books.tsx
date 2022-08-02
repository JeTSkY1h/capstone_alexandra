import { Book } from "../../service/models";
import BookComp from "./BookComp";
import {Flex} from "@chakra-ui/react";

interface BooksProps {
    books: Book[]
}

export default function Books(props: BooksProps){

    return (
        <>
            <Flex p={8} wrap={"wrap"}>
                {props.books.map((book, i)=>{
                    return <BookComp key={"book"+ i} book={book}/>
                })}
            </Flex>
        </>
        
    )
}