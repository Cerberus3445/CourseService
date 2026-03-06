package ru.gentleman.course.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.gentleman.common.exception.ValidationException;
import ru.gentleman.common.util.ValidationErrorUtils;
import ru.gentleman.course.dto.LessonDto;
import ru.gentleman.course.service.LessonService;
import ru.gentleman.course.validator.LessonCreateValidator;
import ru.gentleman.course.validator.LessonUpdateValidator;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lessons")
@Tag(name = "Course Controller", description = "Взаимодействие с уроками")
@RateLimiter(name = "lessonLimiter")
public class LessonController {

    private final LessonService lessonService;

    private final LessonCreateValidator createValidator;

    private final LessonUpdateValidator updateValidator;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    @Operation(summary = "Получить урок")
    public LessonDto get(@PathVariable("id") UUID id){
        return this.lessonService.get(id);
    }

    @GetMapping
    @Operation(summary = "Получить все уроки(для админ панели)")
    public List<LessonDto> getAll(){
        return this.lessonService.getAll();
    }


    @PostMapping
    @Operation(summary = "Создать урок")
    public ResponseEntity<LessonDto> create(@RequestBody @Valid LessonDto lessonDto,
                                            BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.createValidator.validate(lessonDto);

        LessonDto createdLesson = this.lessonService.create(lessonDto);

        return ResponseEntity
                .created(URI.create("/api/v1/lessons/" + createdLesson.id()))
                .body(createdLesson);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить урок")
    public ResponseEntity<String> update(@PathVariable("id") UUID id,
                                         @RequestBody @Valid LessonDto lessonDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.updateValidator.validate(id, lessonDto);

        this.lessonService.update(id, lessonDto);

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.lesson.updated",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить урок")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id){
        this.lessonService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
