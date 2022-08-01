import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Book, ResumeData} from "../../service/models";
import {getBook, getBookUserData, getChapter, getChapters, postBookData,} from "../../service/apiService";
import ReaderContent from "./ReaderContent";
import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerHeader,
    DrawerOverlay, Flex, Spacer, Text,
    useDisclosure
} from "@chakra-ui/react";
import ReaderNav from "./ReaderNav";
import {FaBars, FaChevronLeft, FaChevronRight, FaTimes} from "react-icons/fa";


export default function Reader(){
    const {id} = useParams();
    const [userData, setUserData] = useState<ResumeData>();
    const [chapters, setChapters] = useState<String[]>()
    const { isOpen, onOpen, onClose } = useDisclosure()
    const [err, setErr] = useState("");
    const [book, setBook] = useState<Book>();
    const [chapterText, setChapterText] = useState("");

    useEffect(()=>{
        if(!id) {
            setErr("es konnte keine Buch ID gefunden werden.")
            return
        }
        getBook(id).then(data=>setBook(data))

        getBookUserData().then((data:ResumeData[])=> {
                let filteredData = data.find(data => data.bookId === id)
                if (!filteredData) filteredData = {bookId: id, currChapter: 0, contentHeight: 1, contentWidth: 0, timeRead: 0, contentScrollTop: 0}
                setUserData(filteredData);
            }
        ).catch(e=>setErr(e))

        getChapters(id).then(data=>setChapters(data)).catch(e=>setErr(e))
    },[id])

    const postNewData = (currUserData: ResumeData) => {
        postBookData(currUserData).then(data => console.log(data)).catch(err => setErr(err))
    }

    const getNewChapter = (i: number) => {
        if(isOpen) onClose();
        if (!userData) {
            setErr("Es gab eim Problem beim Abrufen der Buchinformationen.")
            return;
        }
        getChapter( id!,  i ).then(data=>setChapterText(data))
        let currUserData = userData;
        currUserData.currChapter = i;
        setUserData(currUserData)
        postNewData(currUserData)
    }

    const scrollTop = () => {
        let contentDiv = document.getElementById("content")
        if (contentDiv) contentDiv.scrollTo(0, 0);
    }

    const nextChapter = () => {
        scrollTop()
        if (!userData) return
        let currUserData = userData;
        currUserData.currChapter += 1;
        currUserData.contentScrollTop = 0;
        setUserData(currUserData);
        getNewChapter(currUserData.currChapter);
        postNewData(currUserData)
    }

    const prevChapter = () => {
        scrollTop()
        if (!userData) return
        let currUserData = userData;
        currUserData.currChapter -= 1;
        currUserData.contentScrollTop = 0;
        setUserData(currUserData);
        getNewChapter(currUserData.currChapter);
        postNewData(currUserData)
    }


    return (
        <>
            {err && <div>{err}</div>}

            {chapters &&
                <Drawer isOpen={isOpen} onClose={onClose}>
                    <DrawerOverlay/>
                    <DrawerContent>
                        <DrawerCloseButton/>
                        <DrawerHeader>Chapters</DrawerHeader>
                        <DrawerBody>
                            <>
                                {chapters.map((c, i) => <Text onClick={() => getNewChapter(i)}>{c}</Text>)}
                            </>
                        </DrawerBody>
                    </DrawerContent>
                </Drawer>
            }


            {book && <ReaderNav title={book?.title}>
                <Button onClick={onOpen}>{isOpen ? <FaTimes/> : <FaBars/>}</Button>
            </ReaderNav>}


            {userData && chapters && chapterText &&
                <ReaderContent chapterText={chapterText} setUserData={setUserData} userData={userData}>
                    <Flex>
                        {userData.currChapter > 0 && <Button onClick={prevChapter}><FaChevronLeft/></Button>}
                        <Spacer/>
                        {userData.currChapter < chapters?.length && <Button onClick={nextChapter}><FaChevronRight/></Button>}
                    </Flex>
                </ReaderContent>
            }
        </>
    )
}

