package ru.gentleman.course.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record LessonDto(
        UUID id,
        UUID courseId,

        String title,

        String description,
        JsonNode content
) {
}
