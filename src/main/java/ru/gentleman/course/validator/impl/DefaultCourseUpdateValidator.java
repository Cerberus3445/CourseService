package ru.gentleman.course.validator.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.course.dto.CourseDto;
import ru.gentleman.course.entity.Course;
import ru.gentleman.course.service.CourseService;
import ru.gentleman.course.validator.CourseUpdateValidator;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultCourseUpdateValidator implements CourseUpdateValidator {

    private final CourseService courseService;

    @Override
    public void validate(CourseDto courseDto) {
        log.info("validate {}", courseDto);
        Optional<Course> foundCourse = this.courseService.getByTitle(courseDto.title());

        if(foundCourse.isPresent() && !Objects.equals(courseDto.id(), foundCourse.get().getId())
                && courseDto.title().equalsIgnoreCase(foundCourse.get().getTitle())){
            throw ExceptionUtils.alreadyExists("error.lesson.already_exist", courseDto.title());
        }
    }
}
