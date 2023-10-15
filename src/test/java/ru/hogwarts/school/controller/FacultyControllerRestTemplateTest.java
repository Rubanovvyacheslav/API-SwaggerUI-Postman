package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Faculty;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void getFaculty() {
        Faculty facultyForCreate = new Faculty("Слизерин", "зеленый");

        Faculty postedFaculty = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyForCreate, Faculty.class);
        Faculty actualFaculty = this.restTemplate.getForObject("http://localhost:" + port + "/faculty/" + postedFaculty.getId(), Faculty.class);
        assertThat(postedFaculty).isNotNull();
        assertEquals(actualFaculty, postedFaculty);
    }


    @Test
    void createFaculty() {
        Faculty facultyForCreate = new Faculty("Слизерин", "зеленый");
        Faculty expectedFaculty = new Faculty("Слизерин", "зеленый");

        Faculty postedFaculty = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyForCreate, Faculty.class);
        assertThat(postedFaculty).isNotNull();
        assertEquals(expectedFaculty.getName(), postedFaculty.getName());
        assertEquals(expectedFaculty.getColor(), postedFaculty.getColor());
    }

    @Test
    void deleteFaculty() {
        Faculty facultyForCreate = new Faculty("Слизерин", "зеленый");

        Faculty postedFaculty = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyForCreate, Faculty.class);
        this.restTemplate.delete("http://localhost:" + port + "/faculty/" + postedFaculty.getId(), Faculty.class);
        Faculty result = this.restTemplate.getForObject("http://localhost:" + port + "/faculty/" + postedFaculty.getId(), Faculty.class);
        assertEquals(result.toString(), "Faculty{id=0, name='null', color='null'}");

    }

    @Test
    void findFacultyByColor() {

        Faculty facultyForCreate = new Faculty("testFaculty","black");

        var postedFaculty = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyForCreate, Faculty.class);
        var result = this.restTemplate.getForObject("http://localhost:" + port + "/faculty/color/black", String.class);
        assertThat(result.contains("\"testFaculty\",\"color\":\"black\"")).isTrue();


    }
}