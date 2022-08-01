import { Book } from "../../service/models"
import {useEffect, useState} from "react";
import {getCover} from "../../service/apiService";
import {Rating} from "../Rating/Rating";
import {Box, Button, Image, Text, Tooltip, useColorModeValue} from "@chakra-ui/react";
import {Link, useNavigate} from "react-router-dom";


interface BookProps {
    book: Book;
}

export default function BookComp(props: BookProps){

    const [cover, setCover] = useState("");
    const nav = useNavigate();
    const bg = useColorModeValue("white","dark.600" )

    useEffect(()=>{
        getCover(props.book.id).then(res=>{
                let image = URL.createObjectURL(res.data);
                setCover(image);
        });
    },[props])

    return (
            <Box background={bg} width={"144px"} borderRadius={"5px"}  shadow={"lg"} m={"1rem"}>
                <Link to={"/book/" + props.book.id}>
                <Image width={"100%"} src={cover} height={220} fit={"cover"}/>
                </Link>
                <Box p={2}>
                    <Tooltip label={props.book.title}>
                    <Text size={"sm"} noOfLines={1} my={"8px"}>
                        {props.book.title}
                    </Text>
                    </Tooltip>
                    <Rating id={props.book.id} rating={props.book.rating}/>
                    <Button onClick={()=>nav("/reader/" + props.book.id)}>
                        Read
                    </Button>
                </Box>
            </Box>

    )
}