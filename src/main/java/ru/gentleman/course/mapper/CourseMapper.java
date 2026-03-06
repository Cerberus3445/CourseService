package ru.gentleman.course.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gentleman.course.dto.CourseDto;
import ru.gentleman.course.entity.Course;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    Course toEntity(CourseDto dto);

    CourseDto toDto(Course entity);

    List<CourseDto> toDto(List<Course> entities);
}
