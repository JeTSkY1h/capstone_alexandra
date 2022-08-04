import {Link, } from "react-router-dom"
import {UserButton} from "./UserButton/UserButton"
import Search from "../Input/Search";
import {Box, Divider, Flex, Heading, Spacer, useColorModeValue} from "@chakra-ui/react";




interface NavProps {
    noSearch?: boolean;
}


export default function Nav({noSearch}:NavProps){
const bg = useColorModeValue("dark.100", "dark.700")

return (
    <Box height={"56px"} background={bg}>

        <Flex alignItems={"center"}>
            <Link to={"/"}>
                <Heading as={"h1"}> Alexandra</Heading>
            </Link>
            <Spacer/>
            <Flex gap={"4px"}>
                {!noSearch && <Search/>}
                <UserButton/>
            </Flex>
            {/*<Burger opened={opened} onClick={()=>toggleOpened()} className={classes.burger}/>*/}
        </Flex>
        <Divider/>
     </Box>
 )

}