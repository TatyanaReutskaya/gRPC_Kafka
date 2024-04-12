package com.example.libraryserviceapp.dto;

import com.example.libraryserviceapp.entity.enums.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String id;
    private String title;
    private String author;
    BookStatus bookStatus;
}
