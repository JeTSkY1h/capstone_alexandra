import { Book } from "../../service/models"

interface BookProps {
    book: Book;
}

export default function BookComp(props: BookProps){
    return (
        <div className="book card">
            <div className="cover-img-wrapper">
                <img src="HarryPotter-Gesamtausgabe.png" alt="book Cover"/>
                
            </div>
        </div>
    )
}