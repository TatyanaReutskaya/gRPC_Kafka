package com.example.libraryserviceapp.service;

import com.example.jrpc.grpc.UserInfoResponse;
import com.example.libraryserviceapp.dto.BookDto;
import com.example.libraryserviceapp.entity.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    Book save(BookDto bookDto);
    List<BookDto> getAllBooks();
    void assign(String bookId,String phone);
    void release(String bookId);
}
