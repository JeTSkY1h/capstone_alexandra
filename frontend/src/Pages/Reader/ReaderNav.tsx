import {Link, } from "react-router-dom"
import {Box, Button, Divider, Flex, Heading, Spacer, useColorModeValue} from "@chakra-ui/react";
import React from "react";
import {FaChevronLeft} from "react-icons/fa";




interface ReaderNavProps {
    title: string;
    children: JSX.Element
}


export default function ReaderNav({title, children}: ReaderNavProps){
    const bg = useColorModeValue("dark.100", "dark.700")

    return (
        <Box height={"56px"} background={bg}>
            <Flex alignItems={"center"}>
                <Link to={"/"}>
                    <Button><FaChevronLeft/></Button>
                </Link>
                <Spacer/>
                <Heading noOfLines={1} as={"h1"}>{title}</Heading>
                <Spacer/>
                {children}
            </Flex>
            <Divider/>
        </Box>
    )

}