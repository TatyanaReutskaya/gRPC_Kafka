package com.example.libraryserviceapp.service.impl;

import com.example.jrpc.grpc.PhoneRequest;
import com.example.jrpc.grpc.UserInfoResponse;
import com.example.jrpc.grpc.UserStatusServiceGrpc;
import com.example.libraryserviceapp.dto.BookDto;
import com.example.libraryserviceapp.entity.Book;
import com.example.libraryserviceapp.exception.BookException;
import com.example.libraryserviceapp.kafka.KafkaProducer;
import com.example.libraryserviceapp.mapper.BookMapper;
import com.example.libraryserviceapp.repository.BookRepository;
import com.example.libraryserviceapp.service.BookService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final KafkaProducer kafkaProducer;
    private final Logger LOGGER = LoggerFactory.getLogger("gRPC client");
    private final String VALID_USER_STATUS = "ACTIVE";
    private int ASSIGN = 1;
    private int RELEASE = -1;
    @GrpcClient("user-service-grpc")
    UserStatusServiceGrpc.UserStatusServiceBlockingStub stub;

    @Override
    public Book save(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        bookRepository.save(book);
        return book;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::bookToBookDto).toList();
    }

    @Override
    public void assign(String bookId, String phone) {
        UUID bookIdUuid;
        try {
            bookIdUuid = UUID.fromString(bookId);
        }
        catch (IllegalArgumentException e) {
            throw new BookException("Invalid book id");
        }
        Optional<Book> bookOptional = bookRepository.findById(bookIdUuid);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getUserId()!=null) {
                throw new BookException("Book is assign to user "+book.getUserId());
            }
            PhoneRequest phoneRequest = PhoneRequest.newBuilder()
                    .setPhone(phone).build();
            UserInfoResponse userInfoResponse = stub.getUserInfoByPhone(phoneRequest);
            LOGGER.info("request {"+phone+"}, response {"+userInfoResponse.getUserId()+", "+userInfoResponse.getUserStatusGrpc()+"}");
            String userIdString = userInfoResponse.getUserId();
            if (userIdString.isBlank()) {
                throw new BookException("User with phone "+ phone +" doesn't exist");
            }
            UUID userId = UUID.fromString(userInfoResponse.getUserId());
            if (userInfoResponse.getUserStatusGrpc().name().equals(VALID_USER_STATUS)){
                book.setUserId(userId);
                kafkaProducer.reportUserService(userId,ASSIGN);
            }
            else {
                throw new BookException("User with phone "+phone+" is BLOCKED. Too much books.");
            }
        }
        else {
            throw new BookException("Book doesn't exist");
        }
    }

    @Override
    public void release(String bookId) {
        Optional<Book> bookOptional = bookRepository.findById(UUID.fromString(bookId));
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getUserId()==null) {
                throw new BookException("Book is free");
            }
            kafkaProducer.reportUserService(book.getUserId(),RELEASE);
            book.setUserId(null);
        }

    }

}
