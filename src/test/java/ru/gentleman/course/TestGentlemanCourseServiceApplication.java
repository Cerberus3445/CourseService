package ru.gentleman.course;

import org.springframework.boot.SpringApplication;

public class TestGentlemanCourseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(GentlemanCourseServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
