import Nav from "../components/Nav/Nav";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getBook, getBookUserData} from "../service/apiService";
import {Book, ResumeData} from "../service/models";
import {Rating} from "../components/Rating/Rating";
import {Box, Container, Flex, Heading, Image, Stack, Text} from "@chakra-ui/react";

export default function BookPage(){
    const {id} = useParams();
    const [book, setBook] = useState<Book>();
    const [bookData, setBookData] = useState<ResumeData>();

    useEffect(()=>{
        if(id) {
            console.log(id);
            getBook(id).then(data => {
                console.log(data);
                setBook(data)
            });

            getBookUserData().then(data=>{
                console.log(data);
                let currData = data.filter((bookData: ResumeData) => bookData.bookId === id)[0]
                console.log(currData)
                setBookData(currData);
            })
        }
    },[id])


    return (
        <Box>
            <Nav/>
            <Container maxW="100%" p={8}>
                <Flex align={"flex-start"} wrap={"wrap"}>
                    <Image src={book?.coverPath} alt={"cover"} width={300} mb={"2rem"}/>
                    <Stack style={{maxWidth: "900px"}} paddingLeft={8}>
                        <Heading>{book?.title}</Heading>
                        <Text>{book?.author}</Text>
                        {bookData &&
                            <>
                                <Text>{"Gelesen: " + (bookData?.timeRead) + " Sekunden" }</Text>
                                <Text>{"Kapitel: " + bookData?.currChapter}</Text>
                            </>
                        }
                        {id && <Rating rating={book?.rating} id={id}></Rating>}
                        <Text>{book?.description}</Text>
                    </Stack>
                </Flex>
            </Container>

        </Box>
    )
}