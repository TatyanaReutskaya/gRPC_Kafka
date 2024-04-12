package com.example.libraryserviceapp.controller;

import com.example.libraryserviceapp.dto.BookDto;
import com.example.libraryserviceapp.exception.BookException;
import com.example.libraryserviceapp.kafka.KafkaProducer;
import com.example.libraryserviceapp.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final KafkaProducer kafkaProducer;

    public BookController(BookService bookService, KafkaProducer kafkaProducer) {
        this.bookService = bookService;
        this.kafkaProducer = kafkaProducer;
    }
    @GetMapping("/healthCheck")
    @ResponseStatus(HttpStatus.OK)
    public void healthCheck() {
    }
    @PostMapping("/create")
    public void createBook(@RequestBody BookDto bookDto) {
        bookService.save(bookDto);
    }
    @GetMapping
    public List<BookDto> getAllBooks(){
        return bookService.getAllBooks();
    }
    @PostMapping("/assign")
    public void assignBook(@RequestHeader String bookId, @RequestHeader String phone){
        bookService.assign(bookId,phone);
    }
    @PostMapping("/release")
    public void releaseBook(@RequestHeader String bookId){
        bookService.release(bookId);
    }
    @ExceptionHandler(BookException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exception(Exception e){
        return  e.getMessage();
    }
}
