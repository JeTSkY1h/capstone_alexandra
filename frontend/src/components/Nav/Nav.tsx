
import {Link, useNavigate} from "react-router-dom"
import {
    Avatar,
    Container,
    createStyles,
    Header,
    Title,
    Text,
    UnstyledButton,
    Tooltip, Button
} from "@mantine/core";

import {AiOutlineExperiment} from "react-icons/ai";
import {parseJwt} from "../../service/apiService";

const useStyles = createStyles((theme)=>({
    header: {
        paddingLeft: theme.spacing.md,
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        height: "100%",
    },
    burger: {
        [theme.fn.largerThan('xs')]: {
            display: 'none',
        },
    },
    title: {
        color: theme.colorScheme === "dark" ? theme.colors.gray[0] : theme.colors.dark[9],
        textDecoration: "none",
    }
}))



const UserButton = () => {
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
                        padding: theme.spacing.xs,
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


export default function Nav(){

    const {classes} = useStyles();


return (
    <Header height={56} >
        <Container className={classes.header} fluid>
            <Link to={"/"} className={classes.title}>
                <Title order={1}>Alexandra</Title>
            </Link>
            <UserButton/>
            {/*<Burger opened={opened} onClick={()=>toggleOpened()} className={classes.burger}/>*/}
        </Container>
     </Header>
 )

}