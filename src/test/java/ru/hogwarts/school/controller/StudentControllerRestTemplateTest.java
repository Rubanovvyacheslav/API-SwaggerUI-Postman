package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void createStudent() {
        Student studentForCreate = new Student("Иван", 25);
        Student expectedStudent = new Student("Иван", 25);

        Student postedStudent = this.restTemplate.postForObject("http://localhost:" + port + "/student", studentForCreate, Student.class);
        assertThat(postedStudent).isNotNull();
        assertEquals(expectedStudent.getName(), postedStudent.getName());
        assertEquals(expectedStudent.getAge(), postedStudent.getAge());
    }

    @Test
    void getStudent() {
        Student studentForCreate = new Student("Иван", 25);

        Student postedStudent = this.restTemplate.postForObject("http://localhost:" + port + "/student", studentForCreate, Student.class);
        Student actualStudent = this.restTemplate.getForObject("http://localhost:" + port + "/student/" + postedStudent.getId(), Student.class);
        assertThat(actualStudent).isNotNull();
        assertEquals(actualStudent, postedStudent);
    }


    @Test
    void deleteStudent() {
            Student student = new Student();
            student.setName("testStudent");
            student.setAge(22);

            Student postStudent = this.restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
            this.restTemplate.delete("http://localhost:" + port + "/student/" + postStudent.getId(), Student.class);
            Student result = this.restTemplate.getForObject("http://localhost:" + port + "/student/" + postStudent.getId(), Student.class);

            assertEquals(result.toString(), "Student{id=0, name='null', age=0}");
        }

    @Test
    void findStudentByAge() {
        Student studentForCreate = new Student("Петр", 31);
        Student postedStudent = this.restTemplate.postForObject("http://localhost:" + port + "/student", studentForCreate, Student.class);
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/age/31", String.class))
                .isEqualTo("[{\"id\":1,\"name\":\"Петр\",\"age\":31,\"faculty\":null}]");
    }


    @Test
    void getByAgeBetween() {
        Student studentForCreate = new Student("Иван", 25);
        Student studentForCreate2 = new Student("Петр", 28);

        Student postedStudent1 = this.restTemplate.postForObject("http://localhost:" + port + "/student", studentForCreate, Student.class);
        Student postedStuden2 = this.restTemplate.postForObject("http://localhost:" + port + "/student", studentForCreate2, Student.class);
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/age-between/?min=26&max=29", String.class))
                .isEqualTo("[{\"id\":2,\"name\":\"Петр\",\"age\":28,\"faculty\":null}]");
    }

}