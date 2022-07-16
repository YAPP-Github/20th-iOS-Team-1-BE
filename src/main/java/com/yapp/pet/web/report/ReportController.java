package com.yapp.pet.web.report;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.report.ReportService;
import com.yapp.pet.global.annotation.AuthAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/reports/club/{club-id}")
    public ResponseEntity<Boolean> createClubReport(@PathVariable("club-id") Long clubId,
                                                    @AuthAccount Account account){
        Boolean response;

        try {
            response = reportService.createClubReport(clubId, account);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reports/comment/{comment-id}")
    public ResponseEntity<Boolean> createCommentReport(@PathVariable("comment-id") Long commentId,
                                                       @AuthAccount Account account){
        Boolean response;

        try {
            response = reportService.createCommentReport(commentId, account);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reports/account/{account-id}")
    public ResponseEntity<Boolean> createAccountReport(@PathVariable("account-id") Long reportedAccountId,
                                                       @AuthAccount Account account){
        Boolean response;

        try {
            response = reportService.createAccountReport(reportedAccountId, account);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(response);
    }

}
