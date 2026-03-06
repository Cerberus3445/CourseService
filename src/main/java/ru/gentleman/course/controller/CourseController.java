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
import org.springframework.web.multipart.MultipartFile;
import ru.gentleman.common.exception.ValidationException;
import ru.gentleman.common.util.ValidationErrorUtils;
import ru.gentleman.common.validator.ImageValidator;
import ru.gentleman.course.dto.CourseDto;
import ru.gentleman.course.dto.LessonDto;
import ru.gentleman.course.service.CourseService;
import ru.gentleman.course.validator.CourseCreateValidator;
import ru.gentleman.course.validator.CourseUpdateValidator;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
@Tag(name = "Course Controller", description = "Взаимодействие с курсами")
@RateLimiter(name = "courseLimiter")
public class CourseController {

    private final CourseService courseService;

    private final CourseCreateValidator createValidator;

    private final CourseUpdateValidator updateValidator;

    private final MessageSource messageSource;

    private final ImageValidator imageValidator;

    @GetMapping("/{id}")
    @Operation(summary = "Получить курс")
    public CourseDto get(@PathVariable("id") UUID id){
        return this.courseService.get(id);
    }

    @GetMapping
    @Operation(summary = "Получить все курсы")
    public List<CourseDto> getAll(){
        return this.courseService.getAll();
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "Загрузить изображение для курса")
    public ResponseEntity<String> assignImage(@PathVariable("id") UUID id,
                                              @RequestParam("image") MultipartFile image){
        this.imageValidator.validateImage(image);

        this.courseService.assignImage(id, image);

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.course.image.uploaded",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}/image")
    @Operation(summary = "Удалить изображение курса")
    public ResponseEntity<String> deleteImage(@PathVariable("id") UUID id,
                                              @RequestParam("link") String link){
        this.courseService.deleteImage(id, link);
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.course.image.deleted",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @GetMapping("/{id}/lessons")
    @Operation(summary = "Получить все уроки курса")
    public List<LessonDto> getLessons(@PathVariable("id") UUID id){
        return this.courseService.getAllLessonsByCourseId(id);
    }

    @PostMapping
    @Operation(summary = "Создать курс")
    public ResponseEntity<CourseDto> create(@RequestBody @Valid CourseDto subcategoryDto,
                                                 BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.createValidator.validate(subcategoryDto);

        CourseDto createdCourse = this.courseService.create(subcategoryDto);

        return ResponseEntity
                .created(URI.create("/api/v1/courses/" + createdCourse.id()))
                .body(createdCourse);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить курс")
    public ResponseEntity<String> update(@PathVariable("id") UUID id,
                                         @RequestBody @Valid CourseDto subcategoryDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.updateValidator.validate(id, subcategoryDto);

        this.courseService.update(id, subcategoryDto);

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.course.updated",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить курс")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id){
        this.courseService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
