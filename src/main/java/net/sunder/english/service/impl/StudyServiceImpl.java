package net.sunder.english.service.impl;

import lombok.RequiredArgsConstructor;
import net.sunder.english.domain.*;
import net.sunder.english.repository.BookRepository;
import net.sunder.english.repository.StudentRepository;
import net.sunder.english.service.StudyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyServiceImpl implements StudyService {

    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    @Override
    public List<Content> startExam(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Book book = bookOptional.orElseThrow();

        List<Content> contentList = book.getContents();
        Collections.shuffle(contentList);

        return contentList;
    }

    @Override
    public List<Content> finishExam(Long studentId, Long bookId, List<Content> submit) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Student student = studentOptional.orElseThrow();

        Optional<Book> originalOptional = bookRepository.findById(bookId);
        Book original = originalOptional.orElseThrow();

        List<Content> submitCopy = new ArrayList<>(submit);
        List<Content> originalContentCopy = new ArrayList<>(original.getContents());

        deletePastResult(student, original);

        sortCopy(submitCopy, originalContentCopy);
        removeMarks(submitCopy, originalContentCopy);
        List<Content> result = compareResult(submitCopy, originalContentCopy);

        getWrongSubmit(submit, result);
        saveExamResult(submit, student, original);

        return submit;
    }

    private void deletePastResult(Student student, Book book){
        List<Long> idList = book.getContents()
                .stream().map(Content::getId)
                .collect(Collectors.toList());

        List<ContentWrong> studentWrongs = student.getContentWrongs();
        studentWrongs.removeIf(wrong -> idList.contains(wrong.getContent().getId()));

        List<Score> scores = student.getScores();
        scores.removeIf(score -> score.getBook().getId().equals(book.getId()));
    }

    private void sortCopy(List<Content> submit, List<Content> original){
        submit.sort(Comparator.comparing(Content::getId));
        original.sort(Comparator.comparing(Content::getId));
    }

    private void removeMarks(List<Content> submit, List<Content> original){
        for (Content content : submit) {
            String sc = content.getContent();
            String replaced = sc.replaceAll("[.,'\":;\\s]", "");
            content.setContent(replaced);
        }
        for (Content content : original) {
            String oc = content.getContent();
            String replaced = oc.replaceAll("[.,'\":;\\s]", "");
            content.setContent(replaced);
        }
    }

    private List<Content> compareResult(List<Content> submit, List<Content> original){
        List<Content> result = new ArrayList<>();
        for (int i = 0; i < original.size(); i++) {
            Content sc = submit.get(i);
            Content oc = original.get(i);
            if (!oc.getContent().equals(sc.getContent())) {
                result.add(sc);
            }
        }
        return result;
    }

    private void getWrongSubmit(List<Content> submit, List<Content> result) {
        submit.removeIf(content -> !result.stream()
                .map(Content::getId)
                .collect(Collectors.toList())
                .contains(content.getId()));
    }

    private void saveExamResult(List<Content> submit, Student student, Book book){
        List<ContentWrong> studentWrongs = student.getContentWrongs();
        List<Content> originalContents = book.getContents();

        for (Content oc : originalContents){
            for(Content sc : submit){
                if (sc.getId().equals(oc.getId())) {
                    ContentWrong cw = new ContentWrong();
                    cw.setContent(oc);
                    cw.setStudent(student);
                    cw.setReply(sc.getContent());
                    studentWrongs.add(cw);
                }
            }
        }

        List<Score> scores = student.getScores();
        Score score = new Score();
        score.setStudent(student);
        score.setBook(book);
        score.setWrongCount(submit.size());
        score.setSubmitDate(LocalDateTime.now());
        scores.add(score);
    }
}
