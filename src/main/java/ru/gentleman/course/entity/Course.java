package ru.gentleman.course.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@Table(schema = "course", name = "courses")
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    private String title;

    private String description;

    private String imageLink;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;
}
