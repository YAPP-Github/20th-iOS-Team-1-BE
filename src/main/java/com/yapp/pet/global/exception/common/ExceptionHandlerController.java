package com.yapp.pet.global.exception.common;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.yapp.pet.global.exception.common.response.ExceptionResponse;
import com.yapp.pet.global.exception.common.response.ExceptionResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * @Valid,@Validated 검증으로 binding 못할 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        errorLogging(exception);
        return ExceptionResponse.of(ExceptionStatus.INVALID_INPUT_VALUE_EXCEPTION,
                exception.getBindingResult());
    }

    /**
     * @RequestParam enum type 불일치로 binding 못할 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ExceptionResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        errorLogging(exception);
        return ExceptionResponse.of(exception);
    }

    /**
     * @ModelAttribute 나 RequestBody 로 @Valid 로 Binding 못할 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    private ExceptionResponse handleBindException(BindException exception) {
        errorLogging(exception);
        return ExceptionResponse.of(ExceptionStatus.INVALID_TYPE_VALUE_EXCEPTION,
                exception.getBindingResult());
    }

    /**
     * 지원하지 않는 HTTP METHOD 를 요청 했을때
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ExceptionResponse handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        errorLogging(exception);
        return ExceptionResponse.of(exception);
    }

    /*
    * 커스텀 예외 핸들러
    * */
    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<ExceptionResponseInfo> handleStatusException(BusinessException exception) {
        ExceptionResponseInfo response = ExceptionResponseInfo.from(exception);
        HttpStatus httpStatus = exception.getHttpStatus();
        errorLogging(exception);
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ExceptionResponse handleEntityNotFoundException(EntityNotFoundException exception){
        errorLogging(exception);
        return ExceptionResponse.of(exception);
    }

    /**
     * @RequestBody 에 잘못된 타입을 보냈을 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    private ExceptionResponse handleInvalidFormatException(InvalidFormatException exception) {
        errorLogging(exception);
        return ExceptionResponse.of(exception);
    }

    private void errorLogging(Exception ex) {
        log.error("Exception = {} , message = {}", ex.getClass().getSimpleName(),
                ex.getLocalizedMessage());
    }

}
