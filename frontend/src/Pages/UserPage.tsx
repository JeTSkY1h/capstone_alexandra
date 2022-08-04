import Nav from "../components/Nav/Nav";
import {Container, Heading} from "@chakra-ui/react";
import {parseJwt} from "../service/apiService";

export default function UserPage() {
    return (
        <>
            <Nav noSearch={true}/>
            <Container width={"100%"}>
                <Heading as={"h1"}>{parseJwt().sub}</Heading>
            </Container>
        </>
    )

}