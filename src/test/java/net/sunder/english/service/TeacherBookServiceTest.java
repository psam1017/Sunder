package net.sunder.english.service;

import lombok.extern.slf4j.Slf4j;
import net.sunder.english.domain.Book;
import net.sunder.english.domain.Content;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static net.sunder.english.domain.enumtype.ContentType.SUB_TEXT;
import static net.sunder.english.domain.enumtype.ContentType.WORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:sql/dummy.sql"})
class TeacherBookServiceTest {

    private final TeacherService teacherService;
    private final TeacherBookService teacherBookService;

    @Autowired
    public TeacherBookServiceTest(TeacherService teacherService, TeacherBookService teacherBookService) {
        this.teacherService = teacherService;
        this.teacherBookService = teacherBookService;
    }

    @Test
    @DisplayName("선생님이 교재를 등록하고 조회할 수 있다")
    void createBookTest() {
        //given
        Teacher loginTeacher = teacherService.login("teacherId", "1234");

        String title = "newBook";
        String chapter = "1-1";
        ContentType contentType = SUB_TEXT;
        List<String> phrases = new ArrayList<>();
        List<String> meanings = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            phrases.add("con" + i);
            meanings.add("mean" + i);
        }

        // when
        teacherBookService.createBook(title, chapter, contentType, phrases, meanings, loginTeacher);

        // then
        List<Book> books = loginTeacher.getBooks();
        Book foundBook = teacherBookService.getBookByInfo(title, chapter, contentType, loginTeacher.getId());
        List<Content> contents = foundBook.getContents();

        assertThat(books.size()).isEqualTo(3);
        assertThat(contents.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("선생님이 교재 목록을 가져올 수 있다")
    void getBookTest() {
        //given
        Teacher loginTeacher = teacherService.login("teacherId", "1234");
        Long loginTeacherId = loginTeacher.getId();

        // when
        List<Book> books = teacherBookService.getBookList(loginTeacherId);

        // then
        assertThat(books.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("선생님이 교재를 수정할 수 있다")
    void updateBook() {
        // given
        Teacher loginTeacher = teacherService.login("teacherId", "1234");
        Book foundBook = teacherBookService.getBookByInfo("book1", "chapter1", WORD, loginTeacher.getId());

        Long bookId = foundBook.getId(); // 실제로는 param 값과 함께 bookId도 받음
        String title = "newBook1";
        String chapter = "newChapter1";
        ContentType contentType = WORD;
        List<String> phrases = new ArrayList<>();
        List<String> meanings = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            phrases.add("con" + i);
            meanings.add("mean" + i);
        }

        // when
        teacherBookService.updateBook(title, chapter, contentType, phrases, meanings, loginTeacher, bookId);

        // then
        assertThat(foundBook.getContents().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("선생님이 교재를 하나 또는 모두 삭제할 수 있다")
    void deleteBookTest() {
        // given
        Teacher loginTeacher = teacherService.login("teacherId", "1234");
        List<Book> books = loginTeacher.getBooks();
        Book book = books.get(0);

        // when 1
        teacherBookService.deleteBook(loginTeacher, book.getId());

        // then 1
        assertThat(books.size()).isEqualTo(1);

        // when 2
        teacherBookService.deleteAllBook(loginTeacher);

        // then
        assertThatThrownBy(() -> teacherBookService.getBookByInfo("book1", "chapter1", WORD, loginTeacher.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
