import {Group, UnstyledButton} from "@mantine/core";
import {rateBook} from "../../service/apiService";
import {AiFillStar, AiOutlineStar} from "react-icons/ai";

interface RatingProps{
    rating: number | undefined
    id: string;
}

export const Rating = ({rating, id}: RatingProps) => {

    let Stars = [];
    for (let i = 1; i < 6; i++) {
        Stars.push(<UnstyledButton onClick={()=>{
            rateBook(id, i).then(data => rating = data)
        }}>{rating && i <= rating ? <AiFillStar/> : <AiOutlineStar/>}</UnstyledButton> )
    }

    return (
        <>
            <Group>
                {Stars}
            </Group>
        </>
    )
}