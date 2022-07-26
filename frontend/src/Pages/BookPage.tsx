import Nav from "../components/Nav/Nav";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getBook, getCover,} from "../service/apiService";
import {Book} from "../service/models";
import "./BookPage.css";
import {Container, Group, Image, Stack, Text, Title} from "@mantine/core";
import {Rating} from "../components/Rating/Rating";

export default function BookPage(){
    const {id} = useParams();
    const [book, setBook] = useState<Book>();
    const [cover, setCover] = useState("");

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
        }
    },[id])


    return (
        <>
        <Nav/>
            <Container fluid p={8}>
                <Group align={"flex-start"}>
                    <Image src={cover} alt={"cover"} width={300}/>
                    <Stack>
                        <Title order={1}>{book?.title}</Title>
                        <Text>{book?.author}</Text>
                        {id && <Rating rating={book?.rating} id={id}></Rating>}
                    </Stack>
                </Group>
            </Container>

        </>
    )
}