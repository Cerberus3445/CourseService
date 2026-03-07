CREATE SCHEMA IF NOT EXISTS course;

CREATE TABLE IF NOT EXISTS course.courses(
    id UUID PRIMARY KEY ,
    title VARCHAR(100) NOT NULL UNIQUE ,
    description VARCHAR(1000) ,
    image_link VARCHAR
);

CREATE TABLE IF NOT EXISTS course.lessons(
    id UUID PRIMARY KEY ,
    course_id UUID REFERENCES course.courses(id) ON DELETE CASCADE NOT NULL ,
    title VARCHAR(100) NOT NULL UNIQUE ,
    description VARCHAR(1000) ,
    content JSONB
);