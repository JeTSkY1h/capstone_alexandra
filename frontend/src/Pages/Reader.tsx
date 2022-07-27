import {useNavigate, useParams} from "react-router-dom";
import { useCallback, useEffect, useLayoutEffect, useState} from "react";
import {getBookData, getChapter, getChapters, postBookData} from "../service/apiService";
import {BsChevronDoubleLeft, BsChevronDoubleRight} from "react-icons/bs";
import { FaChevronLeft} from "react-icons/fa";
import "./Reader.css";
import {ResumeData} from "../service/models";
import {UserButton} from "../components/Nav/UserButton/UserButton";
import {useBooleanToggle, useInterval} from "@mantine/hooks";
import {Burger, Drawer, Paper, Title, Tooltip, UnstyledButton} from "@mantine/core";


export default function Reader(){

    const {id} = useParams()
    const [resumeData, setResumeData] = useState<ResumeData>();
    const [toc, setToc] = useState<Array<string>>(["fetching Chapters"]);
    const [chapterText, setChapterText] = useState("");
    const [drawerState, toggleDrawerState] = useBooleanToggle(false);
    const [currChapter, setCurrChapter] = useState(0);
    const [otherScreen, setOtherScreen] = useState(false);
    const nav = useNavigate();
    const [seconds, setSeconds] = useState(0);
    const interval = useInterval(()=>setSeconds(s=>s+1),1000);

    const delay = (time: number) =>{
        return new Promise(resolve => setTimeout(resolve,time))
    }

useEffect(()=>{
    interval.start()
    return interval.stop();
},[interval]);

    const getNewChapter =  useCallback(()=> {
        if(id) {
            getChapter(id, currChapter).then(data => {
                setChapterText(data)
            }).then(()=>{
                let currData = resumeData;
                 if(currData) {
                     currData.currChapter = currChapter;
                     currData.timeRead = seconds;
                     setResumeData(currData);
                     postBookData(currData).then(data=>console.log(data))
              }
            }).catch(()=>{
                setChapterText("<div style='color: red'>Das Kapitel konnte nicht geladen werden</div>")
            })
        } else {
            setChapterText("<div style='color: red'>Das Kapitel konnte nicht geladen werden</div>")
        }
        //eslint-disable-next-line
    },[id, currChapter])

    useEffect(()=>{
        getNewChapter();
    },[currChapter, getNewChapter])

    useLayoutEffect(()=>{
        const content = document.getElementById("test");
        if(id) {
            getChapters(id).then(data => {
                setToc(data)
            })
            getBookData().then((data: ResumeData[]) => {
                if (!data) {
                    let bookData = {
                        bookId: id,
                        currChapter: currChapter,
                        contentHeight: content!.offsetHeight,
                        contentWidth: content!.offsetWidth,
                        contentScrollTop: content!.scrollTop,
                        timeRead: 0,
                    }
                    postBookData(bookData).then(data=>console.log(data));
                    setResumeData(bookData);
                    setCurrChapter(0)
                }
                let bookData = data.filter((bookdata: ResumeData) => bookdata.bookId === id)[0]

                if (bookData) {
                    setResumeData(bookData)
                    if ( content && (content.offsetWidth !== bookData.contentWidth || content.offsetHeight !== bookData.contentHeight)) {
                            setOtherScreen(true);
                        }

                } else {

                        let bookData = {
                            bookId: id,
                            currChapter: currChapter,
                            contentHeight: content!.offsetHeight,
                            contentWidth: content!.offsetWidth,
                            contentScrollTop: content!.scrollTop,
                            timeRead: 0,
                        }
                        setResumeData(bookData);
                }


            }).catch(e=>{
                console.log(e);
                setResumeData({
                    bookId: id,
                    currChapter: currChapter,
                    contentHeight: content!.offsetHeight,
                    contentWidth: content!.offsetWidth,
                    contentScrollTop: content!.scrollTop,
                    timeRead: 0,
                })
            })
        }
        //eslint-disable-next-line
    },[id])

    const firstRender = useCallback(()=>{
        if(resumeData) {
            setCurrChapter(resumeData.currChapter)
            delay(200).then(()=>{
                scrollBy(resumeData.contentScrollTop)
            })

        }
    },[resumeData])

    useLayoutEffect(()=>{
        firstRender();
    },[resumeData, firstRender])
    
    const scrollBy = (target: number)=> {
        const contentDiv = document.getElementById("test");
        if(contentDiv) {
            contentDiv.scrollTo(0,target)
        } else {
            console.log("NO CONTENT DIV")
        }
    }

    const scrollTop = ()=>{
        const contentDiv = document.getElementById("test");
        if(contentDiv) {
            contentDiv.scrollTo(0,0)
        } else {
            console.log("NO CONTENT DIV")
        }
    }

     const getPreviousChapter = () => {
         setCurrChapter(chapter=>chapter-1);
         scrollTop()
     }

    const getNextChapter = () => {
        setCurrChapter(chapter=>chapter+1);
        scrollTop()
    }

    const handleScroll = () => {
        if( id && resumeData){
            const contentDiv = document.getElementById("test");
            let currData = resumeData;
            currData.contentScrollTop = contentDiv!.scrollTop;
            currData.timeRead = seconds;
            setResumeData(currData);
            postBookData(currData).then(data => console.log(data));
        }
    }

    const overwriteScreenSize = ()=> {
        const contentDiv = document.getElementById("test");
        setOtherScreen(false)
        let currData = resumeData;
        if(currData && contentDiv){
            currData.contentWidth = contentDiv.offsetWidth;
            currData.contentHeight = contentDiv.offsetHeight;
            setResumeData(currData);
            postBookData(currData).then(data=>console.log(data))
        }
    }

    return (
        <>
            <Drawer opened={drawerState} onClose={()=>toggleDrawerState()}>

                <UserButton/>

                {toc.map((chapter, i)=> {
                    return (
                        <div>
                            <UnstyledButton onClick={()=>{
                                setCurrChapter(i)
                                toggleDrawerState();
                            }} className={"chapter-btn"}>
                                <Title order={3} sx={(theme)=>({

                                    '&:hover': {
                                        color: theme.colorScheme === "dark" ? theme.colors.gray[0] : theme.colors.dark[9]
                                    }
                                })}>{chapter}</Title>
                            </UnstyledButton>
                        </div>
                    )
                })}
            </Drawer>

            <nav>
                <button onClick={()=>nav("/main")} className={"btn back"}>
                    <FaChevronLeft fontSize={"1rem"}/>
                </button>
                <Tooltip label={"Open Chapter List"}>
              <Burger opened={drawerState}
                      onClick={()=>toggleDrawerState()}
                      title={drawerState? "close sidebar" : "open sidebar"}
                      style={{float: "right"}}
              />
                </Tooltip>


            </nav>

            {otherScreen &&
                <div className={"modal-bg"}>
                    <div className={"screen-size-alert"}>
                        <p>Deine Bildschrimgröße hat sich seit deinem letzten Besuch hier verändert. evt. hat sich deshalb dein lesezeichen verschoben.</p>
                        <button className={"btn"} onClick={()=> {
                            setOtherScreen(false)
                        }}>Bildschirmgröße beibehalten</button>
                        <button className={"btn"} onClick={overwriteScreenSize}>Bildschirmgröße überschreiben</button>
                    </div>
                </div>
            }

                <div className={otherScreen ? "reader-content blur" : "reader-content"} >

                    <Paper id={"test"} onScroll={handleScroll} style={{
                        position: "relative",
                        margin: "1rem",
                        paddingLeft: "2rem",
                        paddingRight: "2rem",
                        overflowY: "scroll",
                        height: "calc(100vh - 80px - 2rem)",
                    }}>
                        <div dangerouslySetInnerHTML={{__html: chapterText}}/>
                        {currChapter < toc.length && <button onClick={getNextChapter} className={"nextpage"}> <BsChevronDoubleRight/> </button>}
                        {currChapter > 0 && <button onClick={getPreviousChapter} className={"prevpage"}> <BsChevronDoubleLeft/> </button>}
                    </Paper>

                </div>
        </>
    )
}