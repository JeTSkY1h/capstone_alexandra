import { Book } from "../../service/models";
import BookComp from "./BookComp";
import {Button, Flex} from "@chakra-ui/react";
import {FaChevronLeft, FaChevronRight} from "react-icons/fa";
import {useRef} from "react";

interface BooksProps {
    books: Book[]
}

export default function Books( {books}: BooksProps){
    const ref = useRef<HTMLDivElement>(null);



    return (
        <>
            <Flex ref={ref} position={"relative"} p={8} wrap={"nowrap"} overflowY={"hidden"} overflowX={"scroll"} style={{scrollbarWidth: "none"}}>
                <Button p={0} pos={"fixed"} zIndex={10} left={0} alignSelf={"center"} bg={"none"} onClick={()=>ref.current!.scrollBy({top: 0, left: -150, behavior: "smooth" })}>
                    <FaChevronLeft size={32}/>
                </Button>
                {books.map((book, i)=>{
                    return <BookComp key={"book"+ i} book={book}/>
                })}
                <Button pos={"fixed"} p={0} right={0} alignSelf={"center"} bg={"none"} onClick={()=>ref.current!.scrollBy({top: 0, left: 150, behavior: "smooth" })}>
                    <FaChevronRight size={32}/>
                </Button>
            </Flex>
        </>
        
    )
}