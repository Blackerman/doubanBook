package com.wu.douban.mapper;

import com.wu.douban.entity.BookFocus;
import com.wu.douban.entity.BookScoreCount;
import com.wu.douban.entity.BookZLM;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper {
    @Select("<script> " +
            "SELECT a.id, book_quote, book_name, book_author, book_score, book_score_num, book_quote, book_d_tags, book_picture " +
            "From book_info  a left join book_det_info b on a.id = b.id   " +
            "<where>  " +
            "   <if test='BookZLM.bookName !=null and BookZLM.bookName.length > 0'> " +
            "       AND book_name like #{BookZLM.bookName} " +
            "   </if> " +
            "   <if test='BookZLM.bookPicture !=null and BookZLM.bookPicture.length > 0'> " +
            "       AND book_picture like #{BookZLM.bookPicture} " +
            "   </if> " +
            "   <if test='BookZLM.bookAuthor !=null and BookZLM.bookAuthor.length > 0'> " +
            "       AND book_author like #{BookZLM.bookAuthor} " +
            "   </if>" +
            "   <if test='BookZLM.bookDTags !=null and BookZLM.bookDTags.length > 0'> " +
            "       AND book_d_tags like #{BookZLM.bookDTags} " +
            "   </if>" +
            "   <if test='BookZLM.bookHotTags !=null and BookZLM.bookHotTags.length > 0'> " +
            "       or book_d_tags like #{BookZLM.bookHotTags} " +
            "   </if>" +
            "   <if test='BookZLM.bookHotAuthor !=null and BookZLM.bookHotAuthor.length > 0'> " +
            "       AND book_author like #{BookZLM.bookHotAuthor} " +
            "   </if>" +
            "</where> " +
            "<if test='start != null and limit!=null'> " +
            "   limit #{start},#{limit} " +
            "</if> " +
            "</script> " +
            "")
    public List<BookFocus> selectByWhere(@Param("BookZLM") BookFocus BookZLM, @Param("start") Integer start, @Param("limit") Integer limit);


    @Select("<script> " +
            "SELECT count(1) " +
            "From book_info  a left join book_det_info b on a.id = b.id   " +
            "<where>  " +
            "   <if test='BookZLM.bookName !=null and BookZLM.bookName.length > 0'> " +
            "       AND book_name like #{BookZLM.bookName} " +
            "   </if> " +
            "   <if test='BookZLM.bookAuthor !=null and BookZLM.bookAuthor.length > 0'> " +
            "       AND book_author like #{BookZLM.bookAuthor} " +
            "   </if>" +
            "   <if test='BookZLM.bookDTags !=null and BookZLM.bookDTags.length > 0'> " +
            "       AND book_d_tags like #{BookZLM.bookDTags} " +
            "   </if>" +
            "   <if test='BookZLM.bookHotTags !=null and BookZLM.bookHotTags.length > 0'> " +
            "       AND book_d_tags like #{BookZLM.bookHotTags} " +
            "   </if>" +
            "   <if test='BookZLM.bookHotAuthor !=null and BookZLM.bookHotAuthor.length > 0'> " +
            "       AND book_author like #{BookZLM.bookHotAuthor} " +
            "   </if>" +
            "</where> " +
            "</script> " +
            "")
    int countSelectByWhere(@Param("BookZLM") BookZLM BookZLM);

    @Select("SELECT book_score as score, COUNT(1) as count FROM book_info " +
            "WHERE book_score <> '' GROUP BY book_score+0 DESC")
    public List<BookScoreCount> countByScore();

    @Select("select * from book_info    where (id+0) = #{book_id}")
    BookFocus selectById(@Param("book_id") int id);

    @Update("Update book_info  set book_score = #{book.bookScore},book_name = #{book.bookName},book_author = #{book.bookAuthor} " +
            "where id = #{book.id}")
    int update(@Param("book") BookZLM book);

    @Select("SELECT " +
            "CASE WHEN book_score+0 <6 THEN 'E' " +
            "WHEN book_score+0 >=6 AND book_score+0 <7 THEN 'D' " +
            "WHEN book_score+0 >=7 AND book_score+0 <8 THEN 'C' " +
            "WHEN book_score+0 >=8 AND book_score+0 <9 THEN 'B' " +
            "ELSE 'A' END AS score, " +
            "COUNT(1) as count " +
            " FROM   book_info   GROUP BY score")
    List<BookScoreCount> coungByRank();

    @Select("select book_score_num from book_info ")
    List<String> coungByReview();

    @Select("select count(1) from book_info  a left join book_det_info b on a.id = b.id")
    int sum();

    @Select("select book_d_five as fivestar, " +
            " book_d_four as fourstar, " +
            " book_d_three as threestar, " +
            " book_d_two as twostar, " +
            "book_d_one as onestar from book_det_info where book_d_author like #{author}")
    List<BookScoreCount> hotAuthor(@Param("author") String author);

    @Select("select count(1) as count from book_det_info" +
            " where book_d_author like #{author}")
    BookScoreCount countAuthor(@Param("author") String author);
}
