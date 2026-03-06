package ru.gentleman.course.validator;

import ru.gentleman.common.validator.UpdateValidator;
import ru.gentleman.course.dto.CourseDto;

import java.util.UUID;

public interface CourseUpdateValidator extends UpdateValidator<CourseDto, UUID> {
}
