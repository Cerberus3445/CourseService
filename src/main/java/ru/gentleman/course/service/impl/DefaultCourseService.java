package ru.gentleman.course.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.course.dto.CourseDto;
import ru.gentleman.course.dto.LessonDto;
import ru.gentleman.course.entity.Course;
import ru.gentleman.course.mapper.CourseMapper;
import ru.gentleman.course.mapper.LessonMapper;
import ru.gentleman.course.repository.CourseRepository;
import ru.gentleman.course.service.CourseService;
import ru.gentleman.course.service.StorageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultCourseService implements CourseService {

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final StorageService storageService;

    private final LessonMapper lessonMapper;

    @Override
    @Cacheable(value = "course", key = "#id")
    public CourseDto get(UUID id) {
        log.info("get {}", id);

        return this.courseMapper.toDto(this.courseRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.course.not_found_id", id)));
    }

    @Override
    @Cacheable(value = "allCourses")
    public List<CourseDto> getAll() {
        log.info("getAll");

        return this.courseMapper.toDto(this.courseRepository.findAll());
    }

    @Override
    @Cacheable(value = "courseLessons", key = "#id")
    public List<LessonDto> getAllLessonsByCourseId(UUID id) {
        Course course = this.courseRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.course.not_found_id", id));

        return this.lessonMapper.toDto(course.getLessons());
    }

    @Override
    @Transactional
    @CacheEvict(value = "allCourses", allEntries = true)
    public CourseDto create(CourseDto courseDto) {
        log.info("create {}", courseDto);

        Course createdCourse =
                 this.courseRepository.save(this.courseMapper.toEntity(courseDto));

        return this.courseMapper.toDto(createdCourse);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "course", key = "#id"),
            @CacheEvict(value = "allCourses", allEntries = true)
    })
    public void update(UUID id, CourseDto courseDto) {
        log.info("update {}, {}", id, courseDto);

        this.courseRepository.findById(id).ifPresentOrElse(course -> {
            Course updatedCourse = Course.builder()
                    .id(id)
                    .title(courseDto.title())
                    .description(courseDto.description())
                    .imageLink(course.getImageLink())
                    .build();

            this.courseRepository.save(updatedCourse);
        }, () -> {
            throw ExceptionUtils.notFound("error.course.not_found_id", id);
        });
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "course", key = "#id"),
            @CacheEvict(value = "allCourses", allEntries = true),
            @CacheEvict(value = "courseLessons", key = "#id")
    })
    public void delete(UUID id) {
        log.info("delete {}", id);
        Course course = this.courseRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.courser.not_found_id", id));

        this.courseRepository.delete(course);
    }

    @Override
    public Optional<Course> getByTitle(String title) {
        log.info("getByTitle {}", title);

        return this.courseRepository.findByTitle(title);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "course", key = "#id"),
            @CacheEvict(value = "allCourses", allEntries = true)
    })
    public void assignImage(UUID id, MultipartFile image) {
        log.info("assignImage {}", id);

        Course course = this.courseRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.course.not_found_id", id));

        String imageName = this.storageService.uploadImage(image);
        course.setImageLink(imageName);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "course", key = "#id"),
            @CacheEvict(value = "allCourses", allEntries = true)
    })
    public void deleteImage(UUID id, String imageName) {
        log.info("deleteImage {}, {}", id, imageName);

        Course course = this.courseRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.course.not_found_id", id));

        this.storageService.deleteImage(imageName);
        course.setImageLink(null);
    }
}
