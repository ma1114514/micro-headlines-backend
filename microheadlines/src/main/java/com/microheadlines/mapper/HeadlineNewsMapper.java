package com.microheadlines.mapper;

import com.microheadlines.entity.News;
import com.microheadlines.entity.vo.HeadlineQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HeadlineNewsMapper {
    @Select("<script>" +
            "SELECT id, title, content, category, author_id , views, " +
            "       create_time , update_time , is_deleted  " +
            "FROM news " +
            "WHERE is_deleted = 0 " +
            "<if test='type != null'>" +
            "  AND category = #{type} " +
            "</if>" +
            "<if test='keyWord != null and keyWord != \"\"'>" +
            "  AND (title LIKE CONCAT('%', #{keyWord}, '%') OR content LIKE CONCAT('%', #{keyWord}, '%')) " +
            "</if>" +
            "ORDER BY update_time DESC " +
            "LIMIT #{pageSize} OFFSET #{passCount}" +
            //limit A offset B 标识跳过B条展示一页A条
            "</script>")
    List<News> findPageList(@Param("type") String type,@Param("keyWord") String keyWord,@Param("pageSize") int pageSize,@Param("passCount") int passCount);

    // 总数查询
    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM news " +
            "WHERE is_deleted = 0 " +
            "<if test='type != null'>" +
            "  AND category = #{type} " +
            "</if>" +
            "<if test='keyWord != null and keyWord != \"\"'>" +
            "  AND (title LIKE CONCAT('%', #{keyWord}, '%') OR content LIKE CONCAT('%', #{keyWord}, '%')) " +
            "</if>" +
            "</script>")
    Long findCount(HeadlineQueryVo headlineQueryVo);

    @Select("select id,title,content,category,author_id,views,create_time,update_time,is_deleted from news where id=#{id}")
    News findNewsById(int id);
}

