export interface LoginData {
    username: string;
    password: string;
}

export interface Book {
    id: string,
    title: string,
    author: string,
    filePath: string,
    genre: string[],
    coverPath: string,
    description: string,
    rating: number
}

export interface ResumeData {
    bookId: string;
    contentWidth: number;
    contentHeight: number;
    contentScrollTop: number;
    currChapter: number;
    timeRead: number;
}