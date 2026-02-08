package com.damir.library.dto;

import com.damir.library.model.BookType;

public class BookRequest {
    private String name;
    private String author;
    private double price;
    private int categoryId;

    private BookType type;
    private Double fileSizeMb;
    private Integer pages;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public BookType getType() { return type; }
    public void setType(BookType type) { this.type = type; }

    public Double getFileSizeMb() { return fileSizeMb; }
    public void setFileSizeMb(Double fileSizeMb) { this.fileSizeMb = fileSizeMb; }

    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }

}
