package net.sunder.english.service;

import lombok.extern.slf4j.Slf4j;
import net.sunder.english.domain.Book;
import net.sunder.english.domain.Content;
import net.sunder.english.domain.Score;
import net.sunder.english.domain.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static net.sunder.english.domain.enumtype.ContentType.WORD;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:sql/dummy.sql"})
class StudyServiceTest {

    private final OnlyStudentService studentService;
    private final StudyService studyService;

    @Autowired
    public StudyServiceTest(OnlyStudentService studentService, StudyService studyService) {
        this.studentService = studentService;
        this.studyService = studyService;
    }

    @Test
    @DisplayName("학생이 시험을 치를 수 있다")
    void examTest() {
        // given
        Student loginStudent = studentService.login("studentId1", "password1");
        Long teacherId = loginStudent.getTeacher().getId();

        Book examBook = studentService.getBookByInfo("book1", "chapter1", WORD, teacherId);

        // when
        List<Content> examList = studyService.startExam(examBook.getId());

        List<Content> submitList = new ArrayList<>();

        for (int i = 0; i < examList.size(); i++) {
            Content sc = new Content();
            Content oc = examList.get(i);
            sc.setId(oc.getId());
            sc.setBook(examBook);
            sc.setContent(oc.getContent().toString());
            sc.setMeaning("meaning" + i);
            if(i % 2 == 0){
                sc.setContent("wrong" + i);
            }
            submitList.add(sc);
        }

        studyService.finishExam(loginStudent.getId(), examBook.getId(), submitList);

        // then
        List<Score> scores = loginStudent.getScores();
        Score examScore = scores.stream()
                .filter(score -> score.getBook().getId().equals(examBook.getId()))
                .findAny().orElseThrow();

        assertThat(scores.size()).isEqualTo(2);
        assertThat(examScore.getWrongCount()).isEqualTo(5);
    }
}