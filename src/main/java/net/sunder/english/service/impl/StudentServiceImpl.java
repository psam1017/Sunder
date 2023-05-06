package net.sunder.english.service.impl;

import lombok.RequiredArgsConstructor;
import net.sunder.english.domain.Student;
import net.sunder.english.repository.jpa.StudentRepository;
import net.sunder.english.service.StudentService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public boolean isExistStudentId(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }

    @Override
    @Transactional
    public void createAccount(Student student) {
        if (isExistStudentId(student.getStudentId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        else{
            studentRepository.save(student);
        }
    }

    @Override
    public Long login(Student student) {
        Optional<Student> studentOptional = studentRepository.findOne(Example.of(student));
        return studentOptional.orElseThrow(NoSuchElementException::new).getId();
    }

    @Override
    public void updateAccount(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void withdraw(Long studentId) {
        studentRepository.deleteById(studentId);
    }
}
