package com.VanierApi.VanierApi.repository;

import com.VanierApi.VanierApi.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

// this class is used to save, delete and get courses directly through the JPA instead of querying
public interface CourseRepository extends JpaRepository<Course, Long>  {
}
