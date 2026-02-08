package com.damir.library.model;

public class PRINTED extends BookBase {
    private int pages;

    public PRINTED(int id, String name, String author, Category category, double price, int pages) {
        super(id, name, author, category, price);
        setPages(pages);
    }

    @Override
    public String getType() {
        return BookType.PRINTED.name();
    }

    @Override
    public String getDetails() {
        return getName() + " " + pages + " pages";
    }

    public int getPages() { return pages; }

    public void setPages(int pages) {
        if (pages <= 0) throw new IllegalArgumentException("pages must be > 0");
        this.pages = pages;
    }
}
