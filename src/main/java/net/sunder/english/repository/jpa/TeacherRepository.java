package net.sunder.english.repository.jpa;

import net.sunder.english.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByTeacherId(String teacherId);
}
