package net.sunder.english.service;

import net.sunder.english.domain.Teacher;

public interface TeacherService {

    boolean isExistTeacherId(String teacherId);
    void createAccount(Teacher teacher);
    Long login(Teacher teacher);
    void updateAccount(Teacher teacher);
    void withdraw(Long teacherId);
}
