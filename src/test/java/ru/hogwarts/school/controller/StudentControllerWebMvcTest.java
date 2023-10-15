package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void getStudent() throws Exception {
        Long id = 1L;
        String name = "testStudent";
        int age = 22;

        Student student2 = new Student();
        student2.setId(id);
        student2.setName(name);
        student2.setAge(age);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void createStudent() throws Exception {
        Student studentForCreate = new Student("Иван", 20);

        String request = objectMapper.writeValueAsString(studentForCreate);

        Student studentWithId = new Student("Иван", 20);
        long id = 1l;
        studentWithId.setId(id);

        when(studentRepository.save(studentForCreate)).thenReturn(studentWithId);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student") //send
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(studentForCreate.getName()))
                .andExpect(jsonPath("$.age").value(studentForCreate.getAge()));
    }

    @Test
    void editStudent() throws Exception{
        final String name = "testStudent";
        final int age = 22;
        final long id = 1;
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }


    @Test
    void deleteStudent() throws Exception {
        Long id = 1L;
        String name = "testStudent";
        int age = 22;

        Student student2 = new Student();
        student2.setId(id);
        student2.setName(name);
        student2.setAge(age);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student2));
        doNothing().when(studentRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void findStudentByAge() throws Exception {
        long id = 100L;
        String name = "testStudent";
        int age = 22;

        Student student2 = new Student();
        student2.setId(id);
        student2.setName(name);
        student2.setAge(age);

        when(studentRepository.findStudentByAge(22)).thenReturn(List.of(student2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age/22")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



    @Test
    void getByAgeBetween() throws Exception {
        Long id = 1L;
        String name = "testStudent";
        int age = 22;
        Long id2 = 2L;
        String name2 = "testStudent2";
        int age2 = 23;


        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        when(studentRepository.findAllByAgeBetween(20, 30)).thenReturn(List.of(student, student2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age-between?min=20&max=30")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].name").value("testStudent2"))
                .andExpect(jsonPath("$[0].age").value(22));
    }
}