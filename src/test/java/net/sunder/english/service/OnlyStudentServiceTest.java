package net.sunder.english.service;

import lombok.extern.slf4j.Slf4j;
import net.sunder.english.domain.Book;
import net.sunder.english.domain.Content;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static net.sunder.english.domain.enumtype.ContentType.WORD;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:sql/dummy.sql"})
class OnlyStudentServiceTest {

    private final OnlyStudentService studentService;

    @Autowired
    public OnlyStudentServiceTest(OnlyStudentService studentService) {
        this.studentService = studentService;
    }

    @Test
    @DisplayName("학생이 로그인할 수 있다")
    void loginTest() {
        // given
        String loginId = "studentId1";
        String password = "password1";

        // when
        Student loginStudent = studentService.login(loginId, password);

        // then
        assertThat(loginStudent).isNotNull();
    }

    @Test
    @DisplayName("학생이 교재 목록을 조회할 수 있다")
    void getBookList() {
        // given
        Student loginStudent = studentService.login("studentId1", "password1");

        Teacher teacher = loginStudent.getTeacher();
        Long teacherId = teacher.getId();

        // when
        List<Book> bookList = studentService.getBookList(teacherId);

        // then
        assertThat(bookList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("학생이 교재를 찾아 콘텐츠를 조회할 수 있다")
    void getBookByInfo() {
        // given
        Student loginStudent = studentService.login("studentId1", "password1");

        Teacher teacher = loginStudent.getTeacher();
        Long teacherId = teacher.getId();

        // when
        Book foundBook = studentService.getBookByInfo("book1", "chapter1", WORD, teacherId);

        // then
        List<Content> contents = foundBook.getContents();
        assertThat(contents.size()).isEqualTo(10);
    }
}