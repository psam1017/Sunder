package net.sunder.english.service.impl;

import lombok.RequiredArgsConstructor;
import net.sunder.english.domain.Book;
import net.sunder.english.domain.Content;
import net.sunder.english.domain.Score;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.ContentType;
import net.sunder.english.repository.BookRepository;
import net.sunder.english.service.TeacherBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherBookServiceImpl implements TeacherBookService {

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public void createBook(String title, String chapter, ContentType contentType, List<String> phrases, List<String> meanings, Teacher teacher) {
        Book book = new Book();
        List<Content> contents = setContents(book, phrases, meanings);

        book.setTitle(title);
        book.setChapter(chapter);
        book.setContentType(contentType);
        book.setContents(contents);
        book.setAmount(contents.size());
        book.setTeacher(teacher);

        List<Book> books = teacher.getBooks();
        books.add(book);
    }

    private List<Content> setContents(Book book, List<String> phrases, List<String> meanings) {
        List<Content> contents = new ArrayList<>();
        for (int i = 0; i < phrases.size(); i++) {
            Content c = new Content();
            c.setContent(phrases.get(i));
            c.setMeaning(meanings.get(i));
            c.setBook(book);
            contents.add(c);
        }
        return contents;
    }

    @Override
    public List<Book> getBookList(Long teacherId) {
        return bookRepository.findAllByTeacherIdOrderByInfo(teacherId);
    }

    @Override
    public Book getBookByInfo(String title, String chapter, ContentType contentType, Long teacherId) {
        Optional<Book> bookOptional = bookRepository.findByBookInfo(title, chapter, contentType, teacherId);
        return bookOptional.orElseThrow();
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow();
    }

    @Override
    @Transactional
    public void updateBook(String title, String chapter, ContentType contentType, List<String> phrases, List<String> meanings, Teacher teacher, Long bookId) {
        List<Book> books = teacher.getBooks();
        Optional<Book> bookOptional = books.stream().filter(book -> book.getId().equals(bookId)).findAny();
        Book book = bookOptional.orElseThrow();

        book.setTitle(title);
        book.setChapter(chapter);
        book.setContentType(contentType);

        List<Content> oldContents = book.getContents();
        List<Content> updateContents = updateContents(book, oldContents, phrases, meanings);
        book.setAmount(updateContents.size());

        List<Score> scores = book.getScores();
        scores.clear();
    }

    private List<Content> updateContents(Book book, List<Content> contents, List<String> phrases, List<String> meanings) {
        contents.clear();
        for (int i = 0; i < phrases.size(); i++) {
            Content c = new Content();
            c.setContent(phrases.get(i));
            c.setMeaning(meanings.get(i));
            c.setBook(book);
            contents.add(c);
        }
        return contents;
    }

    @Override
    @Transactional
    public void deleteBook(Teacher teacher, Long bookId) {
        List<Book> books = teacher.getBooks();
        Optional<Book> bookOptional = books.stream().filter(book -> book.getId().equals(bookId)).findAny();
        Book deleteBook = bookOptional.orElseThrow();

        // 영속성 관리에서 remove 는 맡기지 않았으므로 직접 삭제
        books.remove(deleteBook);
        bookRepository.deleteById(bookId);
    }

    @Override
    public void deleteAllBook(Teacher teacher) {
        List<Book> books = teacher.getBooks();
        books.clear();
    }
}
