import {useParams} from "react-router-dom";
import { useCallback, useEffect, useLayoutEffect, useRef, useState} from "react";
import {getBookData, getChapter, getChapters, setBookData} from "../service/apiService";
import {BsChevronDoubleLeft, BsChevronDoubleRight} from "react-icons/bs";
import {FaBars, FaTimes} from "react-icons/fa";
import "./Reader.css";
import {ResumeData} from "../service/models";

export default function Reader(){


    const {id} = useParams()
    const content = useRef<HTMLDivElement>(null);
    const [resumeData, setResumeData] = useState<ResumeData>();
    const [toc, setToc] = useState<Array<string>>(["fetching Chapters"]);
    const [chapterText, setChapterText] = useState("");
    const [sidebarState, setSidebarState] = useState(false);
    const [currChapter, setCurrChapter] = useState(0);
    const [otherScreen, setOtherScreen] = useState(false);

    const getNewChapter =  useCallback(()=> {
        if(id) {
            getChapter(id, currChapter).then(data => {
                setChapterText(data)
            }).then(()=>{
                let currData = resumeData;
                 if(currData) {
                     currData.currChapter = currChapter;
                     setResumeData(currData);
                     setBookData(currData).then(data=>console.log(data))
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
        if(id) {
            getChapters(id).then(data => {
                setToc(data)
            })
            getBookData().then((data: ResumeData[]) => {
                if (!data) {
                    setResumeData({
                        bookId: id,
                        currChapter: currChapter,
                        contentHeight: content.current!.offsetHeight,
                        contentWidth: content.current!.offsetWidth,
                        contentScrollTop: content.current!.scrollTop
                    })
                    setCurrChapter(0)
                } else {
                    let bookData = data.filter((bookdata: ResumeData) => bookdata.bookId === id)[0]
                    if (bookData) {
                        setResumeData(bookData)
                        //setCurrChapter(bookData.currChapter) Maybe do this in a useEffect
                        if (content.current?.offsetWidth !== bookData.contentWidth || content.current?.offsetHeight !== bookData.contentHeight) {
                            setOtherScreen(true);
                        }
                    } else {
                        setResumeData({
                            bookId: id,
                            currChapter: currChapter,
                            contentHeight: content.current!.offsetHeight,
                            contentWidth: content.current!.offsetWidth,
                            contentScrollTop: content.current!.scrollTop
                        })
                    }
                }
            })
        }
        //eslint-disable-next-line
    },[id])

    const resumeDataStuff = useCallback(()=>{
        console.log("triggered callback")
        if(resumeData) {
            setCurrChapter(resumeData.currChapter)
            const contentDiv = document.getElementById("test");
            if (contentDiv) {
                contentDiv.scrollBy({top: resumeData.contentScrollTop});
            } else {
                console.log("no DIV! ")
            }
        }
    },[resumeData, ])

    useEffect(()=>{
        resumeDataStuff();
    },[resumeData, resumeDataStuff])

     const getPreviousChapter = () => {
         setCurrChapter(chapter=>chapter-1);
     }

    const getNextChapter = () => {
        setCurrChapter(chapter=>chapter+1);
    }

    const handleScroll = () => {
        if( id && content.current && resumeData){
            let currData = resumeData;
            currData.contentScrollTop = content.current.scrollTop;
            setResumeData(currData);
            setBookData(currData).then();
        }
    }

    const overwriteScreenSize = ()=> {
        setOtherScreen(false)
        let currData = resumeData;
        if(currData && content.current){
            currData.contentWidth = content.current.offsetWidth;
            currData.contentHeight = content.current.offsetHeight;
            setResumeData(currData);
            setBookData(currData).then(data=>console.log(data))
        }
    }

    return (
        <>
            <nav>
                <button className={"btn sidebar-toggle"} onClick={()=>{setSidebarState(currState=>!currState)}}>
                    {sidebarState ? <FaTimes/> : <FaBars/>}
                </button>
            </nav>
            {otherScreen &&
                <div className={"modal-bg"}>
                    <div className={"screen-size-alert"}>
                        <p>Deine Bildschrimgröße hat sich seit deinem letzten Besuch hier verändert. evt. hat sich deshalb dein lesezeichen verschoben.</p>
                        <button onClick={()=> {
                            setOtherScreen(false)
                        }}>Bildschirmgröße beibehalten</button>
                        <button onClick={overwriteScreenSize}>Bildschirmgröße überschreiben</button>
                    </div>
                </div>
            }

                <div className={otherScreen ? "reader-content blur" : "reader-content"} >

                    <div className={sidebarState ? "sidebar" : "sidebar hidden"}>
                        {toc.map((chapter, i)=> {
                            return (
                                <div>
                                    <button onClick={()=>{setCurrChapter(i)}} className={"chapter-btn"}>
                                        {chapter}
                                    </button>
                                </div>
                            )
                        })}
                    </div>

                    <div  ref={content} id={"test"} onScroll={handleScroll} className={sidebarState ? "content hidden" : "content"}>
                        <div dangerouslySetInnerHTML={{__html: chapterText}}/>
                        {currChapter < toc.length && <button onClick={getNextChapter} className={"nextpage"}> <BsChevronDoubleRight/> </button>}
                        {currChapter > 0 && <button onClick={getPreviousChapter} className={"prevpage"}> <BsChevronDoubleLeft/> </button>}
                    </div>
                </div>
        </>
    )
}