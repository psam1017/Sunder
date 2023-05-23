package net.sunder.english.repository;

import lombok.extern.slf4j.Slf4j;
import net.sunder.english.domain.Book;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.Content;
import net.sunder.english.domain.enumtype.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:sql/dummy.sql"})
class BookRepositoryTest {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTest(TeacherRepository teacherRepository, StudentRepository studentRepository, BookRepository bookRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }

    @Test
    @DisplayName("교재명, 단원명, 콘텐츠 유형, 교사 id에 맞는 교재를 찾을 수 있다")
    void findOneScoreByOneStudentAndOneBook() {
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("teacherId");
        teacher.setPassword("1234");
        Teacher foundTeacher = teacherRepository.findOne(Example.of(teacher)).orElseThrow();

        // when
        Book foundBook = bookRepository.findByBookInfo("book1", "chapter1", ContentType.WORD, foundTeacher.getId())
                .orElseThrow();

        // then
        assertThat(foundBook).isNotNull();
    }

    @Test
    @DisplayName("학생이 자신의 선생님이 등록한 교재 목록을 조회할 수 있다")
    void findAllBookByTeacherTest() {
        // given
        Student student = new Student();
        student.setStudentId("studentId1");
        student.setPassword("password1");
        Optional<Student> studentOptional = studentRepository.findOne(Example.of(student));
        Student foundStudent = studentOptional.orElseThrow();

        // when
        Long teacherId = foundStudent.getTeacher().getId();
        List<Book> books = bookRepository.findAllByTeacherIdOrderByInfo(teacherId);

        // then
        assertThat(books.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("학생이 한 교재의 모든 단어를 조회할 수 있다")
    void findAllContentByOneBookTest() {
        // given
        Student student = new Student();
        student.setStudentId("studentId1");
        student.setPassword("password1");
        Optional<Student> studentOptional = studentRepository.findOne(Example.of(student));
        Student foundStudent = studentOptional.orElseThrow();

        Long teacherId = foundStudent.getTeacher().getId();

        // when
        Optional<Book> bookOptional = bookRepository.findByBookInfo("book1", "chapter1", ContentType.WORD, teacherId);
        Book foundBook = bookOptional.orElseThrow();
        List<Content> contents = foundBook.getContents();

        // then
        assertThat(contents.size()).isEqualTo(10);
    }
}
