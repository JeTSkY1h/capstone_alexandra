import {useParams} from "react-router-dom";
import {useEffect, useRef, useState} from "react";
import {getChapter, getChapters} from "../service/apiService";
import {BsChevronDoubleRight} from "react-icons/bs";
import {FaBars, FaTimes} from "react-icons/fa";
import "./Reader.css";

export default function Reader(){

    const {id} = useParams()
    const content = useRef<HTMLDivElement>(null);
    const [toc, setToc] = useState<Array<string>>(["fetching Chapters"]);
    const [chapterText, setChapterText] = useState("");
    const [sidebarState, setSidebarState] = useState(false);
    const [currChapter, setCurrChapter] = useState(0);


    const getNewChapter = (chapterNr: number) => {
        if(id) {
            getChapter(id, chapterNr).then(data => {
                console.log(data)
                let pattern = /<img src=\"(.*\").*\/>/
                let match = pattern.exec(data);
                console.log(match)
                if(match) console.log(match[1])
                setChapterText(data)
                setCurrChapter(chapterNr);
            }).catch(()=>{
                setChapterText("<div style='color: red'>Das Kapitel konnte nicht geladen werden</div>")
            })
        } else {
            setChapterText("<div style='color: red'>Das Kapitel konnte nicht geladen werden</div>")
        }
    }


    useEffect(()=>{
        if(id){
            getChapters(id).then(data=> {
                console.log(data)
                setToc(data)
            })
            getNewChapter(0);
        }
    },[id])

    const getChapterText= (chapterNr: number) => {
        if (id) {
            getNewChapter(chapterNr)
        }
    }

     // const getPreviousChapter = () => {
     //    getNewChapter(currChapter-1);
     // }

    const getNextChapter = () => {
        getNewChapter(currChapter+1)
    }

    const handleScroll = () => {
        if(content.current){
            console.log(content.current.scrollTop);
        }
    }

    return (
        <>
            <nav>
                <button className={"btn sidebar-toggle"} onClick={()=>{setSidebarState(currState=>!currState)}}>
                    {sidebarState ? <FaTimes/> : <FaBars/>}
                </button>
            </nav>
                <div className={"reader-content"}>

                    <div className={sidebarState ? "sidebar" : "sidebar hidden"}>
                        {toc.map((chapter, i)=> {
                            return (
                                    <div>
                                        <button onClick={()=>{getChapterText(i)}}
                                            className={"chapter-btn"}>
                                            {chapter}
                                        </button>
                                    </div>
                            )
                        })}
                    </div>
                    <div  ref={content} onScroll={handleScroll} className={sidebarState ? "content hidden" : "content"}>
                        <div dangerouslySetInnerHTML={{__html: chapterText}}/>
                        <button onClick={getNextChapter} className={"nextpage"}> <BsChevronDoubleRight/> </button>
                    </div>
                </div>
        </>
    )
}