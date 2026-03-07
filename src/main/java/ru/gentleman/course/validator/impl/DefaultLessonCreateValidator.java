package ru.gentleman.course.validator.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.course.dto.LessonDto;
import ru.gentleman.course.service.LessonService;
import ru.gentleman.course.validator.LessonCreateValidator;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultLessonCreateValidator implements LessonCreateValidator {

    private final LessonService lessonService;

    @Override
    public void validate(LessonDto lessonDto) {
        log.info("validate {}", lessonDto);

        if(this.lessonService.getByTitle(lessonDto.title()).isPresent()){
            throw ExceptionUtils.alreadyExists("error.course.already_exist", lessonDto.title());
        }
    }
}
