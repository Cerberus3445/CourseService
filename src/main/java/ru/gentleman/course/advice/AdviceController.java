package ru.gentleman.course.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gentleman.common.exception.AlreadyExistsException;
import ru.gentleman.common.exception.NotFoundException;
import ru.gentleman.common.exception.StorageException;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class AdviceController {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleException(NotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, this.messageSource.getMessage(
                        exception.getMessageKey(),
                        exception.getArgs(),
                        Locale.getDefault()
                )
        );
        problemDetail.setTitle(
                this.messageSource.getMessage(
                        "error.title.not_found",
                        null,
                        Locale.getDefault()
                )
        );
        return problemDetail;
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ProblemDetail handleException(AlreadyExistsException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, this.messageSource.getMessage(
                        exception.getMessageKey(),
                        exception.getArgs(),
                        Locale.getDefault()
                )
        );
        problemDetail.setTitle(
                this.messageSource.getMessage(
                        "error.title.already_exist",
                        null,
                        Locale.getDefault()
                )
        );
        return problemDetail;
    }

    @ExceptionHandler(StorageException.class)
    public ProblemDetail handleException(StorageException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, this.messageSource.getMessage(
                        "error.storage.details",
                        null,
                        Locale.getDefault()
                )
        );
        problemDetail.setTitle(
                this.messageSource.getMessage(
                        "error.storage.title",
                        null,
                        Locale.getDefault()
                )
        );
        return problemDetail;
    }
}
