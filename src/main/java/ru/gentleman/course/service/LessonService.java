package ru.gentleman.course.service;

import ru.gentleman.common.service.CrudOperations;
import ru.gentleman.course.dto.LessonDto;

import java.util.UUID;

public interface LessonService extends CrudOperations<LessonDto, UUID> {

}
