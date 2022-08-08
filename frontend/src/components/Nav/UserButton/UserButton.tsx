import {useNavigate} from "react-router-dom";
import {AiOutlineExperiment} from "react-icons/ai";
import {parseJwt} from "../../../service/apiService";
import {Box, Button, Tooltip, Text, IconButton, Flex} from "@chakra-ui/react";

export const UserButton = () => {
    const nav = useNavigate();

    return (
        <>
            {!localStorage.getItem("jwt-alexandra") ?
                <Tooltip label={"Login"}>
                    <Button onClick={() => nav("/login")}>Login</Button>
                </Tooltip>
                :
                <Tooltip label={"Logout"}>
                    <Box

                        as={"button"}
                        onClick={() => {
                        localStorage.removeItem("jwt-alexandra");
                        nav("/login");
                    }}>
                        <Flex mr={"8px"}>
                            <IconButton aria-label={""} mr="8px">
                                <AiOutlineExperiment/>
                            </IconButton>
                            <Text style={{lineHeight: "38px", height: "38px"}}>{parseJwt().sub}</Text>
                        </Flex>
                    </Box>
                </Tooltip>
            }
        </>
    )
}