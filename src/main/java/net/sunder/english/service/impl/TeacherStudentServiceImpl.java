package net.sunder.english.service.impl;

import lombok.RequiredArgsConstructor;
import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.Grade;
import net.sunder.english.repository.StudentRepository;
import net.sunder.english.service.TeacherStudentService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherStudentServiceImpl implements TeacherStudentService {

    private final StudentRepository studentRepository;

    @Override
    public boolean isUniqueStudentId(String studentId) {
        return !studentRepository.existsByStudentId(studentId);
    }

    @Override
    @Transactional
    public void createAccount(String studentId, String password, String studentName, Grade grade, Teacher teacher) {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setPassword(password);
        student.setStudentName(studentName);
        student.setGrade(grade);
        student.setTeacher(teacher);

        if (isUniqueStudentId(student.getStudentId())) {
            List<Student> students = teacher.getStudents();
            students.add(student);
        }
        else{
            throw new DuplicateKeyException("이미 존재하는 아이디입니다.");
        }
    }

    @Override
    public void updateAccount(Teacher teacher, Long studentId, String studentName, String password, Grade grade) {
        List<Student> students = teacher.getStudents();
        Optional<Student> studentOptional = students.stream().filter(student -> student.getId().equals(studentId)).findAny();
        Student student = studentOptional.orElseThrow();

        student.setStudentName(studentName);
        student.setPassword(password);
        student.setGrade(grade);
    }

    @Override
    @Transactional
    public void withdraw(Teacher teacher, Long studentId) {
        List<Student> students = teacher.getStudents();
        Optional<Student> studentOptional = students.stream().filter(student -> student.getId().equals(studentId)).findAny();
        Student deleteStudent = studentOptional.orElseThrow();

        // 영속성 관리에서 remove 는 맡기지 않았으므로 직접 삭제
        studentRepository.deleteById(studentId);
        students.remove(deleteStudent);
    }

    @Override
    public List<Student> getAllStudent(Long teacherId) {
        return studentRepository.findAllByTeacherIdOrderByInfo(teacherId);
    }

    @Override
    public Student getStudent(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        return studentOptional.orElseThrow();
    }
}
