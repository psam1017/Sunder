package net.sunder.english.repository;

import lombok.extern.slf4j.Slf4j;
import net.sunder.english.domain.Book;
import net.sunder.english.domain.Score;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.ContentType;
import net.sunder.english.repository.jpa.BookRepository;
import net.sunder.english.repository.jpa.ScoreRepository;
import net.sunder.english.repository.jpa.TeacherRepository;
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
class ScoreRepositoryTest {


    private final TeacherRepository teacherRepository;
    private final BookRepository bookRepository;
    private final ScoreRepository scoreRepository;

    @Autowired
    public ScoreRepositoryTest(TeacherRepository teacherRepository, BookRepository bookRepository, ScoreRepository scoreRepository) {
        this.teacherRepository = teacherRepository;
        this.bookRepository = bookRepository;
        this.scoreRepository = scoreRepository;
    }

    @Test
    @DisplayName("선생님이 한 학생의 한 교재 성적을 조회할 수 있다")
    void findOneScoreByOneStudentAndOneBook() {
        // given
        Teacher teacher = new Teacher();
        teacher.setTeacherId("teacherId");
        teacher.setPassword("1234");
        Teacher foundTeacher = teacherRepository.findOne(Example.of(teacher)).orElseThrow(NoSuchElementException::new);

        Book foundBook = bookRepository.findByBookInfo("book1", "chapter1", ContentType.WORD, foundTeacher.getId())
                .orElseThrow(NoSuchElementException::new);

        List<Student> students = foundTeacher.getStudents();
        Student foundStudent = students.stream()
                .filter(student -> student.getStudentName().equals("student1"))
                .findAny().orElseThrow(NoSuchElementException::new);

        // when
        Optional<Score> scoreOptional = scoreRepository.findByStudentIdAndBookId(foundStudent.getId(), foundBook.getId());
        Score foundScore = scoreOptional.orElseThrow(NoSuchElementException::new);

        // then
        assertThat(foundScore.getWrongCount()).isEqualTo(4);
    }
}
