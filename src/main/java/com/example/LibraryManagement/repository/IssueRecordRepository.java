package com.example.LibraryManagement.repository;

import com.example.LibraryManagement.entity.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRecordRepository extends JpaRepository<IssueRecord,Long> {

}
