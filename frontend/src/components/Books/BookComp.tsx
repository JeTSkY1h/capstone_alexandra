import { Book } from "../../service/models"
import {Rating} from "../Rating/Rating";
import {Box, Button, Image, Text, Tooltip, useColorModeValue} from "@chakra-ui/react";
import {Link, useNavigate} from "react-router-dom";


interface BookProps {
    book: Book;
}

export default function BookComp(props: BookProps){

    const nav = useNavigate();
    const bg = useColorModeValue("white","dark.600" )


    return (
        <Box pos={"relative"} flexGrow={0} flexShrink={0} background={bg} width={"144px"} borderRadius={"5px"}  shadow={"lg"} m={"1rem"}>
            <Link to={"/book/" + props.book.id}><Image width={"100%"} src={props.book.coverPath} fit={"cover"}/>
            </Link>
            <Box p={2}>
                <Tooltip label={props.book.title}>
                    <Text size={"sm"} noOfLines={1} my={"8px"}>
                        {props.book.title}
                    </Text>
                </Tooltip>
                <Rating id={props.book.id} rating={props.book.rating}/>
                <Button bottom={"1rem"} onClick={()=>nav("/reader/" + props.book.id)}>
                    Read
                </Button>
            </Box>
        </Box>

    )
}