package com.damir.library.controller;

import com.damir.library.dto.BookRequest;
import com.damir.library.dto.BookResponse;
import com.damir.library.dto.BookUpdate;
import com.damir.library.model.BookBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.damir.library.service.BookService;
import com.damir.library.utils.BookMapper;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookResponse> getAll() {
        return service.getAllBooks().stream().map(BookMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable int id) {
        BookBase book = service.getBookById(id);
        return BookMapper.toResponse(book);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookRequest req) {
        int newId = service.addBook(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(newId);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable int id, @RequestBody BookUpdate req) {
        service.updateBook(id, req);
        return BookMapper.toResponse(service.getBookById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        service.removeBook(id);
        return ResponseEntity.noContent().build();
    }

}
