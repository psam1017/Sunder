package net.sunder.english.service;

import net.sunder.english.domain.Book;
import net.sunder.english.domain.Content;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.ContentType;

import java.util.List;

public interface TeacherBookService {

    void createBook(String title, String chapter, ContentType contentType, List<String> phrases, List<String> meanings, Teacher teacher);
    List<Book> getBookList(Long teacherId);
    Book getBookByInfo(String title, String chapter, ContentType contentType, Long teacherId);
    Book getBookById(Long bookId);
    void updateBook(String title, String chapter, ContentType contentType, List<String> phrases, List<String> meanings, Teacher teacher, Long bookId);
    void deleteBook(Teacher teacher, Long bookId);
    void deleteAllBook(Teacher teacher);
}
