import {rateBook} from "../../service/apiService";
import {AiFillStar, AiOutlineStar} from "react-icons/ai";
import {Box, Flex} from "@chakra-ui/react";

interface RatingProps{
    rating: number | undefined
    id: string;
}

export const Rating = ({rating, id}: RatingProps) => {

    let Stars = [];
    for (let i = 1; i < 6; i++) {
        Stars.push(<Box key={"rating" + i + id} as={"button"}
                style={{color: "yellow"}}
            onClick={()=>{
            rateBook(id, i).then(data => data)
        }}>{rating && i <= rating ? <AiFillStar/> : <AiOutlineStar/>}</Box> )
    }

    return (
        <>
            <Flex gap={0} my={4}>
                {Stars}
            </Flex>
        </>
    )
}