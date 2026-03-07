package ru.gentleman.course.service;

import ru.gentleman.common.service.CrudOperations;
import ru.gentleman.course.dto.LessonDto;
import ru.gentleman.course.entity.Lesson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService extends CrudOperations<LessonDto, UUID> {

    List<LessonDto> getAll();

    Optional<Lesson> getByTitle(String title);
}
