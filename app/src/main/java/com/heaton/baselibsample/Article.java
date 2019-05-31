package com.heaton.baselibsample;

import java.io.Serializable;

/**
 * description $desc$
 * created by jerry on 2019/5/28.
 */
public class Article implements Serializable {

    public String author;
    public String title;
    public String createDate;
    public String content;

    @Override
    public String toString() {
        return "Article{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", createDate='" + createDate + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
