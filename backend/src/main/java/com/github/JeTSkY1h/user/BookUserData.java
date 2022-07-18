package com.github.JeTSkY1h.user;


import lombok.Data;

@Data
public class BookUserData {
    String bookId;
    int timeRead;
    int contentSize;
    int currChapter;
    int currScroll;
}
