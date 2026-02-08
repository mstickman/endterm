package com.damir.library.model;

public abstract class BookBase extends BaseEntity {

    private String author;
    private Category category;
    private double price;

    protected BookBase(int id, String name, String author, Category category, double price) {
        super(id, name);
        setAuthor(author);
        setCategory(category);
        setPrice(price);
    }

    public void validate() {
        if (getName() == null || getName().isBlank()) throw new IllegalArgumentException("name cannot be empty");
        if (author == null || author.isBlank()) throw new IllegalArgumentException("author cannot be empty");
        if (category == null) throw new IllegalArgumentException("category is required");
        if (price <= 0) throw new IllegalArgumentException("price must be > 0");
    }

    public String getAuthor() { return author; }

    public void setAuthor(String author) {
        if (author == null || author.isBlank()) throw new IllegalArgumentException("author cannot be empty");
        this.author = author;
    }

    public Category getCategory() { return category; }

    public void setCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("category is required");
        this.category = category;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("price must be > 0");
        this.price = price;
    }
}
