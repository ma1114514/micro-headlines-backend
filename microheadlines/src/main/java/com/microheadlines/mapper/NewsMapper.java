package com.microheadlines.mapper;

import com.microheadlines.entity.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface NewsMapper {
    @Insert("insert into news ( title, content, category, author_id) " +
            " values (#{title},#{content},#{category},#{authorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addNews(News news);
}
