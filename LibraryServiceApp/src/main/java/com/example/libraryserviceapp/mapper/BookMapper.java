package com.example.libraryserviceapp.mapper;

import com.example.libraryserviceapp.dto.BookDto;
import com.example.libraryserviceapp.entity.Book;
import com.example.libraryserviceapp.entity.enums.BookStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "bookStatus", ignore = true)
    BookDto bookToBookDto(Book book);

    Book bookDtoToBook(BookDto bookDto);

    @AfterMapping
    default void setBookStatus(Book book, @MappingTarget BookDto bookDto) {
        if (book.getUserId() == null) {
            bookDto.setBookStatus(BookStatus.FREE);
        } else {
            bookDto.setBookStatus(BookStatus.ASSIGNED);
        }
    }
}
