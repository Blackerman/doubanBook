package com.wu.douban.service;

import com.wu.douban.entity.BookFocus;
import com.wu.douban.entity.BookReview;
import com.wu.douban.entity.BookScoreCount;
import com.wu.douban.entity.BookZLM;
import com.wu.douban.tools.ZlmSun;

import java.util.List;

public interface ZLMService {


    List<BookFocus> search(BookFocus BookZLM, int page, int limit);

    int searchCount(BookZLM BookZLM);

    boolean resetPwd(int id);

    BookFocus findBookZLMInfo(int id);

    boolean updateBookZLMInfo(BookZLM BookZLM);

    List<BookScoreCount> scoreCount();


    List<BookScoreCount> rankCount();

    BookReview reviewCount();

    List<ZlmSun> sunMain(List<BookScoreCount> list, BookReview review);

    int[] rankMain(List<BookScoreCount> list);

    List<BookScoreCount> hotAuthor();
}
