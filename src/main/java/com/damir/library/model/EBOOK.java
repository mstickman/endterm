package com.damir.library.model;

public class EBOOK extends BookBase {

    private double fileSizeMb;

    public EBOOK(int id, String name, String author, Category category, double price, double fileSizeMb) {
        super(id, name, author, category, price);
        setFileSizeMb(fileSizeMb);
    }

    @Override
    public String getType() {
        return BookType.EBOOK.name();
    }

    @Override
    public String getDetails() {
        return getName() + " " + fileSizeMb + " MB";
    }

    public double getFileSizeMb() { return fileSizeMb; }

    public void setFileSizeMb(double fileSizeMb) {
        if (fileSizeMb <= 0) throw new IllegalArgumentException("fileSizeMb must be > 0");
        this.fileSizeMb = fileSizeMb;
    }
}
