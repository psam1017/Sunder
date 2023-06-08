package net.sunder.english.service;

import net.sunder.english.domain.Book;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.ContentType;

import java.util.List;

public interface OnlyStudentService {

    Student login(String loginId, String password);
    Student getStudent(Long studentId);
    Teacher getTeacher(Long teacherId); // session 에 teacherId 를 저장할 의도였으나, 실제로 사용하진 않음.
    List<Book> getBookList(Long teacherId);
    Book getBookByInfo(String title, String chapter, ContentType contentType, Long teacherId);
    Book getBookById(Long bookId);
}
