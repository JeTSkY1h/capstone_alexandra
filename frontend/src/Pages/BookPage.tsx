import Nav from "../components/Nav/Nav";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getBook, getBookData, getCover} from "../service/apiService";
import {Book, ResumeData} from "../service/models";
import { Container, Group, Image, Stack, Text, Title} from "@mantine/core";
import {Rating} from "../components/Rating/Rating";

export default function BookPage(){
    const {id} = useParams();
    const [book, setBook] = useState<Book>();
    const [cover, setCover] = useState("");
    const [bookData, setBookData] = useState<ResumeData>();

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

            getBookData().then(data=>{
                console.log(data);
                let currData = data.filter((bookData: ResumeData) => bookData.bookId === id)[0]
                console.log(currData)
                setBookData(currData);
            })
        }
    },[id])


    return (
        <>
        <Nav/>
            <Container fluid p={8}>
                <Group align={"flex-start"} noWrap>
                    <Image src={cover} alt={"cover"} width={300}/>
                    <Stack style={{maxWidth: "900px"}}>
                        <Title order={1}>{book?.title}</Title>
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
                </Group>
            </Container>

        </>
    )
}