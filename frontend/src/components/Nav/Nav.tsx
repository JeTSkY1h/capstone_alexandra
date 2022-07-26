
import {Link, } from "react-router-dom"
import {
    Container,
    createStyles,
    Header,
    Title,
} from "@mantine/core";
import {UserButton} from "./UserButton/UserButton"



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