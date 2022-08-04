import {useNavigate} from "react-router-dom";
import {AiOutlineExperiment} from "react-icons/ai";
import {parseJwt} from "../../../service/apiService";
import {
    Box,
    Button,
    Tooltip,
    Text,
    IconButton,
    Flex,
    forwardRef,
    BoxProps,
    MenuButton,
    MenuList, MenuItem, Menu
} from "@chakra-ui/react";

const ToggleButton = forwardRef<BoxProps, 'div'>((props, ref) => {
    return (
        <Box ref={ref} {...props} height={"65px"} padding={"1rem"}>
            <Tooltip label={"Logout"}>
                <Box
                    as={"button"}>
                    <Flex mr={"8px"}>
                        <IconButton aria-label={""} mr="8px">
                            <AiOutlineExperiment/>
                        </IconButton>
                        <Text style={{lineHeight: "40px", height: "40px"}}>{parseJwt().sub}</Text>
                    </Flex>
                </Box>
            </Tooltip>
        </Box>
    )
})

export const UserButton =() => {
    const nav = useNavigate();


    return (
       <>
           {!localStorage.getItem("jwt-alexandra") ?
               <Button onClick={() => nav("/login")}>Login</Button>
               :
               <Menu>
                   <MenuButton as={ToggleButton}/>
                   <MenuList>
                       <MenuItem onClick={()=>nav("/user")}>Profile</MenuItem>
                       <MenuItem onClick={()=>{
                           localStorage.removeItem("jwt-alexandra")
                           nav("/login")
                       }
                       }>Logout</MenuItem>
                   </MenuList>
               </Menu>
           }
       </>
    )
}