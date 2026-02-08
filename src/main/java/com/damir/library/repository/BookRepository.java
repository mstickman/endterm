package com.damir.library.repository;

import com.damir.library.model.BookBase;
import com.damir.library.model.EBOOK;
import com.damir.library.model.PRINTED;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookRepository {
    private final JdbcTemplate jdbc;
    private final BookRowMapper mapper = new BookRowMapper();

    public BookRepository(JdbcTemplate jdbc) {this.jdbc = jdbc;}

    public int create(BookBase book) {

        String insertBookSql = """
            INSERT INTO books (name, author, price, category_id, book_type)
            VALUES (?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertBookSql, new String[]{"id"});
            ps.setString(1, book.getName());
            ps.setString(2, book.getAuthor());
            ps.setDouble(3, book.getPrice());
            ps.setInt(4, book.getCategory().getId());
            ps.setString(5, book.getType());
            return ps;
        }, keyHolder);

        Integer newId = null;

        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.get("id") != null) {
            newId = ((Number) keys.get("id")).intValue();
        } else if (keyHolder.getKey() != null) {
            newId = keyHolder.getKey().intValue();
        }

        if (newId == null) throw new IllegalStateException("No generated id returned");

        if (book instanceof EBOOK ebook) {
            jdbc.update("INSERT INTO ebooks (book_id, file_size_mb) VALUES (?, ?)", newId, ebook.getFileSizeMb());
        } else if (book instanceof PRINTED printed) {
            jdbc.update("INSERT INTO printed_books (book_id, pages) VALUES (?, ?)", newId, printed.getPages());
        } else {
            throw new IllegalArgumentException("Unknown subclass of BookBase");
        }

        return newId;
    }

    public List<BookBase> getAll() {
        String sql = """
            SELECT b.id, b.name, b.author, b.price, b.book_type,
                   c.id AS category_id, c.name AS category_name,
                   e.file_size_mb, p.pages
            FROM books b
            JOIN category c ON c.id = b.category_id
            LEFT JOIN ebooks e ON e.book_id = b.id
            LEFT JOIN printed_books p ON p.book_id = b.id
            ORDER BY b.id
        """;
        return jdbc.query(sql, mapper);
    }

    public Optional<BookBase> getById(int id) {
        String sql = """
            SELECT b.id, b.name, b.author, b.price, b.book_type,
                   c.id AS category_id, c.name AS category_name,
                   e.file_size_mb, p.pages
            FROM books b
            JOIN category c ON c.id = b.category_id
            LEFT JOIN ebooks e ON e.book_id = b.id
            LEFT JOIN printed_books p ON p.book_id = b.id
            WHERE b.id = ?
        """;

        List<BookBase> list = jdbc.query(sql, mapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public int update(int id, BookBase book) {

        String updateBookSql = """
            UPDATE books
            SET name = ?, author = ?, price = ?, category_id = ?
            WHERE id = ?
        """;

        int affected = jdbc.update(
                updateBookSql,
                book.getName(),
                book.getAuthor(),
                book.getPrice(),
                book.getCategory().getId(),
                id
        );

        if (affected == 0) return 0;

        if (book instanceof EBOOK ebook) {

            jdbc.update("DELETE FROM printed_books WHERE book_id = ?", id);
            jdbc.update("INSERT INTO ebooks (book_id, file_size_mb) VALUES (?, ?) " + "ON CONFLICT (book_id) DO UPDATE SET file_size_mb = EXCLUDED.file_size_mb", id, ebook.getFileSizeMb());

        } else if (book instanceof PRINTED printed) {

            jdbc.update("DELETE FROM ebooks WHERE book_id = ?", id);
            jdbc.update("INSERT INTO printed_books (book_id, pages) VALUES (?, ?) " + "ON CONFLICT (book_id) DO UPDATE SET pages = EXCLUDED.pages", id, printed.getPages());
        }

        return affected;
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM books WHERE id = ?", id);
    }

    public boolean existsByNameAndAuthor(String name, String author) {
        Integer total = jdbc.queryForObject("SELECT COUNT(*) FROM books", Integer.class);

        Integer filtered = jdbc.queryForObject(
                "SELECT COUNT(*) FROM books WHERE name = ? AND author = ?",
                Integer.class,
                name, author
        );

        System.out.println("DEBUG totalBooks=" + total +
                " filtered=" + filtered +
                " name=[" + name + "] author=[" + author + "]");

        return filtered != null && filtered > 0;
    }
}
