package ru.gentleman.course.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gentleman.course.dto.CourseDto;
import ru.gentleman.course.entity.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "course.id", source = "courseId")
    Course toEntity(CourseDto dto);

    @Mapping(target = "courseId", source = "course.id")
    CourseDto toDto(Course entity);
}
