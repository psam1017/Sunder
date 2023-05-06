package net.sunder.english.service;

import net.sunder.english.domain.Student;

public interface StudentService {

    boolean isExistStudentId(String studentId);
    void createAccount(Student student);
    Long login(Student student);
    void updateAccount(Student student);
    void withdraw(Long studentId);
}
