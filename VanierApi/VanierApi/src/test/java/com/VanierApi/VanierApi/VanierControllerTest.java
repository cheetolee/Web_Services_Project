package com.VanierApi.VanierApi;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Optional;

import com.VanierApi.VanierApi.beans.Courses;
import com.VanierApi.VanierApi.beans.Students;
import com.VanierApi.VanierApi.beans.Teachers;
import com.VanierApi.VanierApi.controller.VanierController;
import com.VanierApi.VanierApi.model.Course;
import com.VanierApi.VanierApi.model.Student;
import com.VanierApi.VanierApi.model.Teacher;
import com.VanierApi.VanierApi.repository.CourseRepository;
import com.VanierApi.VanierApi.repository.StudentRepository;
import com.VanierApi.VanierApi.repository.TeacherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(VanierController.class)
public class VanierControllerTest {

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    TeacherRepository teacherRepository;

    @MockBean
    CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    // this method is to return teacher by id test case
    @Test
    void shouldReturnTeacher() throws Exception {
        long id = 1L;
        Teacher teacher = new Teacher(1, "teacherFirst", "teacherLast", "teacher@test");

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
        mockMvc.perform(get("/api/teacher/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.teacherId").value(id))
                .andExpect(jsonPath("$.firstName").value(teacher.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(teacher.getLastName()))
                .andExpect(jsonPath("$.email").value(teacher.getEmail()))
                .andDo(print());
    }

    // this method is to return student by id test case
    @Test
    void shouldReturnStudent() throws Exception {
        long id = 1L;

        Course course = new Course();
        Student student = new Student(1, "firstName", "secondName", "student@test", course);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        mockMvc.perform(get("/api/student/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(id))
                .andExpect(jsonPath("$.firstName").value(student.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(student.getLastName()))
                .andExpect(jsonPath("$.email").value(student.getEmail()))
                //    .andExpect(jsonPath("$.course").value(student.getCourse()))
                .andDo(print());
    }

    // this method is to delete student by id test case
    @Test
    void shouldDeleteStudent() throws Exception {
        long id = 1L;

        doNothing().when(studentRepository).deleteById(id);
        mockMvc.perform(delete("/api/student/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    // this method is to delete teacher by id test case
    @Test
    void shouldDeleteTeacher() throws Exception {
        long id = 1L;

        doNothing().when(teacherRepository).deleteById(id);
        mockMvc.perform(delete("/api/teacher/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

}
