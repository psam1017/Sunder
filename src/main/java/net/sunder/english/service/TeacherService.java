package net.sunder.english.service;

import net.sunder.english.domain.Teacher;

public interface TeacherService {

    boolean isUniqueTeacherId(String teacherId);
    void createAccount(String loginId, String password, String teacherName);
    Teacher login(String loginId, String password);
    void updateAccount(Long teacherId, String teacherName, String password);
    void withdraw(Long teacherId);
    Teacher getTeacher(Long teacherId);
}
