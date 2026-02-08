package com.damir.library.repository;

import com.damir.library.model.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements  RowMapper<BookBase>{

    @Override
    public BookBase mapRow(ResultSet rs, int rowNum) throws SQLException {

        Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));

        String type = rs.getString("book_type");
        int id = rs.getInt("id");

        String name = rs.getString("name");
        String author = rs.getString("author");
        double price = rs.getDouble("price");

        if ("EBOOK".equals(type)) {
            double fileSize = rs.getDouble("file_size_mb");
            return new EBOOK(id, name, author, category, price, fileSize);
        }

        if ("PRINTED".equals(type)) {
            int pages = rs.getInt("pages");
            return new PRINTED(id, name, author, category, price, pages);
        }

        throw new IllegalArgumentException("Unknown book_type: " + type);
    }
}
