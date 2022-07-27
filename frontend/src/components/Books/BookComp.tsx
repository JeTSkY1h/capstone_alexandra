import { Book } from "../../service/models"
import {useEffect, useState} from "react";
import {getCover} from "../../service/apiService";
import {Link} from "react-router-dom";
import {Button, Card, Image, Text} from "@mantine/core";
import {Rating} from "../Rating/Rating";


interface BookProps {
    book: Book;
}

export default function BookComp(props: BookProps){

    const [cover, setCover] = useState("");

    useEffect(()=>{
        getCover(props.book.id).then(res=>{
                let image = URL.createObjectURL(res.data);
                setCover(image);
        });
    },[props])

    return (
            <Card style={{width: 144}} shadow="sm" p={"sm"}>
                <Card.Section component={Link} to={"/book/" + props.book.id} >
                    <Image src={cover} height={220} fit={"cover"}></Image>
                </Card.Section>
                <Text lineClamp={1} weight={600} size={"sm"} style={{marginTop: 8, marginBottom: 8}}>
                    {props.book.title}
                </Text>
                <Rating id={props.book.id} rating={props.book.rating}/>
                <Button component={Link} to={"/reader/" + props.book.id}>
                    Read
                </Button>
            </Card>

    )
}