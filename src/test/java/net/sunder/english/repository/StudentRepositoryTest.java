package net.sunder.english.repository;

import lombok.extern.slf4j.Slf4j;
import net.sunder.english.domain.Score;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.enumtype.ContentType;
import net.sunder.english.repository.jpa.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:sql/dummy.sql"})
class StudentRepositoryTest {

    private StudentRepository studentRepository;

    @Autowired
    public StudentRepositoryTest(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Test
    @DisplayName("studentId 가 이미 존재하는지 확인할 수 있다")
    void existsByStudentIdTest() {
        // given
        String studentId = "studentId1";

        // when
        boolean result = studentRepository.existsByStudentId(studentId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("studentId 와 password 로 조회할 수 있다")
    void findStudentTest() {
        // given
        Student loginStudent = new Student();
        loginStudent.setStudentId("studentId1");
        loginStudent.setPassword("password1");

        // when
        Student foundStudent = studentRepository.findOne(Example.of(loginStudent)).get();

        // then
        assertThat(foundStudent.getStudentId()).isEqualTo(loginStudent.getStudentId());
    }

    @Test
    @DisplayName("학생이 모든 교재 성적을 조회할 수 있다")
    void findAllScoreTest() {
        // given
        Student student = new Student();
        student.setStudentId("studentId1");
        student.setPassword("password1");
        Optional<Student> studentOptional = studentRepository.findOne(Example.of(student));
        Student foundStudent = studentOptional.orElseThrow(NoSuchElementException::new);

        // when
        List<Score> scores = foundStudent.getScores();

        // then
        assertThat(scores.size()).isEqualTo(2);
    }
}
