package ru.gentleman.course.validator.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.course.dto.LessonDto;
import ru.gentleman.course.entity.Lesson;
import ru.gentleman.course.service.LessonService;
import ru.gentleman.course.validator.LessonUpdateValidator;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultLessonUpdateValidator implements LessonUpdateValidator {

    private final LessonService lessonService;

    @Override
    public void validate(UUID id, LessonDto lessonDto) {
        log.info("validate {}", lessonDto);
        Optional<Lesson> foundLesson = this.lessonService.getByTitle(lessonDto.title());

        if(foundLesson.isPresent() && !Objects.equals(id, foundLesson.get().getId())
                && lessonDto.title().equalsIgnoreCase(foundLesson.get().getTitle())){
            throw ExceptionUtils.alreadyExists("error.lesson.already_exist", lessonDto.title());
        }
    }
}
