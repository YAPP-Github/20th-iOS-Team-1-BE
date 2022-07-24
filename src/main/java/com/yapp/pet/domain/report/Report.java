package com.yapp.pet.domain.report;

import com.yapp.pet.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE report SET deleted = true WHERE report_id = ?")
@Where(clause = "deleted = false")
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    private Long reporterAccountId;

    private Long reportedAccountId;

    private Long reportedClubId;

    private Long reportedCommentId;

    @Column(length = 100)
    private String reason;

    private Boolean deleted;

    @Builder
    public Report(Long reporterAccountId, Long reportedAccountId, Long reportedClubId, Long reportedCommentId,
                  String reason) {
        this.reporterAccountId = reporterAccountId;
        this.reportedAccountId = reportedAccountId;
        this.reportedClubId = reportedClubId;
        this.reportedCommentId = reportedCommentId;
        this.reason = reason;
        this.deleted = false;
    }

}
