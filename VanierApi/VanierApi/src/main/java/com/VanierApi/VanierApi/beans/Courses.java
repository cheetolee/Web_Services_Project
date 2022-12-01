package com.VanierApi.VanierApi.beans;

import java.util.List;

public class Courses {

    private long courseId;
    private String name;
    private Teachers teacher;
    List<Students> students;

    public Courses() {}

    public Courses(long courseId, String name, Teachers teacher) {
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

    public Teachers getTeacher() {
        return teacher;
    }

    public void setTeacher(Teachers teacher) {
        this.teacher = teacher;
    }

    public List<Students> getStudents() {
        return students;
    }

    public void setStudents(List<Students> students) {
        this.students = students;
    }
}
