package net.sunder.english.service.impl;

import lombok.RequiredArgsConstructor;
import net.sunder.english.domain.Book;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.ContentType;
import net.sunder.english.repository.BookRepository;
import net.sunder.english.repository.StudentRepository;
import net.sunder.english.repository.TeacherRepository;
import net.sunder.english.service.OnlyStudentService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OnlyStudentServiceImpl implements OnlyStudentService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    @Override
    public Student login(String loginId, String password) {
        Student student = new Student();
        student.setStudentId(loginId);
        student.setPassword(password);
        Optional<Student> studentOptional = studentRepository.findOne(Example.of(student));

        return studentOptional.orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Student getStudent(Long studentId) {
        return studentRepository.findById(studentId).orElseThrow();
    }

    @Override
    public Teacher getTeacher(Long teacherId) {
        return teacherRepository.findById(teacherId).orElseThrow();
    }

    @Override
    public List<Book> getBookList(Long teacherId) {
        return bookRepository.findAllByTeacherIdOrderByInfo(teacherId);
    }

    @Override
    public Book getBookByInfo(String title, String chapter, ContentType contentType, Long teacherId) {
        return bookRepository.findByBookInfo(title, chapter, contentType, teacherId).orElseThrow();
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow();
    }
}
