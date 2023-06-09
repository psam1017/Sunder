package net.sunder.english.repository;

import lombok.extern.slf4j.Slf4j;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.Grade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@Transactional
class JpaStudyTest {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public JpaStudyTest(TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Test
    @DisplayName("Spring Data JPA 는 메서드 쿼리로 조회가 가능하다")
    void findTeacherTest(){
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("id1");
        teacher.setTeacherName("teacher");
        teacher.setPassword("1234");

        // when
        Teacher savedTeacher = teacherRepository.save(teacher);
        Teacher foundTeacher = teacherRepository.findOne(Example.of(teacher)).get();

        // then
        log.info("teacher name = {}", foundTeacher.getTeacherName());
        log.info("teacher password = {}", foundTeacher.getPassword());

        assertThat(savedTeacher).isEqualTo(teacher);
        assertThat(foundTeacher).isEqualTo(teacher);
    }

    @Test
    @DisplayName("영속 상태에 있는 객체는 영속성 컨텍스트에 의해 관리된다")
    void saveTeacherTest() {
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("id1");
        teacher.setTeacherName("teacher");
        teacher.setPassword("1234");

        teacherRepository.save(teacher);

        // when
        teacher.setTeacherName("new teacher");
        teacher.setPassword("new Sun12#$");

        Teacher foundTeacher = teacherRepository.findOne(Example.of(teacher)).get();

        // then
        assertThat(foundTeacher).isNotNull();
    }

    @Test
    @DisplayName("인스턴스를 찾지 못하면 예외를 발생시킨다")
    void deleteTeacherTest() {
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("id1");
        teacher.setTeacherName("teacher");
        teacher.setPassword("1234");
        Teacher savedTeacher = teacherRepository.save(teacher);

        // when
        teacherRepository.delete(savedTeacher);

        // then
        assertThatThrownBy(() -> teacherRepository.findOne(Example.of(savedTeacher)).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("연관관계를 이용하여 참조 관계에 있는 객체도 저장할 수 있다")
    void registerStudentTest() {
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("newTeacher");
        teacher.setPassword("1234");
        teacher.setTeacherName("newTeacherName");

        Student student = new Student();
        student.setStudentId("newStudent");
        student.setPassword("1234");
        student.setStudentName("newStudentName");
        student.setGrade(Grade.FIRST);
        student.setTeacher(teacher);

        teacher.getStudents().add(student);
        teacherRepository.save(teacher);

        // then
        Student loginStudent = new Student();
        loginStudent.setStudentId("newStudent");
        loginStudent.setPassword("1234");
        Student foundStudent = studentRepository.findOne(Example.of(loginStudent)).get();

        // then
        assertThat(foundStudent).isNotNull();
    }
}
