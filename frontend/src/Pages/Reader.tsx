import {useNavigate, useParams} from "react-router-dom";
import { useCallback, useEffect, useLayoutEffect, useState} from "react";
import {getBookData, getChapter, getChapters, parseJwt, setBookData} from "../service/apiService";
import {BsChevronDoubleLeft, BsChevronDoubleRight} from "react-icons/bs";
import {FaBars, FaChevronLeft, FaTimes} from "react-icons/fa";
import "./Reader.css";
import {ResumeData} from "../service/models";

export default function Reader(){


    const {id} = useParams()
    const [resumeData, setResumeData] = useState<ResumeData>();
    const [toc, setToc] = useState<Array<string>>(["fetching Chapters"]);
    const [chapterText, setChapterText] = useState("");
    const [sidebarState, setSidebarState] = useState(false);
    const [currChapter, setCurrChapter] = useState(0);
    const [otherScreen, setOtherScreen] = useState(false);
    const nav = useNavigate();

    const delay = (time: number) =>{
        return new Promise(resolve => setTimeout(resolve,time))
    }

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
                const content = document.getElementById("test");
                if (!data) {
                    setResumeData({
                        bookId: id,
                        currChapter: currChapter,
                        contentHeight: content!.offsetHeight,
                        contentWidth: content!.offsetWidth,
                        contentScrollTop: content!.scrollTop
                    })
                    setCurrChapter(0)
                } else {
                    let bookData = data.filter((bookdata: ResumeData) => bookdata.bookId === id)[0]
                    if (bookData) {
                        setResumeData(bookData)
                   if ( content && (content.offsetWidth !== bookData.contentWidth || content.offsetHeight !== bookData.contentHeight)) {
                            setOtherScreen(true);
                        }

                    } else {
                        setResumeData({
                            bookId: id,
                            currChapter: currChapter,
                            contentHeight: content!.offsetHeight,
                            contentWidth: content!.offsetWidth,
                            contentScrollTop: content!.scrollTop
                        })
                    }
                }
            })
        }
        //eslint-disable-next-line
    },[id])

    const resumeDataStuff = useCallback(()=>{
        if(resumeData) {
            setCurrChapter(resumeData.currChapter)
            delay(200).then(()=>{
                scrollBy(resumeData.contentScrollTop)
            })
        }
    },[resumeData])

    useLayoutEffect(()=>{
        resumeDataStuff();
    },[resumeData, resumeDataStuff])


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
            setResumeData(currData);
            setBookData(currData).then(data => console.log(data));
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
            setBookData(currData).then(data=>console.log(data))
        }
    }

    return (
        <>
            <div className={sidebarState ? "sidebar" : "sidebar hidden"}>
                <button>
                    {parseJwt().sub}
                </button>
                <button onClick={()=>nav("/main")}>
                    <FaChevronLeft /> Zurück
                </button>
                <hr/>
                Kapitel
                <hr/>
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

            <nav>
                <button onClick={()=>nav("/main")} className={"btn back"}>
                    <FaChevronLeft fontSize={"1rem"}/>
                </button>
                <button className={"btn sidebar-toggle"} onClick={()=>{setSidebarState(currState=>!currState)}}>
                    {sidebarState ? <FaTimes fontSize={"1rem"}/> : <FaBars fontSize={"1rem"}/>}
                </button>

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

                    <div style={{color: "black"}} id={"test"} onScroll={handleScroll} className={sidebarState ? "content hidden" : "content"}>
                        <div dangerouslySetInnerHTML={{__html: chapterText}}/>
                        {currChapter < toc.length && <button onClick={getNextChapter} className={"nextpage"}> <BsChevronDoubleRight/> </button>}
                        {currChapter > 0 && <button onClick={getPreviousChapter} className={"prevpage"}> <BsChevronDoubleLeft/> </button>}
                    </div>

                </div>
        </>
    )
}