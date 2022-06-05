package com.wu.douban.service.impl;

import com.wu.douban.entity.BookFocus;
import com.wu.douban.entity.BookReview;
import com.wu.douban.entity.BookScoreCount;
import com.wu.douban.entity.BookZLM;
import com.wu.douban.mapper.BookMapper;
import com.wu.douban.service.ZLMService;
import com.wu.douban.tools.ZlmChild;
import com.wu.douban.tools.ZlmSun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.wu.douban.controller.ZLMBookCountController.getNumberText;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@Service
public class ZLMServiceImpl  implements ZLMService {
    //MyBtis + Spring
    @Autowired
    private BookMapper mapper;


    @Override
    public List<BookFocus> search(BookFocus BookZLM, int page, int limit) {
        if(BookZLM!=null&& !"".equals(BookZLM.getBookName().trim()))
            BookZLM.setBookName("%" + BookZLM.getBookName() + "%");
        if(BookZLM!=null&& !"".equals(BookZLM.getBookAuthor().trim()))
            BookZLM.setBookAuthor("%" + BookZLM.getBookAuthor() + "%");
        if(BookZLM!=null&& !"".equals(BookZLM.getBookDTags().trim()))
            BookZLM.setBookDTags("%" + BookZLM.getBookDTags() + "%");
        if(BookZLM!=null&& !"".equals(BookZLM.getBookHotTags().trim()))
            BookZLM.setBookHotTags("%" + BookZLM.getBookHotTags() + "%");
        if(BookZLM!=null&& !"".equals(BookZLM.getBookHotAuthor().trim()))
            BookZLM.setBookHotAuthor("%" + BookZLM.getBookHotAuthor() + "%");

        List<BookFocus> bookList ;

        if(page>0 &&limit>0)
            bookList =  mapper.selectByWhere(BookZLM,(page-1)*limit, limit);
        else
            bookList = mapper.selectByWhere(BookZLM,null, null);
        for(BookZLM book : bookList){
//            if(book.getBookDTags() == null)
                book.setBookDTags(book.getBookName() + "," + book.getBookAuthor()+",");
        }
        return bookList;
    }

    @Override
    public int searchCount(BookZLM BookZLM) {
        return mapper.countSelectByWhere(BookZLM);
    }

    @Override
    public boolean resetPwd(int id) {
        return false;
    }

    @Override
    public BookFocus findBookZLMInfo(int id) {
        return mapper.selectById(id);
    }

    @Override
    public boolean updateBookZLMInfo(BookZLM book) {
        if(book!=null &&book.getId()!=null){
            BookZLM oldbookInfo = mapper.selectById(Integer.valueOf(book.getId()));
            if(oldbookInfo!=null){
//                oldbookInfo.setBookScore(book.getBookScore());
                oldbookInfo.setBookInfo(book);
                return mapper.update(oldbookInfo) == 1?true:false;
            }
            return false;
        }

        return false;
    }

    @Override
    public List<BookScoreCount> scoreCount() {
        return mapper.countByScore();
    }

    @Override
    public List<BookScoreCount> rankCount() {
        return mapper.coungByRank();
    }

    @Override
    public BookReview reviewCount() {
        BookReview review = new BookReview();
        List<String> list = mapper.coungByReview();
        for(int i = 0; i < list.size(); i++){
            int number = getNumberText(list.get(i));
            if(number<=50){
                review.bad++;
            }else if(number <=200){
                review.normal++;
            }else if(number <1000){
                review.good++;
            }else{
                review.hot++;
            }
        }
        return review;
    }

    @Override
    public List<ZlmSun> sunMain(List<BookScoreCount> list, BookReview review) {
        List<ZlmSun> suns = new ArrayList<>();

        ZlmSun sun1 = new ZlmSun();
        sun1.name = "爬虫信息";
        ZlmChild child = new ZlmChild("表", 35);
        ZlmChild child1 = new ZlmChild("网站", 4);
        ZlmChild child2 = new ZlmChild("数目", 16);
        ZlmChild child_child1 = new ZlmChild("book_info", 14);
        ZlmChild child_child2 = new ZlmChild("book_det_info", 20);
        ZlmChild child1_child1 = new ZlmChild("豆瓣",4);
        ZlmChild child2_child1 = new ZlmChild("图片10075张",4);
        ZlmChild child2_child2 = new ZlmChild("基本页10075",4);
        ZlmChild child2_child3 = new ZlmChild("详情页7453",4);

        ZlmChild child_child1_col1 = new ZlmChild("编号", 2);
        ZlmChild child_child1_col2 = new ZlmChild("书名", 2);
        ZlmChild child_child1_col3 = new ZlmChild("作者", 2);
        ZlmChild child_child1_col4 = new ZlmChild("得分", 2);
        ZlmChild child_child1_col5 = new ZlmChild("引言", 2);
        ZlmChild child_child1_col6 = new ZlmChild("图片哈希值", 2);
        ZlmChild child_child1_col7 = new ZlmChild("图片链接", 2);

        ZlmChild child_child2_col1 = new ZlmChild("编号", 2);
        ZlmChild child_child2_col2 = new ZlmChild("书名", 2);
        ZlmChild child_child2_col3 = new ZlmChild("五星评价", 2);
        ZlmChild child_child2_col4 = new ZlmChild("四星评价", 2);
        ZlmChild child_child2_col5 = new ZlmChild("三星评价", 2);
        ZlmChild child_child2_col6 = new ZlmChild("两星评价", 2);
        ZlmChild child_child2_col7 = new ZlmChild("一星评价", 2);
        ZlmChild child_child2_col8 = new ZlmChild("简介", 2);
        ZlmChild child_child2_col9 = new ZlmChild("标签", 2);
        ZlmChild child_child2_col10 = new ZlmChild("相似书籍", 2);



        child_child1.children.add(child_child1_col1);
        child_child1.children.add(child_child1_col2);
        child_child1.children.add(child_child1_col3);
        child_child1.children.add(child_child1_col4);
        child_child1.children.add(child_child1_col5);
        child_child1.children.add(child_child1_col6);
        child_child1.children.add(child_child1_col7);

        child_child2.children.add(child_child2_col1);
        child_child2.children.add(child_child2_col2);
        child_child2.children.add(child_child2_col3);
        child_child2.children.add(child_child2_col4);
        child_child2.children.add(child_child2_col5);
        child_child2.children.add(child_child2_col6);
        child_child2.children.add(child_child2_col7);
        child_child2.children.add(child_child2_col8);
        child_child2.children.add(child_child2_col9);
        child_child2.children.add(child_child2_col10);

        child.children.add(child_child1);
        child.children.add(child_child2);
        child1.children.add(child1_child1);
        child2.children.add(child2_child1);
        child2.children.add(child2_child2);
        child2.children.add(child2_child3);
        sun1.children.add(child);
        sun1.children.add(child1);
        sun1.children.add(child2);
        suns.add(sun1);

        ZlmSun sun2 = new ZlmSun();
        sun2.name = "图书管理";
        ZlmChild child_= new ZlmChild("评分分析", 15);
        ZlmChild child2_ = new ZlmChild("热门分析", 16);
        ZlmChild child__child1 = new ZlmChild(list.get(0).score + "级图书" + list.get(0).count, 3);
        ZlmChild child__child2 = new ZlmChild(list.get(1).score + "级图书" + list.get(1).count, 3);
        ZlmChild child__child3 = new ZlmChild(list.get(2).score + "级图书" + list.get(2).count, 3);
        ZlmChild child__child4 = new ZlmChild(list.get(3).score + "级图书" + list.get(3).count, 3);
        ZlmChild child__child5 = new ZlmChild(list.get(4).score + "级图书" + list.get(4).count, 3);
        ZlmChild child2__child1 = new ZlmChild(review.hot+ "本热门图书", 4);
        ZlmChild child2__child2 = new ZlmChild(review.good+ "本较好图书", 4);
        ZlmChild child2__child3 = new ZlmChild(review.normal+ "本一般图书", 4);
        ZlmChild child2__child4 = new ZlmChild(review.bad+ "本冷门图书", 4);

        child_.children.add(child__child1);
        child_.children.add(child__child2);
        child_.children.add(child__child3);
        child_.children.add(child__child4);
        child_.children.add(child__child5);
        child2_.children.add(child2__child2);
        child2_.children.add(child2__child3);
        child2_.children.add(child2__child4);
        child2_.children.add(child2__child1);
        sun2.children.add(child_);
        sun2.children.add(child2_);

        suns.add(sun2);
        return suns;
    }

    @Override
    public int[] rankMain(List<BookScoreCount> list) {
        int sum = mapper.sum();
        int[] percent = new int[5];
        for(int i = 0; i < list.size(); i++){
            BookScoreCount item = list.get(i);
            switch (item.score){
                case "A":
                    percent[0] = item.count*100 / sum;
                    break;
                case "B":
                    percent[1] = item.count*100 / sum;
                    break;
                case "C":
                    percent[2] = item.count*100 / sum;
                    break;
                case "D":
                    percent[3] = item.count*100 / sum;
                    break;
                case "E":
                    percent[4] = item.count*100 / sum;
                    break;
            }
        }
        int temp = 0;
        for(int j = 0; j < 4; j++){
            temp += percent[j];
        }
        percent[4] = 100 - temp;
        return percent;
    }

    @Override
    public List<BookScoreCount> hotAuthor() {
        List<BookScoreCount> result = new ArrayList<>();
        String [] authors = {"东野圭吾","霍金","村上春树"};

        for(String author:authors){
            String temp="";
            if(author!=null&& !"".equals(author.trim()))
                temp = "%" + author + "%";
            BookScoreCount one = new BookScoreCount();
            one.hotAuthor = author;
            List<BookScoreCount> list = mapper.hotAuthor(temp);
            int sum = 0;

            for(int i = 0; i < list.size(); i++){
                BookScoreCount item = list.get(i);
                one.five += getNumberText(item.fivestar);
                one.four += getNumberText(item.fourstar);
                one.three += getNumberText(item.threestar);
                one.two += getNumberText(item.twostar);
                one.one += getNumberText(item.onestar);

            }
            one.count = mapper.countAuthor(temp).count;
            if(one.count!=0){
                one.five = one.five/one.count;
                one.four = one.four/one.count;
                one.three = one.three/one.count;
                one.two = one.two/one.count;
                one.one = one.one/one.count;
            }


            result.add(one);
        }

      return result;
    }

    public  int getNumberText(String str){

        str=str.trim();
        String str2="";
        if(str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if(str.charAt(i) == '.')
                    break;


                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }
        }
        if(str2==""){
            return 0;
        }
        int number = Integer.valueOf(str2);
        return number;
    }


}
