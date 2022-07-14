import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getChapter, getChapters} from "../service/apiService";
import "./Reader.css";

export default function Reader(){

    const {id} = useParams()

    const [toc, setToc] = useState<Array<string>>(["fetching Chapters"]);
    const [chapterText, setChapterText] = useState("");
    const [sidebarState, setSidebarState] = useState(false);

    useEffect(()=>{
        if(id){
            getChapters(id).then(data=> {
                console.log(data)
                setToc(data)
            })
        }
    },[id])

    const handleClick = (chapterNr: number) => {
        if (id) {
            getChapter(id, chapterNr).then(data=>setChapterText(data))
            return
        }
        setChapterText("There was an Problem fetching this Chapters text.")
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
                                        <button onClick={()=>{handleClick(i)}}
                                            className={"chapter-btn"}>
                                            {chapter}
                                        </button>
                                    </div>
                            )
                        })}
                    </div>
                    <div className={"content"}>
                        <div dangerouslySetInnerHTML={{__html: chapterText}}/>
                    </div>
                </div>
        </>
    )
}