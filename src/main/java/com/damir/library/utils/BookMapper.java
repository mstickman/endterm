package com.damir.library.utils;

import com.damir.library.dto.BookResponse;
import com.damir.library.model.BookBase;
import com.damir.library.model.EBOOK;
import com.damir.library.model.PRINTED;
import com.damir.library.model.BookType;

public class BookMapper {

    public static BookResponse toResponse(BookBase book) {
        BookResponse res = new BookResponse();

        res.setId(book.getId());
        res.setName(book.getName());
        res.setAuthor(book.getAuthor());
        res.setPrice(book.getPrice());

        res.setCategoryId(book.getCategory().getId());
        res.setCategoryName(book.getCategory().getName());

        if (book instanceof EBOOK ebook) {
            res.setType(BookType.EBOOK);
            res.setFileSizeMb(ebook.getFileSizeMb());
            res.setPages(null);

        } else if (book instanceof PRINTED printed) {
            res.setType(BookType.PRINTED);
            res.setPages(printed.getPages());
            res.setFileSizeMb(null);
        }

        return res;
    }
}
