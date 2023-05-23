package net.sunder.english.service;

import lombok.extern.slf4j.Slf4j;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.Grade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:sql/dummy.sql"})
class TeacherStudentServiceTest {

    private final TeacherService teacherService;
    private final OnlyStudentService studentService;
    private final TeacherStudentService teacherStudentService;

    @Autowired
    public TeacherStudentServiceTest(TeacherService teacherService, OnlyStudentService studentService, TeacherStudentService teacherStudentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.teacherStudentService = teacherStudentService;
    }

    @Test
    @DisplayName("선생님은 학생을 등록할 수 있다")
    void createAccountTest() {
        // given
        Teacher loginTeacher = teacherService.login("teacherId", "1234");

        String studentId = "newStudentId1";
        String password = "newPassword1";
        String studentName = "newStudent1";
        Grade grade = Grade.FIRST;

        // when
        boolean result = teacherStudentService.isUniqueStudentId(studentId);
        teacherStudentService.createAccount(studentId, password, studentName, grade, loginTeacher);

        for (Student student : loginTeacher.getStudents()) {
            log.info("student name = {}", student.getStudentName());
        }

        // then
        assertThat(result).isTrue();
        assertThat(loginTeacher.getStudents().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("선생님은 학생 계정을 변경할 수 있다")
    void updateAccountTest() {
        // given
        Teacher loginTeacher = teacherService.login("teacherId", "1234");
        List<Student> students = loginTeacher.getStudents();

        // when
        Student student = students.get(0);
        Long id = student.getId();
        String name = "newName";
        String password = "newPassword1";
        Grade grade = Grade.SECOND;
        teacherStudentService.updateAccount(loginTeacher, id, name, password, grade);

        // then
        Student updatedStudent = studentService.getStudent(id);
        assertThat(updatedStudent.getStudentName()).isEqualTo(name);
    }

    @Test
    @DisplayName("선생님은 학생 계정을 삭제할 수 있다")
    void withdrawTest() {
        // given
        String studentId = "studentId1";

        Teacher loginTeacher = teacherService.login("teacherId", "1234");
        Student loginStudent = studentService.login(studentId, "password1");

        // when
        teacherStudentService.withdraw(loginTeacher, loginStudent.getId());

        // then
        boolean result = teacherStudentService.isUniqueStudentId(studentId);
        assertThat(result).isTrue();
        assertThat(loginTeacher.getStudents().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("선생님이 학생 목록을 가져올 수 있다")
    void getAllStudentTest() {
        // given
        Teacher loginTeacher = teacherService.login("teacherId", "1234");

        // when
        List<Student> students = teacherStudentService.getAllStudent(loginTeacher.getId());

        // then
        assertThat(students.size()).isEqualTo(2);
    }
}