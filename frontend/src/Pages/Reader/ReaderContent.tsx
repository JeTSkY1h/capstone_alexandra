import {ResumeData} from "../../service/models";
import React, { useEffect,  useState} from "react";
import {  postBookData} from "../../service/apiService";
import {
    Box,
    Button,
    Modal, ModalBody,
    ModalCloseButton,
    ModalContent, ModalFooter,
    ModalHeader,
    ModalOverlay,
    Spacer, Text
} from "@chakra-ui/react";

interface ReaderContentProps{
    userData: ResumeData
    setUserData: Function
    chapterText: string
    children: JSX.Element;
}

export default function ReaderContent({userData, setUserData, chapterText, children}:ReaderContentProps) {
    const [newScreen, setNewScreen] = useState(false);


    useEffect(()=>{
        let contentDiv = document.getElementById("content");
        if(contentDiv && (contentDiv.offsetWidth !== userData.contentWidth || contentDiv!.offsetHeight !== userData.contentHeight)) setNewScreen(true);
        if(!contentDiv) {
            console.log("there was an error Loading The Book content.")
            return
        }
        contentDiv.scrollTo(0, userData.contentScrollTop);
        
    },[userData, chapterText])

    const postNewData = (currData: ResumeData)=>{
        postBookData(currData).then(data=>console.log(data))
    }

    const handleScroll = () => {
        let contentDiv = document.getElementById("content")
        let currUserData = userData;
        if (contentDiv) currUserData.contentScrollTop = contentDiv.scrollTop;
        setUserData(currUserData);
        postNewData(currUserData)
    }

    const overwriteScreenSize = ()=> {
        let contentDiv = document.getElementById("content")
        let currUserData = userData;
        setNewScreen(false)
        if(contentDiv) {
            currUserData.contentWidth = contentDiv.offsetWidth
            currUserData.contentHeight = contentDiv.offsetHeight
            setUserData(currUserData)
            postNewData(currUserData);
        }
    }


    return (
        <>

            <Modal isOpen={newScreen} onClose={()=>setNewScreen(false)} >
                <ModalOverlay/>
                <ModalContent>
                    <ModalHeader>Neue Bildschirmgröße</ModalHeader>
                    <ModalCloseButton/>
                    <ModalBody>
                        <Text>Seit deinem Letzten Besuch hier, hat sich deine Bildschirmgröße verändert. Wie möchtest du vorgehen?</Text>
                    </ModalBody>
                    <ModalFooter>
                        <Button onClick={()=>setNewScreen(false)}>Beibehalten</Button>
                        <Spacer/>
                        <Button onClick={()=>overwriteScreenSize()}>Überschreiben</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

                <Box id={"content"} overflowY={"scroll"} overflowX={"hidden"} onScroll={handleScroll} mx={8} height={window.innerHeight - 57 + "px"} >
                    <div dangerouslySetInnerHTML={{__html: chapterText}} />
                    {children}
                </Box>

        </>
    )
}