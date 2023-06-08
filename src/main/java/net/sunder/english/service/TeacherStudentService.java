package net.sunder.english.service;

import net.sunder.english.domain.Student;
import net.sunder.english.domain.Teacher;
import net.sunder.english.domain.enumtype.Grade;

import java.util.List;

public interface TeacherStudentService {

    boolean isUniqueStudentId(String studentId);
    void createAccount(String studentId, String password, String studentName, Grade grade, Teacher teacher);
    void updateAccount(Teacher teacher, Long studentId, String studentName, String password, Grade grade);
    void withdraw(Teacher teacher, Long studentId);
    List<Student> getAllStudent(Long teacherId);
    Student getStudent(Long studentId);
}
