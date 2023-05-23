package net.sunder.english.service;

import net.sunder.english.domain.Book;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.ContentType;

import java.util.List;

public interface OnlyStudentService {

    Student login(String loginId, String password);
    Student getStudent(Long studentId);
    Teacher getTeacher(Long teacherId);
    List<Book> getBookList(Long teacherId);
    Book getBookByInfo(String title, String chapter, ContentType contentType, Long teacherId);
    Book getBookById(Long bookId);
}
