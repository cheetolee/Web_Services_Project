package com.VanierApi.VanierApi.controller;

import com.VanierApi.VanierApi.beans.Courses;
import com.VanierApi.VanierApi.beans.Students;
import com.VanierApi.VanierApi.beans.Teachers;
import com.VanierApi.VanierApi.model.Course;
import com.VanierApi.VanierApi.model.Student;
import com.VanierApi.VanierApi.model.Teacher;
import com.VanierApi.VanierApi.repository.CourseRepository;
import com.VanierApi.VanierApi.repository.StudentRepository;
import com.VanierApi.VanierApi.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class VanierController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    CourseRepository courseRepository;

    // this method will check if provided student id already exist in database, and if exist, error will be thrown
    // in case id does not exist, student entry will be stored in database
    @PostMapping("/student")
    public ResponseEntity<Students> createStudent(@RequestBody Students student) {
        try {

            Optional<Course> courseData = courseRepository.findById(student.getCourseId());

            if (courseData.isPresent()) {

                List<Student> studentListWithSameCourse = studentRepository.findByCourse(courseData.get());

                if(!studentListWithSameCourse.isEmpty() && studentListWithSameCourse.size() >= 30) {
                    return new ResponseEntity<>(null, HttpStatus.INSUFFICIENT_STORAGE);
                }

                Optional<Student> studentData = studentRepository.findById(student.getStudentId());
                if (studentData.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.IM_USED);
                } else {
                    Student _student = studentRepository.save(new Student(student.getStudentId(), student.getFirstName(), student.getLastName(), student.getEmail(), courseData.get()));
                    return new ResponseEntity<>(student, HttpStatus.CREATED);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will fetch the student details against provided student id
    // in case if id does not exist, error will be thrown
    @GetMapping("/student/{id}")
    public ResponseEntity<Students> getStudent(@PathVariable("id") long id) {
        Optional<Student> studentData = studentRepository.findById(id);
        if (studentData.isPresent()) {
            Students student = new Students();
            student.setStudentId(studentData.get().getStudentId());
            student.setFirstName(studentData.get().getFirstName());
            student.setLastName(studentData.get().getLastName());
            student.setEmail(studentData.get().getEmail());
            student.setCourseId(studentData.get().getCourse().getCourseId());

            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // this method will delete student details against provides student id
    @DeleteMapping("/student/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable("id") long id) {
        try {
            studentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will update student details against provided student id
    @PutMapping("/student/{id}")
    public ResponseEntity<HttpStatus> updateStudent(@PathVariable("id") long id, @RequestBody Students student) {
        try {
            Optional<Student> studentData = studentRepository.findById(id);
            Optional<Course> courseData = courseRepository.findById(student.getCourseId());
            if (studentData.isPresent() && courseData.isPresent()) {
                Student _student = studentData.get();
                _student.setFirstName(student.getFirstName());
                _student.setLastName(student.getLastName());
                _student.setEmail(student.getEmail());
                _student.setCourse(courseData.get());
                studentRepository.save(_student);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will fetch all student records from database
    @GetMapping("/student/all")
    public ResponseEntity<List<Students>> getAllStudents(@RequestParam(required = false) String title) {
        try {
            List<Student> students = new ArrayList<Student>();

            studentRepository.findAll().forEach(students::add);

            if (students.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<Students> studentBean = new ArrayList<Students>();
            for (Student stu : students) {
                Students s = new Students();
                s.setStudentId(stu.getStudentId());
                s.setFirstName(stu.getFirstName());
                s.setLastName(stu.getLastName());
                s.setEmail(stu.getEmail());
                s.setCourseId(stu.getCourse().getCourseId());
                studentBean.add(s);
            }

            return new ResponseEntity<>(studentBean, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will check if provided teacher id already exist in database, and if exist, error will be thrown
    // in case id does not exist, teacher entry will be stored in database
    @PostMapping("/teacher")
    public ResponseEntity<Teachers> createTeacher(@RequestBody Teachers teacher) {
        try {
            Optional<Teacher> teacherData = teacherRepository.findById(teacher.getTeacherId());
            if (teacherData.isPresent()) {
                return new ResponseEntity<>(HttpStatus.IM_USED);
            } else {
                Teacher _teacher = teacherRepository.save(new Teacher(teacher.getTeacherId(), teacher.getFirstName(), teacher.getLastName(), teacher.getEmail()));
                Teachers t = new Teachers();
                t.setTeacherId(_teacher.getTeacherId());
                t.setFirstName(_teacher.getFirstName());
                t.setLastName(_teacher.getLastName());
                t.setEmail(_teacher.getEmail());

                return new ResponseEntity<>(t, HttpStatus.CREATED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will fetch the teacher details against provided teacher id
    // in case if id does not exist, error will be thrown
    @GetMapping("/teacher/{id}")
    public ResponseEntity<Teachers> getTeacher(@PathVariable("id") long id) {
        try {
            Optional<Teacher> teacherData = teacherRepository.findById(id);

            if (teacherData.isPresent()) {
                Teachers t = new Teachers();
                t.setTeacherId(teacherData.get().getTeacherId());
                t.setFirstName(teacherData.get().getFirstName());
                t.setLastName(teacherData.get().getLastName());
                t.setEmail(teacherData.get().getEmail());
                return new ResponseEntity<>(t, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will delete teacher details against provided student id
    @DeleteMapping("/teacher/{id}")
    public ResponseEntity<HttpStatus> deleteTeacher(@PathVariable("id") long id) {
        try {
            teacherRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will update teacher details against provided teacher id
    @PutMapping("/teacher/{id}")
    public ResponseEntity<Teachers> updateTeacher(@PathVariable("id") long id, @RequestBody Teachers teacher) {
        try {
            Optional<Teacher> teacherData = teacherRepository.findById(id);

            if (teacherData.isPresent()) {
                Teacher _teacher = teacherData.get();
                _teacher.setFirstName(teacher.getFirstName());
                _teacher.setLastName(teacher.getLastName());
                _teacher.setEmail(teacher.getEmail());
                teacherRepository.save(_teacher);

                Teachers t = new Teachers();
                t.setTeacherId(_teacher.getTeacherId());
                t.setFirstName(_teacher.getFirstName());
                t.setLastName(_teacher.getLastName());
                t.setEmail(_teacher.getEmail());

                return new ResponseEntity<>(t, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will fetch all teacher records from database
    @GetMapping("/teacher/all")
    public ResponseEntity<List<Teachers>> getAllTeachers(@RequestParam(required = false) String title) {
        try {
            List<Teacher> teachers = new ArrayList<Teacher>();

            teacherRepository.findAll().forEach(teachers::add);

            if (teachers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<Teachers> teacherBean = new ArrayList<>();
            for(Teacher tea : teachers) {
                Teachers t = new Teachers();
                t.setTeacherId(tea.getTeacherId());
                t.setFirstName(tea.getFirstName());
                t.setLastName(tea.getLastName());
                t.setEmail(tea.getEmail());
                teacherBean.add(t);
            }

            return new ResponseEntity<>(teacherBean, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will check if provided course id already exist in database, and if exist, error will be thrown
    // in case id does not exist, course entry will be stored in database
    @PostMapping("/course")
    public ResponseEntity<Courses> createCourse(@RequestBody Courses course) {
        try {
            Optional<Course> courseData = courseRepository.findById(course.getCourseId());
            if (courseData.isPresent()) {
                return new ResponseEntity<>(HttpStatus.IM_USED);
            } else {
                Optional<Teacher> teacherData = teacherRepository.findById(course.getTeacher().getTeacherId());
                Course _course = courseRepository.save(new Course(course.getCourseId(), course.getName(), teacherData.get()));

                Teachers teacherBean = new Teachers();
                teacherBean.setTeacherId(_course.getTeacher().getTeacherId());
                teacherBean.setFirstName(_course.getTeacher().getFirstName());
                teacherBean.setLastName(_course.getTeacher().getLastName());
                teacherBean.setEmail(_course.getTeacher().getEmail());

                Courses courseBean = new Courses();
                courseBean.setCourseId(_course.getCourseId());
                courseBean.setName(_course.getName());
                courseBean.setTeacher(teacherBean);

                return new ResponseEntity<>(courseBean, HttpStatus.CREATED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will fetch the course details against provided course id
    // in case if id does not exist, error will be thrown
    @GetMapping("/course/{id}")
    public ResponseEntity<Courses> getCourse(@PathVariable("id") long id) {
        try {
            Optional<Course> courseData = courseRepository.findById(id);

            if (courseData.isPresent()) {

                Teachers t = new Teachers();
                t.setTeacherId(courseData.get().getTeacher().getTeacherId());
                t.setFirstName(courseData.get().getTeacher().getFirstName());
                t.setLastName(courseData.get().getTeacher().getLastName());
                t.setEmail(courseData.get().getTeacher().getEmail());

                List<Students> studentBean = new ArrayList<>();
                for(Student stu : courseData.get().getStudents()) {
                    Students s = new Students();
                    s.setStudentId(stu.getStudentId());
                    s.setFirstName(stu.getFirstName());
                    s.setLastName(stu.getLastName());
                    s.setEmail(stu.getEmail());
                    s.setCourseId(stu.getCourse().getCourseId());
                    studentBean.add(s);
                }

                Courses c = new Courses();
                c.setCourseId(courseData.get().getCourseId());
                c.setName(courseData.get().getName());
                c.setTeacher(t);
                c.setStudents(studentBean);

                return new ResponseEntity<>(c, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will delete course details against provided course id
    @DeleteMapping("/course/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") long id) {
        try {
            courseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will update course details against provided course id
    @PutMapping("/course/{id}")
    public ResponseEntity<Courses> updateCourse(@PathVariable("id") long id, @RequestBody Course course) {
        try {
            Optional<Course> courseData = courseRepository.findById(id);

            if (courseData.isPresent()) {
                Course _course = courseData.get();
                _course.setName(course.getName());
                courseRepository.save(_course);

                Courses c = new Courses();
                c.setCourseId(_course.getCourseId());
                c.setName(_course.getName());

                return new ResponseEntity<>(c, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this method will fetch all course records from database
    @GetMapping("/course/all")
    public ResponseEntity<List<Courses>> getAllCourses(@RequestParam(required = false) String title) {
        try {
            List<Course> courses = new ArrayList<Course>();

            courseRepository.findAll().forEach(courses::add);

            if (courses.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<Courses> courseBean = new ArrayList<Courses>();
            for (Course cou : courses) {
                Courses c = new Courses();
                c.setCourseId(cou.getCourseId());
                c.setName(cou.getName());

                Teachers t = new Teachers();
                t.setTeacherId(cou.getTeacher().getTeacherId());
                t.setFirstName(cou.getTeacher().getFirstName());
                t.setLastName(cou.getTeacher().getLastName());
                t.setEmail(cou.getTeacher().getEmail());

                List<Students> studentBean = new ArrayList<Students>();
                for(Student stu : cou.getStudents()) {
                    Students s = new Students();
                    s.setStudentId(stu.getStudentId());
                    s.setFirstName(stu.getFirstName());
                    s.setLastName(stu.getLastName());
                    s.setEmail(stu.getLastName());
                    studentBean.add(s);
                }
                c.setTeacher(t);
                c.setStudents(studentBean);
                courseBean.add(c);
            }

            return new ResponseEntity<>(courseBean, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
