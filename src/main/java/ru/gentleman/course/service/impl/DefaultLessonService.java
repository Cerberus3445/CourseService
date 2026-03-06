package ru.gentleman.course.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.course.dto.LessonDto;
import ru.gentleman.course.entity.Lesson;
import ru.gentleman.course.mapper.LessonMapper;
import ru.gentleman.course.repository.LessonRepository;
import ru.gentleman.course.service.LessonService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultLessonService implements LessonService {

    private final LessonRepository lessonRepository;

    private final LessonMapper lessonMapper;

    @Override
    @Cacheable(value = "lesson", key = "#id")
    public LessonDto get(UUID id) {
        log.info("get {}", id);

        return this.lessonMapper.toDto(this.lessonRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.lesson.not_found_id", id)));
    }

    @Override
    @Cacheable(value = "allLessons")
    public List<LessonDto> getAll() {
        log.info("getAll");

        return this.lessonMapper.toDto(this.lessonRepository.findAll());
    }

    @Override
    @Transactional
    @CacheEvict(value = "allLessons", allEntries = true)
    public LessonDto create(LessonDto courseDto) {
        log.info("create {}", courseDto);

        Lesson createdLesson =
                this.lessonRepository.save(this.lessonMapper.toEntity(courseDto));

        return this.lessonMapper.toDto(createdLesson);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "lesson", key = "#id"),
            @CacheEvict(value = "allLessons", allEntries = true)
    })
    public void update(UUID id, LessonDto lessonDto) {
        log.info("update {}, {}", id, lessonDto);

        this.lessonRepository.findById(id).ifPresentOrElse(lesson -> {
            Lesson updatedLesson = Lesson.builder()
                    .id(id)
                    .title(lessonDto.title())
                    .description(lessonDto.description())
                    .content(lessonDto.content())
                    .course(lesson.getCourse())
                    .build();

            this.lessonRepository.save(updatedLesson);
        }, () -> {
            throw ExceptionUtils.notFound("error.lesson.not_found_id", id);
        });
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "lesson", key = "#id"),
            @CacheEvict(value = "allLessons", allEntries = true)
    })
    public void delete(UUID id) {
        log.info("delete {}", id);

        Lesson lesson = this.lessonRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.lesson.not_found_id", id));

        this.lessonRepository.delete(lesson);
    }

    @Override
    public Optional<Lesson> getByTitle(String title) {
        log.info("getByTitle {}", title);

        return this.lessonRepository.findByTitle(title);
    }
}
