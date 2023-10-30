package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByAgeBetween(int min, int max);

    List<Student> findByFacultyId(Long facultyId);

    List<Student> findStudentByAge(int age);

    @Query(
            value = "SELECT COUNT(*)" +
                    "FROM student",
            nativeQuery = true
    )
    Integer getCount();

    @Query(
            value = "SELECT AVG(age)" +
                    "FROM student",
            nativeQuery = true
    )
    Double getAvgAge();

    @Query(
            value = "SELECT * " +
                    "FROM student s " +
                    "ORDER BY id desc " +
                    "LIMIT 5",
            nativeQuery = true
    )
    List<Student> getLastFive();
}
