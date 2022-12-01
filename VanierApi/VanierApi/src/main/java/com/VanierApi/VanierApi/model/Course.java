package com.VanierApi.VanierApi.model;

import javax.persistence.*;
import java.util.Set;

// JPA for mapping in database
@Entity
@Table(name = "course")
public class Course {

    @Id
    @Column(name = "course_id")
    private long courseId;

    @Column(name = "course_name")
    private String name;
    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "course")
    private Set<Student> students;

    public Course() {
    }

    public Course(long courseId, String name, Teacher teacher) {
        this.courseId = courseId;
        this.name = name;
        this.teacher = teacher;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
