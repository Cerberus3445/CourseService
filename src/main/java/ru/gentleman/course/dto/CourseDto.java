package ru.gentleman.course.dto;

import java.util.UUID;

public record CourseDto(
        UUID id,

        String title,

        String description,

        String imageLink
) {
}
