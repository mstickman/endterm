package com.damir.library.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository {
    private final JdbcTemplate jdbc;

    public CategoryRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean existsById(int id) {
        Integer v = jdbc.queryForObject("SELECT 1 FROM category WHERE id = ?", Integer.class, id);
        return v != null;
    }

    public String findNameById(int id) {
        return jdbc.queryForObject("SELECT name FROM category WHERE id = ?", String.class, id);
    }
}
