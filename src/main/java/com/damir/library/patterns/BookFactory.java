package com.damir.library.patterns;

import com.damir.library.dto.BookRequest;
import com.damir.library.model.*;

public class BookFactory {

    public static BookBase create(BookRequest req, Category category) {

        if (req.getType() == null) {
            throw new IllegalArgumentException("type is required");
        }

        return switch (req.getType()) {
            case EBOOK -> new EBOOK(
                    0,
                    req.getName(),
                    req.getAuthor(),
                    category,
                    req.getPrice(),
                    req.getFileSizeMb()
            );
            case PRINTED -> new PRINTED(
                    0,
                    req.getName(),
                    req.getAuthor(),
                    category,
                    req.getPrice(),
                    req.getPages()
            );
        };
    }
}
