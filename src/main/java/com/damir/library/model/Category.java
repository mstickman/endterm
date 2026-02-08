package com.damir.library.model;

public class Category {
    private int id;
    private String name;

    public Category(int id, String name) {
        if (id < 0) throw new IllegalArgumentException("category id must be >= 0");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("category name cannot be empty");
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("category id must be >= 0");
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("category name cannot be empty");
        this.name = name;
    }
}
