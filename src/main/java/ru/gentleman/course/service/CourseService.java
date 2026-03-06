package ru.gentleman.course.service;

import ru.gentleman.common.service.CrudOperations;
import ru.gentleman.course.entity.Course;

import java.util.UUID;

public interface CourseService extends CrudOperations<Course, UUID> {
}
