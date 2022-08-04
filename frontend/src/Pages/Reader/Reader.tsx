import {useParams} from "react-router-dom";
import {useCallback, useEffect, useState} from "react";
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
    DrawerOverlay,  Flex, Spacer, Text,
    useDisclosure
} from "@chakra-ui/react";
import ReaderNav from "./ReaderNav";
import {FaBars, FaChevronLeft, FaChevronRight, FaTimes} from "react-icons/fa";


export default function Reader(){
    const {id} = useParams();
    const [userData, setUserData] = useState<ResumeData>();
    const [chapters, setChapters] = useState<String[]>();
    const { isOpen, onOpen, onClose } = useDisclosure();
    const [err, setErr] = useState("");
    const [book, setBook] = useState<Book>();
    const [chapterText, setChapterText] = useState("");

    const getNewChapter = useCallback((i: number, filteredData?: ResumeData,) => {
        if(!id){
            setErr("Keine Buch Id gefunden")
            return
        }
        let currUserData;
        if (!userData) {
            if(!filteredData) {
                setErr("Es gab ein Problem beim Abrufen der Buchinformationen.")
                return
            }
            currUserData = filteredData;
        } else {
            currUserData = userData;
        }
        if(isOpen) onClose();
        getChapter( id,  i ).then(data=>setChapterText(data))
        currUserData!.currChapter = i;
        setUserData(currUserData)
        postNewData(currUserData)
        //eslint-disable-next-line
    },[id, userData]);

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
                console.log(filteredData);
                getNewChapter(filteredData.currChapter, filteredData);
            }
        ).catch(e=> {
            let filteredData;
            if(e.response.status === 404 || !filteredData) {
                filteredData = {
                    bookId: id,
                    currChapter: 0,
                    contentHeight: 1,
                    contentWidth: 0,
                    timeRead: 0,
                    contentScrollTop: 0
                }
            }
            postNewData(filteredData)
            setUserData(filteredData)
            console.log(e)
            setErr(e.message)
        })

        getChapters(id).then(data=>setChapters(data)).catch(e=>setErr(e))

        //eslint-disable-next-line
    },[id])

    const postNewData = (currUserData: ResumeData) => {
        postBookData(currUserData).then(data => console.log(data)).catch(err => setErr(err))
    }



    const nextChapter = () => {
        if (!userData) return
        let currUserData = userData;
        currUserData.currChapter += 1;
        currUserData.contentScrollTop = 0;
        setUserData(currUserData);
        getNewChapter(currUserData.currChapter);
        postNewData(currUserData)
    }

    const prevChapter = () => {
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

