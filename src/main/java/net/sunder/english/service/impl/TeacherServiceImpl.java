package net.sunder.english.service.impl;

import lombok.RequiredArgsConstructor;
import net.sunder.english.domain.Teacher;
import net.sunder.english.repository.TeacherRepository;
import net.sunder.english.service.TeacherService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public boolean isUniqueTeacherId(String teacherId) {
        return !teacherRepository.existsByTeacherId(teacherId);
    }

    @Override
    @Transactional
    public void createAccount(String loginId, String password, String teacherName) {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(loginId);
        teacher.setPassword(password);
        teacher.setTeacherName(teacherName);

        if (isUniqueTeacherId(teacher.getTeacherId())) {
            teacherRepository.save(teacher);
        }
        else {
            throw new DuplicateKeyException("이미 존재하는 아이디입니다.");
        }
    }

    @Override
    public Teacher login(String loginId, String password) {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(loginId);
        teacher.setPassword(password);
        Optional<Teacher> teacherOptional = teacherRepository.findOne(Example.of(teacher));
        return teacherOptional.orElseGet(Teacher::new);
    }

    @Override
    public void updateAccount(Long teacherId, String teacherName, String password) {
        Teacher teacher = getTeacher(teacherId);
        teacher.setTeacherName(teacherName);
        teacher.setPassword(password);
    }

    @Override
    public void withdraw(Long teacherId) {
        teacherRepository.deleteById(teacherId);
    }

    @Override
    public Teacher getTeacher(Long teacherId) {
        return teacherRepository.findById(teacherId).orElseThrow();
    }
}
