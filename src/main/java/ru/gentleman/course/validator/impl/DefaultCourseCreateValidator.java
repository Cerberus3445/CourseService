package ru.gentleman.course.validator.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.course.dto.CourseDto;
import ru.gentleman.course.service.CourseService;
import ru.gentleman.course.validator.CourseCreateValidator;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultCourseCreateValidator implements CourseCreateValidator {

    private final CourseService courseService;

    @Override
    public void validate(CourseDto courseDto) {
        log.info("validate {}", courseDto);

        if(this.courseService.getByTitle(courseDto.title()).isPresent()){
            throw ExceptionUtils.alreadyExists("error.lesson.already_exist", courseDto.title());
        }
    }
}
