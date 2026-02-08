package com.damir.library.service;

import com.damir.library.dto.BookRequest;
import com.damir.library.dto.BookUpdate;
import com.damir.library.exception.DatabaseOperationException;
import com.damir.library.exception.DuplicateResourceException;
import com.damir.library.exception.InvalidInputException;
import com.damir.library.exception.ResourceNotFoundException;
import com.damir.library.model.BookBase;
import com.damir.library.model.BookType;
import com.damir.library.model.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.damir.library.patterns.BookFactory;
import com.damir.library.repository.BookRepository;
import com.damir.library.repository.CategoryRepository;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepo;
    private final CategoryRepository categoryRepo;

    public BookService(BookRepository bookRepo, CategoryRepository categoryRepo) {
        this.bookRepo = bookRepo;
        this.categoryRepo = categoryRepo;
    }

    @Transactional
    public int addBook(BookRequest req) {
        try {
            validateRequest(req);

            if (!categoryRepo.existsById(req.getCategoryId())) {
                throw new ResourceNotFoundException("Category with id " + req.getCategoryId() + " not found");
            }

            if (bookRepo.existsByNameAndAuthor(req.getName(), req.getAuthor())) {
                throw new DuplicateResourceException("Book already exists (same name and author)");
            }

            String categoryName = categoryRepo.findNameById(req.getCategoryId());

            Category category = new Category(req.getCategoryId(), categoryName);

            BookBase book = BookFactory.create(req, category);

            book.validate();

            return bookRepo.create(book);

        } catch (InvalidInputException | ResourceNotFoundException | DuplicateResourceException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Database error while creating book", e);
        } catch (Exception e) {
            throw new DatabaseOperationException("Unexpected error while creating book", e);
        }
    }

    @Transactional
    public void updateBook(int id, BookUpdate req) {

        if (id <= 0) throw new InvalidInputException("id must be positive");
        if (req == null) throw new InvalidInputException("Request body is required");

        BookBase existing = getBookById(id);

        if (req.getCategoryId() != null) {
            if (req.getCategoryId() <= 0) throw new InvalidInputException("categoryId must be positive");
            if (!categoryRepo.existsById(req.getCategoryId())) {
                throw new ResourceNotFoundException("Category with id " + req.getCategoryId() + " not found");
            }
            String catName = categoryRepo.findNameById(req.getCategoryId());
            existing.setCategory(new com.damir.library.model.Category(req.getCategoryId(), catName));
        }

        if (req.getName() != null) existing.setName(req.getName());
        if (req.getAuthor() != null) existing.setAuthor(req.getAuthor());
        if (req.getPrice() != null) existing.setPrice(req.getPrice());

        if (existing instanceof com.damir.library.model.EBOOK ebook) {
            if (req.getFileSizeMb() != null) ebook.setFileSizeMb(req.getFileSizeMb());
            if (req.getPages() != null) {
                throw new InvalidInputException("Trying to put pages in EBOOK");
            }
        } else if (existing instanceof com.damir.library.model.PRINTED printed) {
            if (req.getPages() != null) printed.setPages(req.getPages());
            if (req.getFileSizeMb() != null) {
                throw new InvalidInputException("Trying to put sizemb in PRINTED");
            }
        }

        existing.validate();

        int affected = bookRepo.update(id, existing);
        if (affected == 0) {
            throw new ResourceNotFoundException("Book not found id: " + id);
        }
    }

    public List<BookBase> getAllBooks() {
        try {
            return bookRepo.getAll();
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Database cant access", e);
        }
    }

    public BookBase getBookById(int id) {
        if (id <= 0) throw new InvalidInputException("id must be positive");

        try {
            return bookRepo.getById(id).orElseThrow(() -> new ResourceNotFoundException("Book " + id + " not found"));
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Database cant access", e);
        }
    }

    @Transactional
    public void removeBook(int id) {
        if (id <= 0) throw new InvalidInputException("id must be positive");

        try {
            int affected = bookRepo.delete(id);
            if (affected == 0) {
                throw new ResourceNotFoundException("Book not found id:" + id);
            }
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Database error while deleting book", e);
        }
    }

    private void validateRequest(BookRequest req) {
        if (req == null) throw new InvalidInputException("Request body is required");

        if (req.getName() == null || req.getName().isBlank())
            throw new InvalidInputException("name is required");

        if (req.getAuthor() == null || req.getAuthor().isBlank())
            throw new InvalidInputException("author is required");

        if (req.getPrice() <= 0)
            throw new InvalidInputException("price must be > 0");

        if (req.getCategoryId() <= 0)
            throw new InvalidInputException("categoryId must be positive");

        if (req.getType() == null)
            throw new InvalidInputException("type is required");

        if (req.getType() == BookType.EBOOK) {
            if (req.getFileSizeMb() == null || req.getFileSizeMb() <= 0) {
                throw new InvalidInputException("[EBOOK] fileSizeMb must be > 0");
            }
        }

        if (req.getType() == BookType.PRINTED) {
            if (req.getPages() == null || req.getPages() <= 0) {
                throw new InvalidInputException("[PRINTED] pages must be > 0");
            }
        }
    }
}
