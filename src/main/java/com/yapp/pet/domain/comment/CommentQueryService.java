package com.yapp.pet.domain.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public Comment findCommentById(long commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(NoSuchElementException::new);
    }
}
