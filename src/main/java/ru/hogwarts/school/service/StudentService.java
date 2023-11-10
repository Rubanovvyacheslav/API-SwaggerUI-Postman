package ru.hogwarts.school.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;


import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Был вызван метод addStudent");
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        logger.info("Был вызван метод findStudent");
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        logger.info("Был вызван метод editStudent");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Был вызван метод deleteStudent");
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudent() {
        logger.info("Был вызван метод getAllStudent");
        return studentRepository.findAll();
    }

    public List<Student> findStudentByAge(int age) {
        logger.info("Был вызван метод findStudentByAge");
        return getAllStudent().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public List<Student> getByAgeBetween(int min, int max) {
        logger.info("Был вызван метод getByAgeBetween");
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long id) {
        logger.info("Был вызван метод getFacultyByStudentId");
        return studentRepository.findById(id).get().getFaculty();
    }

    public Integer getCount() {
        logger.info("Был вызван метод getCount");
        return studentRepository.getCount();
    }

    public Double getAvgAge() {
        logger.info("Был вызван метод getAvgAge");
        return studentRepository.getAvgAge();
    }
    public List<Student> getLastFive() {
        logger.info("Был вызван метод getLastFive");
        return studentRepository.getLastFive();
    }

    public List<Student> getByFacultyId(Long facultyId) {
        logger.info("Был вызван метод getByFacultyId");
        return studentRepository.findByFacultyId(facultyId);
    }

    public List<String> getAllStudentsWithFirstLetterH() {
        return studentRepository.findAll().stream()
                .map(student -> student.getName().toUpperCase())
                .filter(name -> name.startsWith("H"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAvgAgeStream() {
        return studentRepository.findAll().stream()
                .mapToDouble(stident -> (double) stident.getAge())
                .average()
                .orElse(0);
    }

    public void threadStudents() {
        List<Student> students = studentRepository.findAll();

        printThread(students.get(0));
        printThread(students.get(1));

        Thread thread1 = new Thread(() -> {
            printThread(students.get(2));
            printThread(students.get(3));
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            printThread(students.get(4));
            printThread(students.get(5));
        });
        thread2.start();
    }

    public void threadStudentsSync() {
        List<Student> students = studentRepository.findAll();

        printThreadSync(students.get(0));
        printThreadSync(students.get(1));

        Thread thread1 = new Thread(() -> {
            printThreadSync(students.get(2));
            printThreadSync(students.get(3));
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            printThreadSync(students.get(4));
            printThreadSync(students.get(5));
        });
        thread2.start();
    }

    private void printThread(Student student) {
        System.out.println(Thread.currentThread().getName() + " " + student.getName());
    }

    private synchronized void printThreadSync(Student student) {
        printThread(student);
    }
}
