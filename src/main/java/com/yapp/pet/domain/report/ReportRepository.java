package com.yapp.pet.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select r from Report r where r.reportedClubId = :clubId")
    List<Report> findClubReportsByClubId(@RequestParam("clubId") Long clubId);

    @Query("select r from Report r where r.reportedCommentId = :commentId")
    List<Report> findCommentReportsByCommentId(@RequestParam("commentId") Long commentId);

    @Query("select r from Report r where r.reportedAccountId = :accountId")
    List<Report> findAccountReportsByAccountId(@RequestParam("accountId") Long accountId);

}
