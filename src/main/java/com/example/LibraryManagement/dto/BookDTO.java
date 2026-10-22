package com.example.LibraryManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookDTO {

    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
    private Boolean available;

}
