package net.sunder.english.service;

import lombok.extern.slf4j.Slf4j;
import net.sunder.english.domain.Teacher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:sql/dummy.sql"})
class TeacherServiceTest {

    private final TeacherService teacherService;
    private final TeacherStudentService teacherStudentService;

    @Autowired
    public TeacherServiceTest(TeacherService teacherService, TeacherStudentService teacherStudentService) {
        this.teacherService = teacherService;
        this.teacherStudentService = teacherStudentService;
    }

    @Test
    @DisplayName("선생님이 회원가입과 로그인을 할 수 있다")
    void createAccountTest() {
        // given
        String loginId = "newTeacherId";
        String password = "new1234";
        String teacherName = "newTeacher";

        // when
        boolean result = teacherService.isUniqueTeacherId(loginId);
        teacherService.createAccount(loginId, password, teacherName);
        Teacher loginTeacher = teacherService.login(loginId, password);

        // when
        assertThat(result).isTrue();
        assertThat(loginTeacher).isNotNull();
    }

    @Test
    @DisplayName("선생님이 계정 정보를 변경할 수 있다")
    void updateAccountTest() {
        // given
        String loginId = "teacherId";
        String password = "1234";
        Teacher loginTeacher = teacherService.login(loginId, password);

        // when
        Long id = loginTeacher.getId();
        String newName = "newName";
        teacherService.updateAccount(id, newName, password);

        // then
        Teacher updatedTeacher = teacherService.getTeacher(id);
        assertThat(loginTeacher.getTeacherName()).isEqualTo(updatedTeacher.getTeacherName());
    }

    @Test
    @DisplayName("선생님이 탈퇴할 수 있다")
    void withdrawTest() {
        // given
        String loginId = "teacherId";
        String password = "1234";
        Teacher loginTeacher = teacherService.login(loginId, password);

        // when
        teacherService.withdraw(loginTeacher.getId());

        // then
        boolean result = teacherStudentService.isUniqueStudentId("studentId1");
        assertThat(result).isTrue();
    }
}
