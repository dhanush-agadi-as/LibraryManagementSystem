package com.example.LibraryManagement.controller;

import com.example.LibraryManagement.entity.IssueRecord;
import com.example.LibraryManagement.service.IssueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issueRecord")

public class IssueRecordController {

    @Autowired
    private IssueRecordService issueRecordService;

    @GetMapping("/createissue/bookid{bookId}")
    public ResponseEntity<IssueRecord> issueTheBook(@PathVariable Long bookId){
        return ResponseEntity.ok(issueRecordService.issueBook(bookId));
    }

    @PutMapping("/updateissue/{issueid}")
    public ResponseEntity<IssueRecord> returnTheBook(@PathVariable Long issueId){
        return ResponseEntity.ok(issueRecordService.returnTheBook(issueId));
    }



}
