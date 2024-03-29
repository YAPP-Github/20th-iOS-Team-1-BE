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

    @Modifying
    @Transactional
    @Query("delete from Comment c where c.club.id = :id")
    void deleteCommentByClubId(@Param("id") long id);


    @Modifying
    @Transactional
    @Query("delete from Comment c where c.account.id = :id")
    void deleteCommentByAccountId(@Param("id") long id);
}
