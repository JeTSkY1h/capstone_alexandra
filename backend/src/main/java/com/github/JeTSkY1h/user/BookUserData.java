package com.github.JeTSkY1h.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUserData {
    String bookId;
    int contentWidth;
    int contentHeight;
    int contentScrollTop;
    int currChapter;
    int timeRead;
}
