package ru.gentleman.course;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class GentlemanCourseServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
