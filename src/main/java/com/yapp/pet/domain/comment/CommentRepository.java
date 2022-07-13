package com.yapp.pet.domain.comment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"account", "club"})
    List<Comment> findCommentByClubId(long clubId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from Comment c where c.id in :ids")
    int deleteCommentByIds(@Param("ids") List<Long> ids);

    @Query("select c.id from Comment c join c.account a where a.id = :id")
    List<Long> findCommentIdsByAccountId(@Param("id") long accountId);
}
