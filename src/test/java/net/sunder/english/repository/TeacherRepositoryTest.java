package net.sunder.english.repository;

import lombok.extern.slf4j.Slf4j;
import net.sunder.english.domain.*;
import net.sunder.english.domain.enumtype.ContentType;
import net.sunder.english.repository.jpa.TeacherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:sql/dummy.sql"})
class TeacherRepositoryTest {

    /*
    JUnit5 에서 의존성 주입은 @Autowired 로 해야 하는 이유
    https://pinokio0702.tistory.com/189

    findAny 는 탐색한 첫 번째 요소를 반환하고, findFirst 는 탐색된 모든 요소 중 순서상 첫 번째를 반환한다.
    https://codechacha.com/ko/java8-stream-difference-findany-findfirst
     */

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherRepositoryTest(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Test
    @DisplayName(("teacherId 가 이미 존재하는지 확인할 수 있다"))
    void existsByTeacherIdTest() {
        // given
        String teacherId = "teacherId";

        // when
        boolean result = teacherRepository.existsByTeacherId(teacherId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("teacherId 와 password 로 조회할 수 있다")
    void findTeacherTest(){
        // given
        Teacher loginteacher = new Teacher();
        loginteacher.setTeacherId("teacherId");
        loginteacher.setPassword("1234");

        // when
        Teacher foundTeacher = teacherRepository.findOne(Example.of(loginteacher)).orElseThrow(NoSuchElementException::new);

        // then
        assertThat(foundTeacher.getTeacherId()).isEqualTo(loginteacher.getTeacherId());
    }

    @Test
    @DisplayName("선생님이 학생 목록을 조회할 수 있다")
    void findAllStudentTest(){
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("teacherId");
        teacher.setPassword("1234");

        // when
        Teacher foundTeacher = teacherRepository.findOne(Example.of(teacher)).orElseThrow(NoSuchElementException::new);
        List<Student> foundStudents = foundTeacher.getStudents();

        // then
        assertThat(foundStudents.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("선생님이 모든 학생의 모든 성적을 조회할 수 있다")
    void findAllScoreByAllStudentTest() {
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("teacherId");
        teacher.setPassword("1234");
        Teacher foundTeacher = teacherRepository.findOne(Example.of(teacher)).orElseThrow(NoSuchElementException::new);
        List<Student> students = foundTeacher.getStudents();

        // when
        List<Score> scores = new ArrayList<>();
        students.forEach(student -> scores.addAll(student.getScores()));

        // then
        assertThat(scores.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("선생님이 한 학생의 모든 성적을 조회할 수 있다")
    void findAllScoreByOneStudentTest() {
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("teacherId");
        teacher.setPassword("1234");
        Teacher foundTeacher = teacherRepository.findOne(Example.of(teacher)).orElseThrow(NoSuchElementException::new);
        List<Student> students = foundTeacher.getStudents();

        // when
        Optional<Student> studentOptional = students.stream().filter(student -> student.getStudentName().equals("student1")).findAny();
        List<Score> scores = studentOptional.orElseThrow(NoSuchElementException::new).getScores();

        // then
        assertThat(scores.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("선생님이 모든 교재 목록을 조회할 수 있다")
    void findAllBookTest() {
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("teacherId");
        teacher.setPassword("1234");
        Teacher foundTeacher = teacherRepository.findOne(Example.of(teacher)).orElseThrow(NoSuchElementException::new);

        // when
        List<Book> books = foundTeacher.getBooks();

        // then
        assertThat(books.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("선생님이 한 교재의 모든 단어 목록을 조회할 수 있다")
    void findAllWordByBookTest() {
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("teacherId");
        teacher.setPassword("1234");
        Teacher foundTeacher = teacherRepository.findOne(Example.of(teacher)).orElseThrow(NoSuchElementException::new);
        List<Book> books = foundTeacher.getBooks();

        // when
        Optional<Book> bookOptional = books.stream()
                .filter(book -> book.getTitle().equals("book1") && book.getContentType().equals(ContentType.WORD))
                .findAny();
        List<Word> words = bookOptional.orElseThrow(NoSuchElementException::new).getWords();

        // then
        assertThat(words.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("선생님이 한 교재의 모든 성적을 조회할 수 있다")
    void findAllScoreByBookTest() {
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("teacherId");
        teacher.setPassword("1234");
        Teacher foundTeacher = teacherRepository.findOne(Example.of(teacher)).orElseThrow(NoSuchElementException::new);
        List<Book> books = foundTeacher.getBooks();

        // when
        Optional<Book> bookOptional = books.stream()
                .filter(book -> book.getTitle().equals("book1") && book.getContentType().equals(ContentType.WORD))
                .findAny();
        List<Score> scores = bookOptional.orElseThrow(NoSuchElementException::new).getScores();

        // then
        assertThat(scores.size()).isEqualTo(2);
    }
}
