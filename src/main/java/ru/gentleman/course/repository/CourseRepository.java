package ru.gentleman.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gentleman.course.entity.Course;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

}
