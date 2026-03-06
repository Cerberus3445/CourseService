package ru.gentleman.course.service;

import ru.gentleman.common.service.CrudOperations;
import ru.gentleman.course.dto.CourseDto;

import java.util.UUID;

public interface CourseService extends CrudOperations<CourseDto, UUID> {
}
