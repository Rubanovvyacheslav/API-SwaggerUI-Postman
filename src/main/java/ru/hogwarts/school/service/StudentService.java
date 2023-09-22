package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class StudentService {

    private final Map<Long, Student> students = new HashMap<>();
    private long id = 0;

    public Student addStudent(Student student) {
        student.setId(++id);
        students.put(id, student);
        return student;
    }

    public Student findStudent(long id) {
        return students.get(id);
    }

    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            students.put(student.getId(), student);
            return student;
        }
        return null;
    }

        public Student deleteStudent(long id) {
            return students.remove(id);
        }

    public Collection<Student> getAllStudent() {
        return students.values();
    }
    public Map<Integer, List<Student>> findStudentByAge(int age) {
        return getAllStudent().stream()
                .filter(student -> student.getAge() == age)
                .collect(groupingBy(Student::getAge, toList()));
    }
}
