import {useParams} from "react-router-dom";
import { useEffect, useState} from "react";
import {getChapter, getChapters} from "../service/apiService";
import "./Reader.css";

export default function Reader(){

    const {id} = useParams()

    const [toc, setToc] = useState<Array<string>>(["fetching Chapters"]);
    const [chapterText, setChapterText] = useState("");
    const [sidebarState, setSidebarState] = useState(false);
    const [currChapter, setCurrChapter] = useState(0);

    useEffect(()=>{
        if(id){
            getChapters(id).then(data=> {
                console.log(data)
                setToc(data)
            })
            getChapter(id, 0).then(data=>{
                setChapterText(data);
            })
        }
    },[id])

    const getChapterText= (chapterNr: number) => {
        if (id) {
            getChapter(id, chapterNr).then(data=> {
                setChapterText(data)
                setCurrChapter(chapterNr);
                console.log(data)
            })

            return
        }
        setChapterText("There was an Problem fetching this Chapters text.")
    }

    // const getPreviousChapter = () => {
    //     if(id){

    //     }
    // }

    const getNextChapter = () => {
        if(id) {
            getChapter(id, currChapter + 1).then(data => {
                setChapterText(data)
                setCurrChapter((currState => currState + 1))
            }).catch((e)=>{
                setChapterText("<div style='color: red'>Das Kapitel konnte nicht geladen werden</div>")
            })
        } else {
            setChapterText("<div style='color: red'>Das Kapitel konnte nicht geladen werden</div>")
        }
    }

    const handleScroll = (e: any) => {
        console.log(e.target.scrollTop);
    }

    return (
        <>
            <nav>
                <button onClick={()=>{setSidebarState(currState=>!currState)}}>
                    toggleSidebar
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
                    <div onScroll={handleScroll} className={sidebarState ? "content hidden" : "content"}>
                        <div dangerouslySetInnerHTML={{__html: chapterText}}/>
                        <button onClick={getNextChapter} className={"nextpage"}> {">"} </button>
                    </div>
                </div>
        </>
    )
}