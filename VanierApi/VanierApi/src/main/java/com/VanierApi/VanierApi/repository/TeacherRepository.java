package com.VanierApi.VanierApi.repository;

import com.VanierApi.VanierApi.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

// this class is used to save, delete and get teachers directly through the JPA instead of querying
public interface TeacherRepository extends JpaRepository<Teacher, Long>  {
}
