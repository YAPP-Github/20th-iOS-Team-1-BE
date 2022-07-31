package com.yapp.pet.global.exception.comment;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class NotDeleteCommentException extends BusinessException {

    public NotDeleteCommentException() {
        super(ExceptionStatus.NOT_DELETE_COMMENT_EXCEPTION);
    }
}
