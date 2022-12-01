package com.VanierApi.VanierApi.repository;

import com.VanierApi.VanierApi.model.Course;
import com.VanierApi.VanierApi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// this class is used to save, delete and get students directly through the JPA instead of querying
public interface StudentRepository extends JpaRepository<Student, Long>  {
    // method will all those students that have same course id to identify
    // if 1 course is not registered by more than 30 students
    List<Student> findByCourse(Course course);
}
