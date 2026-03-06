package ru.gentleman.course.validator;

import ru.gentleman.common.validator.UpdateValidator;
import ru.gentleman.course.dto.LessonDto;

import java.util.UUID;

public interface LessonUpdateValidator extends UpdateValidator<LessonDto, UUID> {

}