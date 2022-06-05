package com.wu.douban.entity;

import lombok.Data;

@Data
public class BookZLM {

    //书籍基本信息
    private String id;
    private String bookName;
    private String bookAuthor;
    private String bookScore;
    private String bookScoreNum;
    private String bookQuote;
    private String bookPictureHash;
    private String bookPicture;

    //书籍详情
    private String bookDId;
    private String bookDName;
    private String bookDFive;
    private String bookDFour;
    private String bookDThree;
    private String bookDTwo;
    private String bookDOne;
    private String bookDInfo;
    private String bookDTags;
    private String bookDAnotherLike;

    //热门
    private String bookHotTags;
    private String bookHotAuthor;

    public void setBookInfo(BookZLM book){
        if(book.getBookName()!=null)
            this.bookName = book.getBookName();
        if(book.getBookAuthor()!=null)
            this.bookAuthor = book.getBookAuthor();
        if(book.getBookScore()!=null)
            this.bookScore = book.getBookScore();
        if(book.getBookDTags()!=null)
            this.bookDTags = book.getBookDTags();
    }
    
    


}
