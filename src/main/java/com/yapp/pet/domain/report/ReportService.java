package com.yapp.pet.domain.report;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.event.AccountDeletedEvent;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.event.ClubDeletedEvent;
import com.yapp.pet.domain.club.repository.jpa.ClubRepository;
import com.yapp.pet.domain.comment.Comment;
import com.yapp.pet.domain.comment.CommentRepository;
import com.yapp.pet.domain.comment.event.CommentDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.yapp.pet.global.TogaetherConstants.NUMBER_OF_LIMIT_REPORTS;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ApplicationEventPublisher eventPublisher;

    private final ReportRepository reportRepository;
    private final AccountRepository accountRepository;
    private final ClubRepository clubRepository;
    private final CommentRepository commentRepository;

    private boolean isSameReporterAccount(Account reporterAccount, List<Report> findReports){
        return findReports.stream()
                .anyMatch(report -> report.getReporterAccountId().equals(reporterAccount.getId()));
    }

    private boolean isReportedMoreThanThree(List<Report> findReports){
        return findReports.size() >= NUMBER_OF_LIMIT_REPORTS;
    }

    public Boolean createClubReport(Long clubId, Account reporterAccount){

        Club findClub = clubRepository.findById(clubId).orElseThrow(EntityNotFoundException::new);

        List<Report> findClubReports = reportRepository.findClubReportsByClubId(clubId);

        if (isSameReporterAccount(reporterAccount, findClubReports)) {
            return false;
        }

        Report report = Report.builder()
                .reporterAccountId(reporterAccount.getId())
                .reportedClubId(findClub.getId())
                .build();

        reportRepository.save(report);
        findClubReports.add(report);

        if(isReportedMoreThanThree(findClubReports)){
            deleteReportAndClub(findClubReports, findClub);
        }

        return true;
    }

    private void deleteReportAndClub(List<Report> findClubReports, Club findClub) {
        reportRepository.deleteAll(findClubReports);
        eventPublisher.publishEvent(
                ClubDeletedEvent.from(findClub.getId())
        );
    }

    public Boolean createCommentReport(Long commentId, Account reporterAccount){

        Comment findComment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);

        List<Report> findCommentReports = reportRepository.findCommentReportsByCommentId(commentId);

        if (isSameReporterAccount(reporterAccount, findCommentReports)) {
            return false;
        }

        Report report = Report.builder()
                .reporterAccountId(reporterAccount.getId())
                .reportedCommentId(findComment.getId())
                .build();

        reportRepository.save(report);
        findCommentReports.add(report);

        if(isReportedMoreThanThree(findCommentReports)){
            deleteReportAndComment(findCommentReports, findComment);
        }

        return true;
    }

    private void deleteReportAndComment(List<Report> findCommentReports, Comment findComment) {
        reportRepository.deleteAll(findCommentReports);
        eventPublisher.publishEvent(
                CommentDeletedEvent.from(findComment)
        );
    }

    public Boolean createAccountReport(Long reportedAccountId, Account reporterAccount){

        Account findReportedAccount
                = accountRepository.findById(reportedAccountId).orElseThrow(EntityNotFoundException::new);

        List<Report> findAccountReports = reportRepository.findAccountReportsByAccountId(reportedAccountId);

        if (isSameReporterAccount(reporterAccount, findAccountReports)) {
            return false;
        }

        Report report = Report.builder()
                .reporterAccountId(reporterAccount.getId())
                .reportedAccountId(findReportedAccount.getId())
                .build();

        reportRepository.save(report);
        findAccountReports.add(report);

        if(isReportedMoreThanThree(findAccountReports)){
            deleteReportAndAccount(findAccountReports, findReportedAccount);
        }

        return true;
    }

    private void deleteReportAndAccount(List<Report> findAccountReports, Account findReportedAccount) {
        reportRepository.deleteAll(findAccountReports);
        eventPublisher.publishEvent(
                AccountDeletedEvent.from(findReportedAccount)
        );
    }

}
