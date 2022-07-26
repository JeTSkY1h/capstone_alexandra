import {useNavigate} from "react-router-dom";
import {Avatar, Button, Text, Tooltip, UnstyledButton} from "@mantine/core";
import {AiOutlineExperiment} from "react-icons/ai";
import {parseJwt} from "../../../service/apiService";

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
                    <UnstyledButton onClick={() => {
                        localStorage.removeItem("jwt-alexandra");
                        nav("/login");
                    }} sx={(theme) => ({
                        paddingRight:".5rem",
                        height: "100%",
                        borderRadius: "2px",
                        display: "block",
                        color: theme.colorScheme === "dark" ? theme.colors.gray[0] : theme.colors.dark[9],

                        "&:hover": {
                            backgroundColor: theme.colorScheme === "dark" ? theme.colors.dark[8] : theme.colors.gray[1]
                        }
                    })}>
                        <div style={{display: "flex"}}>
                            <Avatar style={{marginRight: 8}}>
                                <AiOutlineExperiment/>
                            </Avatar>
                            <Text style={{lineHeight: "38px", height: "38px"}}>{parseJwt().sub}</Text>
                        </div>
                    </UnstyledButton>
                </Tooltip>
            }
        </>
    )
}