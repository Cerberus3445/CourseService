package ru.gentleman.course.mapper;

import org.mapstruct.Mapper;
import ru.gentleman.course.dto.LessonDto;
import ru.gentleman.course.entity.Lesson;


@Mapper(componentModel = "spring")
public interface LessonMapper {
    Lesson toEntity(LessonDto dto);

    LessonDto toDto(Lesson entity);
}
