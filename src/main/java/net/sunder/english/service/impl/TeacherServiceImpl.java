package net.sunder.english.service.impl;

import lombok.RequiredArgsConstructor;
import net.sunder.english.domain.Teacher;
import net.sunder.english.repository.jpa.TeacherRepository;
import net.sunder.english.service.TeacherService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public boolean isExistTeacherId(String teacherId) {
        return teacherRepository.existsByTeacherId(teacherId);
    }

    @Override
    @Transactional
    public void createAccount(Teacher teacher) {
        if (isExistTeacherId(teacher.getTeacherId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        else{
            teacherRepository.save(teacher);
        }
    }

    @Override
    public Long login(Teacher teacher) {
        Optional<Teacher> teacherOptional = teacherRepository.findOne(Example.of(teacher));
        return teacherOptional.orElseThrow(NoSuchElementException::new).getId();
    }

    @Override
    public void updateAccount(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    @Override
    public void withdraw(Long teacherId) {
        teacherRepository.deleteById(teacherId);
    }
}
