package ru.gentleman.course.service;

import org.springframework.web.multipart.MultipartFile;
import ru.gentleman.common.service.CrudOperations;
import ru.gentleman.course.dto.CourseDto;
import ru.gentleman.course.dto.LessonDto;
import ru.gentleman.course.entity.Course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService extends CrudOperations<CourseDto, UUID> {

    List<CourseDto> getAll();

    List<LessonDto> getAllLessonsByCourseId(UUID id);

    Optional<Course> getByTitle(String title);

    void assignImage(UUID id, MultipartFile image);

    void deleteImage(UUID id, String link);
}
