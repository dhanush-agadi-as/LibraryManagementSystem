package com.example.LibraryManagement.service;

import com.example.LibraryManagement.entity.Book;
import com.example.LibraryManagement.entity.IssueRecord;
import com.example.LibraryManagement.entity.User;
import com.example.LibraryManagement.repository.BookRepository;
import com.example.LibraryManagement.repository.IssueRecordRepository;
import com.example.LibraryManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class IssueRecordService {

    @Autowired
    private IssueRecordRepository issueRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public IssueRecord issueBook(Long bookId){
        IssueRecord issueRecord = new IssueRecord();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("book not found"));
        if(book.getQuantity() <= 0 || book.getAvailable() == false){
            throw new RuntimeException("book quantity less than 0");
        }

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User  user = userRepository.findByName(userName).orElseThrow(() -> new RuntimeException("user not found"));

        issueRecord.setIssueDate(LocalDate.now());
        issueRecord.setDueDate(LocalDate.now().plusDays(14));
        issueRecord.setIsReturned(false);
        issueRecord.setUser(user);
        issueRecord.setBook(book);

        book.setQuantity(book.getQuantity() - 1);
        if(book.getQuantity() <= 0){
            book.setAvailable(false);
        }

        return issueRecordRepository.save(issueRecord);

    }

    public IssueRecord returnTheBook(Long issueId){

        IssueRecord issueRecord = issueRecordRepository.findById(issueId).orElseThrow(() -> new RuntimeException("issueRecord not found"));

        if(issueRecord.getIsReturned()){
            throw new RuntimeException("issueRecord is already returned");
        }

        Book book =  issueRecord.getBook();

        book.setAvailable(true);
        book.setQuantity(book.getQuantity() + 1);

        bookRepository.save(book);

        issueRecord.setReturnedDate(LocalDate.now());
        issueRecord.setIsReturned(true);

        return issueRecordRepository.save(issueRecord);

    }


}
