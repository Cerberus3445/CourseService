package ru.gentleman.course.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gentleman.course.dto.LessonDto;
import ru.gentleman.course.entity.Lesson;

import java.util.List;


@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(target = "course.id", source = "courseId")
    Lesson toEntity(LessonDto dto);

    @Mapping(target = "courseId", source = "course.id")
    LessonDto toDto(Lesson entity);

    List<LessonDto> toDto(List<Lesson> entities);
}
